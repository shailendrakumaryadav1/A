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
        System.out.println("Start compress test");

        Settings.inputZipDir = "/Users/kumaryadav/Desktop/JustExampleFolder";
        Settings.outputZipDir = "/Users/kumaryadav/Desktop/JustExampleFolder/out";
        Settings.maxSize = 7; // 7 MB

        Compressor compressor = new CompressorImpl();
        Assert.assertTrue(compressor.compress());
        System.out.println("Done");
    }

    @Test
    public void shouldCompressFailsForSourceDirDoesNotExist() throws IOException {

        Settings.inputZipDir = "/Users/kumaryadav/Desktop/" + Math.random() + Math.random() + File.separator;
        System.out.println(Settings.inputZipDir);
        Settings.outputZipDir = "/Users/kumaryadav/Desktop/JustExampleFolder/out";
        Settings.maxSize = 1;

        Compressor compressor = new CompressorImpl();
        Assert.assertFalse(compressor.compress());

    }

    @Test
    public void shouldCompressFailsForSourceIsNotADirectory() throws IOException {

        Settings.inputZipDir = "/Users/kumaryadav/Desktop/staff.csv";
        Settings.outputZipDir = "/Users/kumaryadav/Desktop/JustExampleFolder/out";
        Settings.maxSize = 1;

        Compressor compressor = new CompressorImpl();
        Assert.assertFalse(compressor.compress());

    }


}
