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
        String inputDirPath = "..\\corpus";
        String outputDirPath = "..\\tokens";
        Tokenizer.tokenize(inputDirPath, outputDirPath);
        // each input file should generate one output file
        File inputDir = new File(inputDirPath);
        File outputDir = new File(outputDirPath);
        int infileCount = inputDir.list().length;
        int outfileCount = outputDir.list().length;
        assertEquals(infileCount, outfileCount);
    }
}
