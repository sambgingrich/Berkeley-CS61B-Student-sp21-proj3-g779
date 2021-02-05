package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class randomizedTest {
    @Test
    public void randomizedTest() {
        LinkedListDeque<Integer> LLD = new LinkedListDeque<Integer>();
        ArrayDeque<Integer> AD = new ArrayDeque<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                LLD.addLast(randVal);
                AD.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int correct = LLD.size();
                int actual = AD.size();
                assertEquals(correct, actual);
            } else if (operationNumber == 2) {
                // removeFirst
                if (LLD.size() > 0) {
                    int correct = LLD.removeFirst();
                    int actual = AD.removeFirst();
                    assertEquals(correct, actual);
                }
            } else if (operationNumber == 3) {
                // removeLast
                if (LLD.size() > 0) {
                    int correct = LLD.removeLast();
                    int actual = AD.removeLast();
                    assertEquals(correct, actual);
                }
            } else if (operationNumber == 4) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                LLD.addFirst(randVal);
                AD.addFirst(randVal);
            } else if (operationNumber == 5) {
                // get
                if (LLD.size() > 0) {
                    int randVal = StdRandom.uniform(0, LLD.size());
                    int correct = LLD.get(randVal);
                    int actual = AD.get(randVal);
                    assertEquals(correct, actual);
                }
            }
        }
    }
}
