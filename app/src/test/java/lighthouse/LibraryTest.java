package lighthouse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

class LibraryTest {
    @Test
    void testJSoup() {
        String html = "<html><head><title>First parse</title></head>"
                + "<body><p>Parsed HTML into a doc.</p></body></html>";
        Document doc = Jsoup.parse(html);
    }

    @Test
    void testCSVParser() {
        try {
            StringReader stringReader = new StringReader("CSV, Parser\nRecord2, 2");
            CSVParser parser = new CSVParser(stringReader, CSVFormat.DEFAULT);
            parser.getRecords();
            long parsedRecords = parser.getRecordNumber();
            parser.close();
        } catch (IOException e) {
            assertTrue(false, "CSV Parser encountered IO error");
        }
    }
}
