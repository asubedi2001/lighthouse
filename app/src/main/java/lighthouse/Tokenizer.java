package lighthouse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;

public class Tokenizer {
    public static final String INPUT_DIR = "files";
    public static final String OUTPUT_DIR = "files_out";
    public static final String STOPLIST_PATH = "stoplist.txt";

    private Tokenizer() {
    }

    public static Set<String> loadStoplist(String stoplistPath) {
        InputStream stoplistInputStream = ClassLoader.getSystemResourceAsStream(stoplistPath);
        BufferedReader stoplistReader = new BufferedReader(
                new InputStreamReader(stoplistInputStream, StandardCharsets.UTF_8));

        return stoplistReader.lines().collect(Collectors.toSet());
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

    public static void tokenizeFile(File f) {
        try {
            // grab all visible text from the HTML file
            String allText = Jsoup.parse(f).body().text();

            String[] tokens = allText.split("\s+");

            for (String t : tokens) {
                // remove special characters and normalize the token
                t = t.replaceAll("[!()_,.?]", "");
                t = t.toLowerCase();

                // filter stopwords
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void tokenize() {
        Path inputDir = Paths.get(INPUT_DIR);
        Path outputDir = Paths.get(OUTPUT_DIR);
        indexDirectory(inputDir, outputDir);
    }
}
