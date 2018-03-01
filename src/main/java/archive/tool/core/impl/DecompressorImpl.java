package archive.tool.core.impl;

import archive.tool.console.Settings;
import archive.tool.core.Constants;
import archive.tool.core.Decompressor;
import archive.tool.core.IOneToMultiProcessor;
import archive.tool.core.OneInputToMulti;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class DecompressorImpl implements Decompressor, IOneToMultiProcessor {

    private File sourcePath, destPath;
    private HashMap<Integer, OutputDecompressor> processors;

    public boolean decompress() throws IOException {
        sourcePath = new File(Settings.inputUnzipDir).getCanonicalFile();
        destPath = new File(Settings.outputUnzipDir);
        processors = new HashMap<>();

        // Validate arguments
        if (!sourcePath.exists() || !sourcePath.isDirectory())
            throw new IllegalArgumentException("Source path does not exist or is not a directory");
        if (!sourcePath.canRead())
            throw new IllegalArgumentException("Source path cannot be read");
        if (destPath.isFile())
            throw new IllegalArgumentException("Destination path is an existing file");
        if (destPath.isDirectory() && !destPath.canWrite())
            throw new IllegalArgumentException("Destination path cannot be written");

        OneInputToMulti input = new OneInputToMulti(sourcePath.toPath(), Constants.BASE_NAME);
        return input.process(this);
    }

    @Override
    public boolean process(int id, byte cmd, byte[] buffer, int size) throws IOException {
        OutputDecompressor decompressor = processors.get(id);
        if (decompressor == null) {
            decompressor = new OutputDecompressor();
            if (!decompressor.initialize())
                return false;
            processors.put(id, decompressor);
        }
        return decompressor.process(cmd, buffer, size);
    }

    @Override
    public boolean close() {
        return true;
    }

    class OutputDecompressor {
        public boolean initialize() {
            return true;
        }

        public boolean process(byte cmd, byte[] buffer, int size) {
            switch (cmd) {
                case Constants.CMD_START_FILE: {
                    String subName = new String(buffer, 1, size - 1, StandardCharsets.UTF_8);
                    System.out.println(String.format("FileToDecompress: %s", subName));

                    break;
                }
                case Constants.CMD_WRITE_FILE: {
                    // Here we would write the contents of the buffer on the current file
                    break;
                }
                default:
                    return false;
            }
            return true;
        }
    }
}
