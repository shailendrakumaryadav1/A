package archive;

import archive.tool.console.Settings;
import archive.tool.core.impl.DecompressorImpl;
import archive.tool.core.interfaces.Decompressor;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DecompressorTest {

    @Test
    public void shouldDecompress() throws Exception {

        Settings settings = new Settings();
        settings.setInputDirDecompress("/Users/kumaryadav/Desktop/JustExampleFolder/out");
        settings.setOutputDirDecompress("/Users/kumaryadav/Desktop/JustExampleFolder/in_1");

        Decompressor decompressor = new DecompressorImpl();
        Assert.assertTrue(decompressor.decompress(settings));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCompressFailsForSourceDirDoesNotExist() throws IOException {

        Settings settings = new Settings();
        settings.setInputDirDecompress("/Users/kumaryadav/Desktop/JustExampleFolder/out" + Math.random() + Math.random() + File.separator);
        settings.setOutputDirDecompress("/Users/kumaryadav/Desktop/JustExampleFolder/in");

        Decompressor decompressor = new DecompressorImpl();
        decompressor.decompress(settings);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCompressFailsForSourceIsNotADirectory() throws IOException {

        Settings settings = new Settings();
        settings.setInputDirDecompress("/Users/kumaryadav/Desktop/JustExampleFolder/out/staff.csv");
        settings.setOutputDirDecompress("/Users/kumaryadav/Desktop/JustExampleFolder/in");

        Decompressor decompressor = new DecompressorImpl();
        decompressor.decompress(settings);

    }

}
