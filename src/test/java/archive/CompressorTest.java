package archive;

import archive.tool.console.Settings;
import archive.tool.core.impl.CompressorImpl;
import archive.tool.core.interfaces.Compressor;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CompressorTest {


    @Test
    public void shouldCompressSucceeds() throws Exception {

        Settings.inputDirCompress = "/Users/kumaryadav/Desktop/JustExampleFolder";
        Settings.outputDirCompress = "/Users/kumaryadav/Desktop/JustExampleFolder/out";
        Settings.maxSizeCompress = 7; // 7 MB

        Compressor compressor = new CompressorImpl();
        Assert.assertTrue(compressor.compress());
    }

    @Test
    public void shouldCompressFailsForSourceDirDoesNotExist() throws IOException {

        Settings.inputDirCompress = "/Users/kumaryadav/Desktop/" + Math.random() + Math.random() + File.separator;
        Settings.outputDirCompress = "/Users/kumaryadav/Desktop/JustExampleFolder/out";
        Settings.maxSizeCompress = 1;

        Compressor compressor = new CompressorImpl();
        Assert.assertFalse(compressor.compress());

    }

    @Test
    public void shouldCompressFailsForSourceIsNotADirectory() throws IOException {

        Settings.inputDirCompress = "/Users/kumaryadav/Desktop/staff.csv";
        Settings.outputDirCompress = "/Users/kumaryadav/Desktop/JustExampleFolder/out";
        Settings.maxSizeCompress = 1;

        Compressor compressor = new CompressorImpl();
        Assert.assertFalse(compressor.compress());

    }


}
