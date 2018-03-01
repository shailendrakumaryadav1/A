package archive.tool.core.impl;

import archive.tool.console.Settings;
import archive.tool.core.Constants;
import archive.tool.core.MultiToOneOutput;
import archive.tool.core.interfaces.Compressor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.zip.DeflaterInputStream;

public class CompressorImpl implements Compressor {
    private File sourcePath, destPath;
    private int sourcePathLen;
    private int maxSize;
    private int workerThreadsCount;
    private boolean hasToStop;
    private Exception lastError;

    public Exception getLastError() {
        return lastError;
    }

    private Queue<File> inputQueue;

    public boolean compress(Settings settings) throws IllegalArgumentException, IOException {
        this.sourcePath = new File(settings.getInputDirCompress()).getCanonicalFile();
        sourcePathLen = sourcePath.getCanonicalPath().length();
        this.destPath = new File(settings.getOutputDirCompress());
        this.maxSize = settings.getMaxSizeCompress();
        this.workerThreadsCount = settings.getWorkerThreadsCount();
        lastError = null;
        hasToStop = false;

        // Validate arguments
        if (maxSize <= 0)
            return setError("Maximum MB should be greater than zero");
        if (!sourcePath.exists() || !sourcePath.isDirectory())
            return setError("Source path does not exist or is not a directory");
        if (!sourcePath.canRead())
            return setError("Source path cannot be read");
        if (destPath.isFile())
            return setError("Destination path is an existing file");
        if (destPath.isDirectory() && !destPath.canWrite())
            return setError("Destination path cannot be written");

        // Obtain input file list
        inputQueue = new ArrayDeque<>();
        String sourcePathStr = sourcePath.getCanonicalPath();
        if (!loadInputList(sourcePath, new FilesFilter(sourcePathStr), new DirectoriesFilter(sourcePathStr)))
            return false;

        if (inputQueue.isEmpty())
            return setError("No files/folders to compress.");

        // Instantiate worker threads
        CompressorThread[] threads = new CompressorThread[workerThreadsCount];
        for (int i = 0; i < workerThreadsCount; i++) {
            threads[i] = new CompressorThread(i);
            Thread thread = new Thread(threads[i]);
            thread.setDaemon(true);
            thread.setName(String.format("CompressorThread-%d", i));
            thread.start();
        }

        // Manage work
        return manage();
    }

    protected final Object lock = new Object();
    protected final Object threadsLock = new Object();

    private boolean manage() throws IOException {
        MultiToOneOutput output = new MultiToOneOutput(destPath.toPath(), Constants.BASE_NAME, maxSize);
        int working = workerThreadsCount;
        while (!inputQueue.isEmpty() || working > 0) {
            // Wait for idle thread
            synchronized (lock) {
                try {
                    if (workerMsg == null)
                        lock.wait();
                    if (workerMsg == null)
                        continue; // Transient awake

                    // Get thread petition
                    synchronized (workerMsg) {
                        switch (workerMsg.cmd) {
                            case GET_NEXT_FILE: {
                                if (inputQueue.isEmpty()) { // Is no more work to be done, signal termination
                                    workerMsg.file = null;
                                    working--;
                                } else {
                                    workerMsg.file = inputQueue.remove();
                                }
                                break;
                            }
                            case PROCESS_BUFFER: {
                                output.write(workerMsg.id, workerMsg.msgCmd, workerMsg.buffer, workerMsg.currentSize);
                                break;
                            }
                            case EXCEPTION: {
                                hasToStop = true;
                                return false;
                            }
                        }
                        workerMsg.ack = true;
                        workerMsg.notify();
                    }
                } catch (InterruptedException ie) {
                }
                clearWorkerMsg();
            }
        }
        output.close();
        return true;
    }

    CompressorThread workerMsg;

    private void clearWorkerMsg() {
        workerMsg = null;
    }

    private boolean loadInputList(File currentPath, FileFilter filesFilter, FileFilter directoriesFilter) throws IOException {
        for (File item : currentPath.listFiles(filesFilter)) {
            if (!item.canRead())
                return setError(String.format("File %s cannot be read", item.getCanonicalPath()));
            inputQueue.add(item);
        }
        for (File item : currentPath.listFiles(directoriesFilter)) {
            if (!item.canRead())
                return setError(String.format("Directory %s cannot be read", item.getCanonicalPath()));
            inputQueue.add(item);
            loadInputList(item, filesFilter, directoriesFilter);
        }
        return true;
    }

    protected boolean setError(String cause) {
        lastError = new IllegalArgumentException(cause);
        return false;
    }

    protected boolean setError(Exception cause) {
        lastError = cause;
        return false;
    }

    class FilesFilter implements FileFilter {
        private String baseStr;

        public FilesFilter(String baseStr) {
            this.baseStr = baseStr;
        }

        public boolean accept(File file) {
            try {
                return file.isFile() &&
                        file.getCanonicalPath().startsWith(baseStr); // avoids links
            } catch (IOException e) {
                return false;
            }
        }
    }

    class DirectoriesFilter implements FileFilter {
        private String baseStr;

        public DirectoriesFilter(String baseStr) {
            this.baseStr = baseStr;
        }

        public boolean accept(File file) {
            try {
                return file.isDirectory() &&
                        file.getCanonicalPath().startsWith(baseStr); // avoids links
            } catch (IOException e) {
                return false;
            }
        }
    }

    enum WorkerCmd {
        GET_NEXT_FILE,
        PROCESS_BUFFER,
        EXCEPTION
    }

    class CompressorThread implements Runnable {
        WorkerCmd cmd;
        File file;
        boolean ack;
        private static final int MAX_BUFFER_SIZE = 16 << 20;// 16 MB

        int id, currentSize;
        byte[] buffer;
        byte msgCmd;

        public CompressorThread(int id) {
            this.id = id;
            int bufferSize = maxSize << 20;
            if (bufferSize > MAX_BUFFER_SIZE)
                bufferSize = MAX_BUFFER_SIZE;
            buffer = new byte[bufferSize];
        }

        private boolean sendMessage(WorkerCmd msg, byte msgCmd) throws InterruptedException {
            return sendMessage(msg, msgCmd, 0);
        }

        private boolean sendMessage(WorkerCmd msg, byte msgCmd, int size) throws InterruptedException {
            synchronized (threadsLock) {
                synchronized (lock) {
                    if (hasToStop)
                        return false;
                    workerMsg = this;
                    cmd = msg;
                    this.msgCmd = msgCmd;
                    currentSize = size;

                    ack = false;
                    // Ask for work
                    lock.notify();
                }
                // Wait for ACK
                synchronized (this) {
                    while (!ack)
                        wait();
                    if (file == null) // If there is no more work to be done, exit thread
                        return false;
                }
            }
            return true;
        }

        public void run() {
            while (true) {
                try {
                    // Get work to do
                    if (!sendMessage(WorkerCmd.GET_NEXT_FILE, Constants.CMD_NONE))
                        return;

                    // Compress this file
                    String subName = getSubName(file);

                    System.out.println(String.format("ThreadId: %d FileToCompress: %s", id, subName));


                    // Send item type, name and attributes
                    buffer[0] = (byte) ((file.isDirectory() ? Constants.FOLDER_MARKER : Constants.FILE_MARKER) |
                            (file.canRead() ? Constants.MARKER_CAN_READ : Constants.MARKER_NONE) |
                            (file.canWrite() ? Constants.MARKER_CAN_WRITE : Constants.MARKER_NONE) |
                            (file.canExecute() ? Constants.MARKER_CAN_EXECUTE : Constants.MARKER_NONE));
                    byte[] subNameInfo = subName.getBytes(StandardCharsets.UTF_8);
                    System.arraycopy(subNameInfo, 0, buffer, 1, subNameInfo.length);
                    if (!sendMessage(WorkerCmd.PROCESS_BUFFER, Constants.CMD_START_FILE, subNameInfo.length + 1))
                        return;

                    if (file.isFile()) { // If its a file, send the compressed data
                        BufferedInputStream inputStream = new BufferedInputStream(getCompressorStream(new FileInputStream(file)));
                        int currentRead = 0, len = buffer.length;
                        int size = 0;
                        while ((currentRead = inputStream.read(buffer, size, len)) != -1) {
                            len -= currentRead;
                            size += currentRead;
                            if (len == 0) {
                                if (!sendMessage(WorkerCmd.PROCESS_BUFFER, Constants.CMD_WRITE_FILE, size))
                                    return;
                                len = buffer.length;
                                size = 0;
                            }
                        }
                        if (size != 0 && !sendMessage(WorkerCmd.PROCESS_BUFFER, Constants.CMD_WRITE_FILE, size))
                            return;
                    }

                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (Exception e) {
                    synchronized (lock) {
                        workerMsg = this;
                        cmd = WorkerCmd.EXCEPTION;
                        setError(e);
                        lock.notify();
                        return;
                    }
                }
            }
        }

        private String getSubName(File file) throws IOException {
            return file.getCanonicalPath().substring(sourcePathLen);
        }

        // This method is the entry point to change the compression algorithm
        // TODO: implement a method like getCompressorStream() for Decompressor
        protected InputStream getCompressorStream(InputStream inputStream) {
            return new DeflaterInputStream(inputStream);
        }

    }
}
