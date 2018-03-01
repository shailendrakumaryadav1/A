package archive.tool.console;

import archive.tool.core.Constants;

public class Settings {

    public static Action action;

    //compress settings
    public static String inputZipDir;
    public static String outputZipDir;
    public static int maxSize;

    //decompress settings
    public static String inputUnzipDir;
    public static String outputUnzipDir;

    public static int workerThreadsCount = Constants.DEFAULT_WORKER_THREADS_COUNT;
}