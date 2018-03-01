package archive.tool.core;

import java.io.IOException;

public interface IMultiToOneProcessor {

    boolean initialize();

    boolean process(byte cmd, byte[] buffer, int size) throws IOException;

    boolean close() throws IOException;
}
