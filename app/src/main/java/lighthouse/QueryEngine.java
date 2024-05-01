package lighthouse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import lighthouse.util.TDMHeader;
import lighthouse.util.TDMNode;

public class QueryEngine {

    public static final double BM25_K = 1.5;
    public static final double BM25_B = .75;

    private static final double TICKMARK_PCT = .1;

    private Map<String, TDMHeader> termMatrix;
    private Map<String, Integer> documentLengths;

    private int totalDocumentLengths;
    private int numDocuments;
    private double avgDocLength;

    public QueryEngine(String tokenDir) {
        loadTDM(tokenDir);
        calculateWeights(tokenDir);
    }

    private void loadTDM(String tokenDirPath) {
        // initializes all instance variables
        // performs all necessary setup to calculate weights

        System.out.println("(1/2) Building index...");

        termMatrix = new HashMap<>();
        documentLengths = new HashMap<>();

        int documentLength;

        File tokenDir = new File(tokenDirPath);
        File[] tokenFiles = tokenDir.listFiles();
        int fileCount = tokenFiles.length;
        long progressPct = Math.max(Math.round(fileCount * TICKMARK_PCT), 1);

        for (File tokenFile : tokenFiles) {
            documentLength = 0;

            if (numDocuments % progressPct == 0) {
                printProgressBar(numDocuments, fileCount);
            }

            try (CSVParser tokenParser = CSVParser.parse(tokenFile, StandardCharsets.UTF_8, CSVFormat.DEFAULT)) {
                for (CSVRecord tokenRecord : tokenParser) {
                    String token = tokenRecord.get(0);
                    int count = Integer.parseInt(tokenRecord.get(1));

                    TDMNode newNode = new TDMNode(tokenFile.getName(), count);
                    if (!termMatrix.containsKey(token)) {
                        // if the term is not in the term document matrix yet, include it
                        termMatrix.put(token, new TDMHeader(token, newNode));
                    } else {
                        // if the term has been added, append the new TDMNode
                        termMatrix.get(token).append(newNode);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            numDocuments++;

            totalDocumentLengths += documentLength;
            documentLengths.put(tokenFile.getName(), documentLength);
        }
        System.out.println();

        // setting values to be used in BM25 weight formula
        avgDocLength = (double) totalDocumentLengths / numDocuments;

    }

    private void calculateWeights(String tokenDirPath) {
        System.out.println("(2/2) Calculating weights...");

        int documentCounter = 0;

        File tokenDir = new File(tokenDirPath);
        File[] tokenFiles = tokenDir.listFiles();
        int fileCount = tokenFiles.length;
        long progressPct = Math.max(Math.round(fileCount * TICKMARK_PCT), 1);

        for (File tokenFile : tokenFiles) {
            if (documentCounter % progressPct == 0) {
                printProgressBar(documentCounter, fileCount);
            }
            try (CSVParser tokenParser = CSVParser.parse(tokenFile, StandardCharsets.UTF_8, CSVFormat.DEFAULT)) {
                for (CSVRecord tokenRecord : tokenParser) {
                    String token = tokenRecord.get(0);
                    int frequency = Integer.parseInt(tokenRecord.get(1));

                    TDMHeader posting = termMatrix.get(token);

                    int nqi = posting.getLen();
                    double termWeight = BM25(frequency, nqi, numDocuments, documentLengths.get(tokenFile.getName()),
                            avgDocLength);

                    for (TDMNode node : posting) {
                        if (node.getDocumentID().equals(tokenFile.getName())) {
                            node.setTermWeight(termWeight);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            documentCounter++;
        }
        System.out.println();
    }

    public List<Entry<String, Double>> query(String query) {
        // return the most relevant documents for a given query

        // simplest case
        if (query.isBlank()) {
            System.out.println("Querying: (empty)");
            return new ArrayList<>();
        }

        Map<String, Double> documentWeights = new HashMap<>();

        // preprocess the query
        List<String> queryTerms = normalizeQuery(query);

        System.out.println("Querying: " + String.join(" ", queryTerms));

        for (String term : queryTerms) {
            TDMHeader posting = termMatrix.get(term);

            // if the term exists, sum all of the weights for each document
            if (posting != null) {
                for (TDMNode node : posting) {
                    documentWeights.put(node.getDocumentID(),
                            documentWeights.getOrDefault(node.getDocumentID(), 0d) + node.getTermWeight());
                }
            }
        }

        // now turn the map into a list of document/weight pairs and sort
        return documentWeights.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
    }

    private List<String> normalizeQuery(String query) {
        String[] queryTerms = query.split("\s+");
        return Arrays.asList(queryTerms).stream()
                .map(String::toLowerCase)
                .map(t -> t.replaceAll("[!()_,.?]", ""))
                .filter(t -> t.length() != 0)
                .collect(Collectors.toList());
    }

    private void printProgressBar(int counter, int totalDocuments) {
        // loaded_pct = round((counter / file_count) * 10)
        // to_load_pct = 10 - loaded_pct
        // progress_bar = "\r[" + "█" * loaded_pct + " " * to_load_pct + "] "
        // progress_pct = "0%" if loaded_pct == 0 else f"{loaded_pct}0%"
        // print(progress_bar + progress_pct, end="")

        long loadedPct = Math.round(((double) counter / totalDocuments) * 10);
        long toLoadPct = 10 - loadedPct;

        String progressBar = "\r[";
        progressBar += String.join("", Collections.nCopies((int) loadedPct, "█"));
        progressBar += String.join("", Collections.nCopies((int) toLoadPct, " "));
        progressBar += "] ";

        String progressPct;
        if (loadedPct == 0) {
            progressPct = "0%";
        } else {
            progressPct = loadedPct + "0%";
        }

        System.out.print(progressBar + progressPct);
    }

    public static double BM25(int fqi, int nqi, int numDocuments, int documentLength, double avgDocLength) {
        double k1 = BM25_K;
        double b = BM25_B;
        return IDF(numDocuments, nqi) * ((fqi * (k1 + 1)) / (fqi + k1 * (1 - b + b * documentLength / avgDocLength)));
    }

    public static double IDF(int numDocuments, int nqi) {
        return Math.log(1 + (numDocuments - nqi + .5) / (nqi + .5));
    }
}
