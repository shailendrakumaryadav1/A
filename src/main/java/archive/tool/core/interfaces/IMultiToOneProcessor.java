package archive.tool.core.interfaces;

import java.io.IOException;

public interface IMultiToOneProcessor {

    boolean initialize();

    boolean process(byte cmd, byte[] buffer, int size) throws IOException;

    boolean close() throws IOException;
}
