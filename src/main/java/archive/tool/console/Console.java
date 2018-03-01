package archive.tool.console;

import java.util.Scanner;

public class Console {

    private Settings settings;

    public void enterSettings() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n Select an action(enter 0 or 1):");
        System.out.println("[0] Compress");
        System.out.println("[1] Decompress");

        settings = new Settings();
        int action = scanner.nextInt();
        switch (action) {
            case 0:
                settings.setAction(Action.COMPRESS);
                enterCompressSettings(scanner);
                break;
            case 1:
                settings.setAction(Action.DECOMPRESS);
                enterDecompressSettings(scanner);
                break;
            default:
                System.out.println("Incorrect action, enter 0 or 1");
                System.exit(0);

        }
    }

    private void enterCompressSettings(Scanner scanner) {

        System.out.println("\n Enter path to Input directory:");
        String inputDir = scanner.next();
        settings.setInputDirCompress(inputDir);

        System.out.println("\n Enter path to Output directory:");
        String outputDir = scanner.next();
        settings.setOutputDirCompress(outputDir);

        System.out.println("\n Enter max output file size in Mbytes:");
        Integer maxSize = scanner.nextInt();
        settings.setMaxSizeCompress(maxSize);
    }

    private void enterDecompressSettings(Scanner scanner) {

        System.out.println("\n Enter path to Input directory:");
        String inputDir = scanner.next();
        settings.setInputDirDecompress(inputDir);

        System.out.println("\n Enter path to Output directory:");
        String outputDir = scanner.next();
        settings.setOutputDirDecompress(outputDir);
    }

    public Settings getSettings() {
        return settings;
    }
}
