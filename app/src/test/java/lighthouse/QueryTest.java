package lighthouse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class QueryTest {

    static QueryEngine engine;

    @BeforeAll
    static void createQueryEngine() {
        engine = new QueryEngine("..\\tokens");
    }

    @Test
    void orderingTest() {
        var result = engine.query("test");
        for (int i = 0; i < result.size() - 1; i++) {
            assert result.get(i).getValue() >= result.get(i + 1).getValue();
        }
    }

    @Test
    void queryTest() {
        var result = engine.query("spaghetti");
        assertNotEquals(0, result.size());

        var testResult = result.stream().filter(r -> r.getKey().equals("038.csv")).findAny();
        assertEquals(true, testResult.isPresent());
    }
}
