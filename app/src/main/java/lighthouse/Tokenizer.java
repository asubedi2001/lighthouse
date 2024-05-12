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
/**
 * The Tokenizer class reads in all files contained in the corpus' designated file location
 * and tokenizes the contents, along with calculating counts for each of the tokens
 */
public class Tokenizer {
    /** default filepath location of corpus input */
    public static final String INPUT_DIR = "corpus";
    /** default filepath location of output from tokenization */
    public static final String OUTPUT_DIR = "tokens";
    /** default filepath location of tokenization stoplist */
    public static final String STOPLIST_PATH = "stoplist.txt";
    /** set of all strings inside of stoplist */
    protected static final Set<String> STOPLIST = loadStoplist(STOPLIST_PATH);

    /**
     * Private default constructor for creation of a Tokenizer object
     */
    private Tokenizer() {
    }

    /**
     * Loads the file given by stoplistPath, which contains a list of strings that are not meant to be
     * examined / not have tokens counted for
     * @param stoplistPath filepath to stoplist text file
     * @return Set which contains all words given by the stoplist
     */
    public static Set<String> loadStoplist(String stoplistPath) {
        // load the stoplist identified by stoplistPath
        InputStream stoplistInputStream = ClassLoader.getSystemResourceAsStream(stoplistPath);
        BufferedReader stoplistReader = new BufferedReader(
                new InputStreamReader(stoplistInputStream, StandardCharsets.UTF_8));

        return stoplistReader.lines().collect(Collectors.toSet());
    }

    /**
     * Performs tokenization all files inside of the directory defined by inputDirPath.
     * Will output resulting token files to files inside of directory defined by outputDirPath.
     * @param inputDirPath filepath to the directory containing the input files
     * @param outputDirPath filepath to the directory containing the output files
     */
    private static void indexDirectory(String inputDirPath, String outputDirPath) {
        /*
         * Perform tokenization across all files in input directory and
         * place resulting token files in the specified output directory
         */
        File inputDir;
        File outputDir;
        inputDir = new File(inputDirPath);
        outputDir = new File(outputDirPath);

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
        System.out.printf("Found %d documents to index in input directory.%n", numFiles);

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

    /**
     * Generatesd the name for an output file given the output directory path and the corresponding input file.
     * @param inputFile The file that was passed as input
     * @param outputDirPath The filepath to the directory containing all output files
     * @return String containing the name of the output token file.
     */
    private static String generateOutfileName(File inputFile, String outputDirPath) {
        // OUTPUT_DIR + basename + ".csv"

        String filename = inputFile.getName();

        // stripping off the .html extension (assuming files conform to standard)
        String basename = filename.substring(0, filename.length() - 5);

        return outputDirPath + "\\" + basename + ".csv";
    }

    /**
     * Tokenizes a single file and calculates counts based off of the frequency in a particular document.
     * @param f File that is going to be tokenized
     * @return Map of Strings and Integers which stores each token and its calculated frequency/count
     */
    public static Map<String, Integer> tokenizeFile(File f) {
        // extract and count the tokens from the file `f`, removing stopwords
        // the map of tokens and counts is returned for accumulation and to write the
        // output file
        try {
            // grab all visible text from the HTML file
            String documentText = Jsoup.parse(f).body().text();

            String[] tokens = documentText.split("\\s+");
            List<String> tokensList = Arrays.asList(tokens);

            // remove special characters and downcase the tokens
            // throw out stopwords and any empty tokens after processing
            // ref:
            // https://stackoverflow.com/questions/14260134/elegant-way-of-counting-occurrences-in-a-java-collection
            return tokensList.stream()
                    .map(String::toLowerCase)
                    .map(t -> t.replaceAll("[!()_,.?':’]", ""))
                    .filter(t -> t.length() != 0)
                    .filter(t -> !STOPLIST.contains(t))
                    .collect(Collectors.groupingBy(t -> t, Collectors.reducing(0, e -> 1, Integer::sum)));

        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * Writes Map<String, Integer> of tokens and their counts into tokenFile in csv format
     * @param tokenFile file where the tokens and counts will be stored
     * @param tokens Map<String, Integer> which stores pairs of tokens and their respective counts
     */
    private static void writeTokens(File tokenFile, Map<String, Integer> tokens) {
        // write `tokens` into `tokenFile` in csv format: token, count

        try (FileWriter fileWriter = new FileWriter(tokenFile);
                BufferedWriter writer = new BufferedWriter(fileWriter)) {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            for (Entry<String, Integer> entry : tokens.entrySet()) {
                printer.printRecord(entry.getKey(), entry.getValue());
            }
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Begins the tokenization process, using constant values INPUT_DIR and OUTPUT_DIR
     * as the input and output directories.
     */
    public static void tokenize() {
        indexDirectory(INPUT_DIR, OUTPUT_DIR);
    }

    /**
     * Begins the tokenization process, using arguments inputDirPath and outputDirPath
     * as the input and output directories.
     * @param inputDirPath filepath for input directory
     * @param outputDirPath filepath for output directory
     */
    public static void tokenize(String inputDirPath, String outputDirPath) {
        indexDirectory(inputDirPath, outputDirPath);
    }
}
