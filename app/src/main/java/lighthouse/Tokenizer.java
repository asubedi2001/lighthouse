package lighthouse;

import java.io.*;
import java.nio.file.Path;

public class Tokenizer {

    public static void indexDirectory(String inputDir, String outputDir) {
        /*
         * Allow for only input/output parameter to be passed, with -1 as maxDocuments
         * -> all documents parsed
         */
        indexDirectory(inputDir, outputDir, -1);
    }

    public static void indexDirectory(String inputDirPath, String outputDirPath, int maxDocuments) {
        /*
         * Perform tokenization across all files in input directory +
         * Place resulting token files in the specified output directory
         * 
         * Will parse UP TO maxDocuments # of files.
         */

        // if directory does not exist, make it
        File inputDir = new File(inputDirPath);
        if (!inputDir.exists()) {
            if (inputDir.mkdirs()) {
                System.out.println("Input directory created as it did not exist prior.");
            } else {
                System.out.println("Input directory does not exist, and program failed to create it.");
            }
        }

        // loop through all files in input directory
        if (inputDir.exists() && inputDir.isDirectory()) {
            // create array of pathnames to files in the directory
            File[] files = inputDir.listFiles();
            System.out.println(String.format("Found %d documents to index in input directory.", files.length));
            if (files != null) {
                // loop through each file in the array of all files in input directory
                for (File file : files) {

                }
            }
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }

    }

    public static void main(String[] args) {
        String inputDir = "./files/";
        String outputDir = "./files_out/";

        if (args.length != 2) {
            System.out.println("Please pass an input/output directory. Proceeding with default values.");
        } else {
            inputDir = args[0];
            outputDir = args[1];

            inputDir = inputDir.replace("\\", "/");
            outputDir = outputDir.replace("\\", "/");

            if (!inputDir.endsWith("/")) {
                inputDir += "/";
            }
            if (!outputDir.endsWith("/")) {
                outputDir += "/";
            }

            indexDirectory(inputDir, outputDir);
        }
    }
}
