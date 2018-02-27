package archive.tool;

import archive.tool.console.Action;
import archive.tool.console.Console;
import archive.tool.console.Settings;
import archive.tool.core.Compressor;
import archive.tool.core.Decompressor;
import archive.tool.core.impl.ZipCompressor;
import archive.tool.core.impl.ZipDecompressor;

public class Main {

    public static void main(String... args) throws Exception {
        System.out.println("Start program");

        // TODO: Do the work here.
        Console console = new Console();
        console.enterSettings();

        if (Settings.action == Action.COMPRESS) {
            // TODO: Do compress here using settings
            Compressor compressor = new ZipCompressor();
            compressor.compress();
        } else {
            // TODO: Do decompress here using settings
            Decompressor decompressor = new ZipDecompressor();
            decompressor.decompress();
        }

        System.out.println("End Program");

    }
}
