package lighthouse;

import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

public class Lighthouse {
    public static final String TABLE_ROW_FORMAT = "| %-9s | %-8.3f |%n";

    private static void printTable(List<Entry<String, Double>> result) {
        // table header
        System.out.format("+-----------+----------+%n");
        System.out.format("| Document  | Weight   |%n");
        System.out.format("+-----------+----------+%n");

        result.stream().forEach(entry -> System.out.format(TABLE_ROW_FORMAT, entry.getKey(), entry.getValue()));

        System.out.format("+----------+-----------+%n");
    }

    public static void main(String[] args) {
        // tokenize the corpus
        Tokenizer.tokenize();

        QueryEngine engine = new QueryEngine("tokens");

        Scanner queryScanner = new Scanner(System.in);
        String query;

        // main loop; prompt user and return relevant documents
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
