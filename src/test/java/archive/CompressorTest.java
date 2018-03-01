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

        Settings settings = new Settings();
        settings.setInputDirCompress("/Users/kumaryadav/Desktop/JustExampleFolder");
        settings.setOutputDirCompress("/Users/kumaryadav/Desktop/JustExampleFolder/out");
        settings.setMaxSizeCompress(7); // 7 MB

        Compressor compressor = new CompressorImpl();
        Assert.assertTrue(compressor.compress(settings));
    }

    @Test
    public void shouldCompressFailsForSourceDirDoesNotExist() throws IOException {

        Settings settings = new Settings();
        settings.setInputDirCompress("/Users/kumaryadav/Desktop/" + Math.random() + Math.random() + File.separator);
        settings.setOutputDirCompress("/Users/kumaryadav/Desktop/JustExampleFolder/out");
        settings.setMaxSizeCompress(1);

        Compressor compressor = new CompressorImpl();
        Assert.assertFalse(compressor.compress(settings));

    }

    @Test
    public void shouldCompressFailsForSourceIsNotADirectory() throws IOException {

        Settings settings = new Settings();
        settings.setInputDirCompress("/Users/kumaryadav/Desktop/staff.csv");
        settings.setOutputDirCompress("/Users/kumaryadav/Desktop/JustExampleFolder/out");
        settings.setMaxSizeCompress(1);

        Compressor compressor = new CompressorImpl();
        Assert.assertFalse(compressor.compress(settings));

    }


}
