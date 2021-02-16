package deque;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {
    private Comparator ac;

    @Test
    public void alphabeticalTest() {
        ac = new AlphabeticalComparator();

        MaxArrayDeque<String> ad1 = new MaxArrayDeque<String>(ac);
        ad1.addLast("c");
        ad1.addLast("b");
        ad1.addLast("a");
        ad1.addLast("d");

        String shouldBeA = ad1.max();
        assertEquals(shouldBeA, "a");
    }
}
