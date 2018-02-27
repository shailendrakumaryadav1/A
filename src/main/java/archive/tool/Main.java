package archive.tool;

import archive.tool.console.Action;
import archive.tool.console.Console;
import archive.tool.console.Settings;

public class Main {

    public static void main(String... args) throws Exception {
        System.out.println("Start program");

        // TODO: Do the work here.
        Console console = new Console();
        console.enterSettings();

        if (Settings.action == Action.COMPRESS) {
            // TODO: Do compress here using settings
        } else {
            // TODO: Do decompress here using settings
        }

        System.out.println("End Program");

    }
}
