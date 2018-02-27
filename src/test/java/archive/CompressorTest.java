package archive;

import archive.tool.console.Settings;
import archive.tool.core.impl.ZipCompressor;
import org.junit.Test;

public class CompressorTest {

    @Test
    public void shouldCompressSucceeds() throws Exception {
        System.out.println("Start compress test");

        Settings.inputZipDir = "/Users/kumaryadav/Desktop/JustExampleFolder/in";
        Settings.outputZipDir = "/Users/kumaryadav/Desktop/JustExampleFolder/out";
        Settings.maxSize = 7000;

        ZipCompressor compressor = new ZipCompressor();
        compressor.compress();
        System.out.println("Done");
    }

}
