package lighthouse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;

class TokenizerTest {
    @Test
    void testLoadStoplist() throws IOException {
        Set<String> stoplist = Tokenizer.loadStoplist(Tokenizer.STOPLIST_PATH);
        assertEquals(8, stoplist.size());
    }
}
