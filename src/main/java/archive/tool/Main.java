package archive.tool;

import archive.tool.console.Console;
import archive.tool.console.Settings;
import archive.tool.core.interfaces.Compressor;
import archive.tool.core.interfaces.Decompressor;
import archive.tool.core.impl.CompressorImpl;
import archive.tool.core.impl.DecompressorImpl;

public class Main {

    public static void main(String... args) throws Exception {
        System.out.println("Start program");

        // TODO: Do the work here.
        Console console = new Console();
        console.enterSettings();

        switch (Settings.action) {
            case COMPRESS:
                // TODO: Do compress here using settings
                Compressor compressor = new CompressorImpl();
                try {
                    if (!compressor.compress()) {
                        compressor.getLastError().printStackTrace(System.err);
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                break;
            case DECOMPRESS:
                // TODO: Do decompress here using settings
                Decompressor decompressor = new DecompressorImpl();
                try {
                    decompressor.decompress();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                break;
        }
        System.out.println("End Program");

    }
}
