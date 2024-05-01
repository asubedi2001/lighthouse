package lighthouse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;

class TokenizerTest {
    @Test
    void testLoadStoplist() throws IOException {
        Set<String> stoplist = Tokenizer.loadStoplist(Tokenizer.STOPLIST_PATH);
        assertEquals(575, stoplist.size());
    }

    @Test
    void testTokenize() throws IOException {
        Tokenizer.tokenize();
        // each input file should generate one output file
        File inputDir = new File(Tokenizer.INPUT_DIR);
        File outputDir = new File(Tokenizer.OUTPUT_DIR);
        int infileCount = inputDir.list().length;
        int outfileCount = outputDir.list().length;
        assertEquals(infileCount, outfileCount);
    }
}
