package archive.tool.console;

import archive.tool.core.Constants;

public class Settings {

    private Action action;

    //compress settings
    private String inputDirCompress;
    private String outputDirCompress;
    private int maxSizeCompress;

    //decompress settings
    private String inputDirDecompress;
    private String outputDirDecompress;

    private int workerThreadsCount;

    public Settings() {
        this.workerThreadsCount = Constants.DEFAULT_WORKER_THREADS_COUNT;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getInputDirCompress() {
        return inputDirCompress;
    }

    public void setInputDirCompress(String inputDirCompress) {
        this.inputDirCompress = inputDirCompress;
    }

    public String getOutputDirCompress() {
        return outputDirCompress;
    }

    public void setOutputDirCompress(String outputDirCompress) {
        this.outputDirCompress = outputDirCompress;
    }

    public int getMaxSizeCompress() {
        return maxSizeCompress;
    }

    public void setMaxSizeCompress(int maxSizeCompress) {
        this.maxSizeCompress = maxSizeCompress;
    }

    public String getInputDirDecompress() {
        return inputDirDecompress;
    }

    public void setInputDirDecompress(String inputDirDecompress) {
        this.inputDirDecompress = inputDirDecompress;
    }

    public String getOutputDirDecompress() {
        return outputDirDecompress;
    }

    public void setOutputDirDecompress(String outputDirDecompress) {
        this.outputDirDecompress = outputDirDecompress;
    }

    public int getWorkerThreadsCount() {
        return workerThreadsCount;
    }

}