package lighthouse;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Tokenizer {
    public static final String INPUT_DIR = "files";
    public static final String OUTPUT_DIR = "files_out";

    private Tokenizer() {
    }

    private static void indexDirectory(Path inputDirPath, Path outputDirPath) {
        /*
         * Perform tokenization across all files in input directory +
         * Place resulting token files in the specified output directory
         */

        // if output directory does not exist, make it
        File outputDir = outputDirPath.toFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File inputDir = inputDirPath.toFile();

        // loop through all files in input directory
        if (inputDir.exists() && inputDir.isDirectory()) {

            // create array of pathnames to files in the directory
            File[] files = inputDir.listFiles();
            System.out.printf("Found %d documents to index in input directory.", files.length);

            // loop through each file in the array of all files in input directory
            for (File file : files) {
                // TODO: open file for tokenization here
                System.out.println(file.getName());
            }
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }

    }

    public static void tokenize() {
        Path inputDir = Paths.get(INPUT_DIR);
        Path outputDir = Paths.get(OUTPUT_DIR);
        indexDirectory(inputDir, outputDir);
    }
}
