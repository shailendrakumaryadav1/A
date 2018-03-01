package archive.tool.core.interfaces;

import java.io.IOException;

public interface IOneToMultiProcessor {
    boolean process(int id, byte cmd, byte[] buffer, int size) throws IOException;

    boolean close() throws IOException;
}
