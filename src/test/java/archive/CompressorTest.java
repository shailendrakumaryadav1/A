package archive;

import archive.tool.console.Settings;
import archive.tool.core.impl.CompressorImpl;
import org.junit.Test;

public class CompressorTest {

    @Test
    public void shouldCompressSucceeds() throws Exception {
        System.out.println("Start compress test");

        Settings.inputZipDir = "/Users/kumaryadav/Desktop/JustExampleFolder/in";
        Settings.outputZipDir = "/Users/kumaryadav/Desktop/JustExampleFolder/out";
        Settings.maxSize = 7000;

        CompressorImpl compressor = new CompressorImpl();
        compressor.compress();
        System.out.println("Done");
    }

}
