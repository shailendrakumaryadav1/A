package archive.tool.core.interfaces;

import archive.tool.console.Settings;

import java.io.IOException;

public interface Compressor {

    boolean compress(Settings settings) throws IOException;

    Exception getLastError();

}
