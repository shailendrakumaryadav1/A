package archive.tool.core.interfaces;

import java.io.IOException;

public interface Compressor {

    boolean compress() throws IOException;

    Exception getLastError();

}
