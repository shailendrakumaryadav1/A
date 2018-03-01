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

        Settings.inputDirDecompress = "/Users/kumaryadav/Desktop/JustExampleFolder/out";
        Settings.outputDirDecompress = "/Users/kumaryadav/Desktop/JustExampleFolder/in_1";

        Decompressor decompressor = new DecompressorImpl();
        Assert.assertTrue(decompressor.decompress());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCompressFailsForSourceDirDoesNotExist() throws IOException {

        Settings.inputDirDecompress = "/Users/kumaryadav/Desktop/JustExampleFolder/out" + Math.random() + Math.random() + File.separator;
        Settings.outputDirDecompress = "/Users/kumaryadav/Desktop/JustExampleFolder/in";

        Decompressor decompressor = new DecompressorImpl();
        decompressor.decompress();

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCompressFailsForSourceIsNotADirectory() throws IOException {

        Settings.inputDirDecompress = "/Users/kumaryadav/Desktop/JustExampleFolder/out/staff.csv";
        Settings.outputDirDecompress = "/Users/kumaryadav/Desktop/JustExampleFolder/in";

        Decompressor decompressor = new DecompressorImpl();
        decompressor.decompress();

    }

}
