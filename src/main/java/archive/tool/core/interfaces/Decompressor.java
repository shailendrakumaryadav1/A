package archive.tool.core.interfaces;

import archive.tool.console.Settings;

import java.io.IOException;

public interface Decompressor {

    boolean decompress(Settings settings) throws IOException;

}
