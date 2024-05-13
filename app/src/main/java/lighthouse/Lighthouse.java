package lighthouse;

import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * Lighthouse.java is the entry point to the information retrieval engine.
 * Provides the user with console output to query the corpus
 */
public class Lighthouse {
    /** Format for table rows */
    public static final String TABLE_ROW_FORMAT = "| %-9s | %-8.3f |%n";

    /**
     * Prints a table of document weights based off of each entry in result
     * @param result A list of <key, value> pairs representing tokens and weights for those tokens
     */
    private static void printTable(List<Entry<String, Double>> result) {
        // table header
        System.out.format("+-----------+----------+%n");
        System.out.format("| Document  | Weight   |%n");
        System.out.format("+-----------+----------+%n");

        result.stream().forEach(entry -> System.out.format(TABLE_ROW_FORMAT, entry.getKey(), entry.getValue()));

        System.out.format("+-----------+----------+%n");
    }

    /**
     * The main method for the lighthouse information retrieval engine, used for querying the corpus
     * @param args passed command line arguments. No arguments expected.
     */
    public static void main(String[] args) {
        // tokenize the corpus
        Tokenizer.tokenize();

        QueryEngine engine = new QueryEngine("tokens");

        Scanner queryScanner = new Scanner(System.in);
        String query;

        // main loop; prompt user and return relevant documents
        // expecting string to query, or q to quit. Will reprompt upon invalid response.
        do {
            System.out.print("Enter query (q to quit): ");
            query = queryScanner.nextLine();
            if (query.isBlank()) {
                System.out.println("Enter terms to search");
            } else if (query.equals("q")) {
                System.out.println("Goodbye!");
            } else {
                printTable(engine.query(query));
            }
        } while (!query.equals("q"));
        queryScanner.close();
    }
}
