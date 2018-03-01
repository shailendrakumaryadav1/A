package archive.tool.core.impl;

import archive.tool.console.Settings;
import archive.tool.core.Decompressor;
import archive.tool.core.FileUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class DecompressorImpl implements Decompressor {

    private Map<String, File> largeFiles = new TreeMap<>();
    private Map<String, FileOutputStream> largeFilesOutputStreams = new HashMap<>();

    /**
     * Decompress files. If archives contains splitted large files, stores
     * names and sorts by parts in map. And then decompress them from first part to the last one.
     */
    public void decompress() throws IOException {
        System.out.println("Start decompress.");
        File inputDir = new File(Settings.inputUnzipDir);
        File[] archives = inputDir.listFiles();
        for (File archive : archives) {
            decompressArchive(archive);
        }
        decompressLargeFiles();
    }

    /**
     * Decompress single archive.
     *
     * @param file target archive.
     * @throws IOException when file not found or error while read/write to file.
     */
    private void decompressArchive(File file) throws IOException {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        ZipEntry ze = zis.getNextEntry();
        while (ze != null) {
            String fileName = ze.getName();
            if (fileName.contains("_part")) {
                System.out.println("Part of large file " + fileName + " found");
                largeFiles.put(fileName, file);
                ze = zis.getNextEntry();
                continue;
            }
            File newFile = new File(Settings.outputUnzipDir + File.separator + fileName);
            System.out.println("Decompress : " + newFile.getAbsoluteFile());

            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            FileUtil.write(zis, fos);
            fos.close();
            ze = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        System.out.println("Done");
    }

    /**
     * Decompress large files.
     *
     * @throws IOException when file not found or error while read/write to file.
     */
    private void decompressLargeFiles() throws IOException {
        if (largeFiles.isEmpty()) {
            return;
        }
        for (Map.Entry<String, File> entry : largeFiles.entrySet()) {
            String fileName = entry.getKey();
            String shortFileName = Settings.outputUnzipDir + File.separator +
                    fileName.substring(0, fileName.indexOf("_part"));
            FileOutputStream fos;
            if (!largeFilesOutputStreams.keySet().contains(shortFileName)) {
                File dir = new File(shortFileName).getParentFile();
                if (!dir.exists()) {
                    boolean mkdir = dir.mkdirs();
                    if (!mkdir) {
                        throw new RuntimeException("Cannot create directory for " + shortFileName);
                    }
                }
                fos = new FileOutputStream(shortFileName);
                largeFilesOutputStreams.put(shortFileName, fos);
            } else {
                fos = largeFilesOutputStreams.get(shortFileName);
            }

            ZipFile zipFile = new ZipFile(entry.getValue());
            ZipEntry zipEntry = zipFile.getEntry(fileName);
            InputStream zis = zipFile.getInputStream(zipEntry);
            FileUtil.write(zis, fos);
        }
    }
}
