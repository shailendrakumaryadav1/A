package archive.tool.console;

import archive.tool.core.Constants;

public class Settings {

    public static Action action;

    //compress settings
    public static String inputDirCompress;
    public static String outputDirCompress;
    public static int maxSizeCompress;

    //decompress settings
    public static String inputDirDecompress;
    public static String outputDirDecompress;

    public static int workerThreadsCount = Constants.DEFAULT_WORKER_THREADS_COUNT;
}