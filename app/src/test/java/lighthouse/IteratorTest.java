package lighthouse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lighthouse.util.TDMHeader;
import lighthouse.util.TDMNode;

class IteratorTest {

    static TDMHeader header;
    final static int[] termCounts = { 1, 2, 3, 4 };
    static int termsSum;

    @BeforeAll
    static void createTDMHeader() {

        for (final int t : termCounts) {
            termsSum += t;
        }

        // create the TDMHeader
        TDMNode node1 = new TDMNode("0", 0);
        header = new TDMHeader("test", node1);

        for (final int t : termCounts) {
            TDMNode node = new TDMNode("" + t, t);
            header.append(node);
        }
    }

    @Test
    void testIterator() {
        int i = 0;
        int termCounts = 0;
        for (TDMNode node : header) {
            i++;
            termCounts += node.getTermCount();
        }

        // 4 nodes in the array + 1 for the initial TDMNode in TDMHeader ctor
        assertEquals(5, i);
        assertEquals(10, termCounts);
    }

}
