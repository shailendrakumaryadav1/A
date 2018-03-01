package archive.tool.core;

import java.io.IOException;

public interface Compressor {

    boolean compress() throws IOException;

    Exception getLastError();

}
