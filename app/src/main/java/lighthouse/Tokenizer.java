package lighthouse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;

import lighthouse.util.ProgressBar;

public class Tokenizer {
    public static final String INPUT_DIR = "corpus";
    public static final String OUTPUT_DIR = "tokens";
    public static final String STOPLIST_PATH = "stoplist.txt";

    protected static final Set<String> STOPLIST = loadStoplist(STOPLIST_PATH);

    private Tokenizer() {
    }

    public static Set<String> loadStoplist(String stoplistPath) {
        InputStream stoplistInputStream = ClassLoader.getSystemResourceAsStream(stoplistPath);
        BufferedReader stoplistReader = new BufferedReader(
                new InputStreamReader(stoplistInputStream, StandardCharsets.UTF_8));

        return stoplistReader.lines().collect(Collectors.toSet());
    }

    private static void indexDirectory(String inputDirPath, String outputDirPath) {
        /*
         * Perform tokenization across all files in input directory and
         * place resulting token files in the specified output directory
         */
        File inputDir;
        File outputDir;
        inputDir = new File(inputDirPath);
        outputDir = new File(outputDirPath);
        // inputDir = new File(ClassLoader.getSystemResource(inputDirPath).toURI());
        // outputDir = new File(ClassLoader.getSystemResource(outputDirPath).toURI());

        // if output directory does not exist, make it
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // check input directory path
        if (!inputDir.exists()) {
            throw new IllegalArgumentException("Input directory is empty");
        }
        if (!inputDir.isDirectory()) {
            throw new IllegalArgumentException("Input directory path is not a directory");
        }

        File[] files = inputDir.listFiles();
        int numFiles = files.length;
        System.out.printf("Found %d documents to index in input directory.\n", numFiles);

        // tokenize each file in the input directory
        // write the token counter into the relevant output file

        long progressPct = Math.max(Math.round(numFiles * ProgressBar.TICKMARK_PCT), 1);
        System.out.println("(1/3) Tokenizing documents");

        int fileCounter = 0;
        for (File file : files) {

            if (fileCounter % progressPct == 0) {
                ProgressBar.printProgressBar(fileCounter, numFiles);
            }

            String outfileName = generateOutfileName(file, outputDirPath);
            File outfile = new File(outfileName);

            Map<String, Integer> tokens = tokenizeFile(file);
            writeTokens(outfile, tokens);
            fileCounter++;
        }
        System.out.println();
    }

    private static String generateOutfileName(File inputFile, String outputDirPath) {
        // OUTPUT_DIR + basename + ".csv"

        String filename = inputFile.getName();

        // stripping off the .html extension (assuming files conform to standard)
        String basename = filename.substring(0, filename.length() - 5);

        return outputDirPath + "\\" + basename + ".csv";
    }

    public static Map<String, Integer> tokenizeFile(File f) {
        // extract and count the tokens from the file `f`, removing stopwords
        // the map of tokens and counts is returned for accumulation and to write the
        // output file
        try {
            // grab all visible text from the HTML file
            String documentText = Jsoup.parse(f).body().text();

            String[] tokens = documentText.split("\s+");
            List<String> tokensList = Arrays.asList(tokens);

            // remove special characters and downcase the tokens
            // throw out stopwords and any empty tokens after processing
            // ref:
            // https://stackoverflow.com/questions/14260134/elegant-way-of-counting-occurrences-in-a-java-collection
            return tokensList.stream()
                    .map(String::toLowerCase)
                    .map(t -> t.replaceAll("[!()_,.?]", ""))
                    .filter(t -> t.length() != 0)
                    .filter(t -> !STOPLIST.contains(t))
                    .collect(Collectors.groupingBy(t -> t, Collectors.reducing(0, e -> 1, Integer::sum)));

        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private static void writeTokens(File tokenFile, Map<String, Integer> tokens) {
        // write `tokens` into `tokenFile` in csv format: token, count

        try (FileWriter fileWriter = new FileWriter(tokenFile);
                BufferedWriter writer = new BufferedWriter(fileWriter)) {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            for (Entry<String, Integer> entry : tokens.entrySet()) {
                printer.printRecord(entry.getKey(), entry.getValue());
            }
            printer.close();
            // System.out.println("wrote file " + tokenFile);
        } catch (IOException e) {
            e.printStackTrace();
            // System.out.println("IOError: did not write file correctly");
        }

    }

    public static void tokenize() {
        indexDirectory(INPUT_DIR, OUTPUT_DIR);
    }

    public static void tokenize(String inputDirPath, String outputDirPath) {
        indexDirectory(inputDirPath, outputDirPath);
    }
}
