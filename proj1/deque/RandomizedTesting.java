package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RandomizedTesting {
    @Test
    public void randomizedTestad() {
        AListNoResizing<Integer> lld = new AListNoResizing<>();
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        int N = 999;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                lld.addLast(randVal);
                ad.addLast(randVal);
            } else if (operationNumber <= 2 || operationNumber == 4) {
                // size
                int correct = lld.size();
                int actual = ad.size();
                assertEquals(correct, actual);
            } else if (operationNumber == 3) {
                // removeLast
                if (lld.size() > 0) {
                    int correct = lld.removeLast();
                    int actual = ad.removeLast();
                    assertEquals(correct, actual);
                }
            } else if (operationNumber == 5) {
                // get
                if (lld.size() > 0) {
                    int randVal = StdRandom.uniform(0, lld.size());
                    int correct = lld.get(randVal);
                    int actual = ad.get(randVal);
                    assertEquals(correct, actual);
                }
            }
        }
    }

    @Test
    public void randomizedTestlld() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<Integer>();
        AListNoResizing<Integer> ad = new AListNoResizing<>();

        int N = 1000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                lld.addLast(randVal);
                ad.addLast(randVal);
            } else if (operationNumber <= 2 || operationNumber == 4) {
                // size
                int correct = lld.size();
                int actual = ad.size();
                assertEquals(correct, actual);
            } else if (operationNumber == 3) {
                // removeLast
                if (lld.size() > 0) {
                    int correct = lld.removeLast();
                    int actual = ad.removeLast();
                    assertEquals(correct, actual);
                }
            } else if (operationNumber == 5) {
                // get
                if (lld.size() > 0) {
                    int randVal = StdRandom.uniform(0, lld.size());
                    int correct = lld.get(randVal);
                    int actual = ad.get(randVal);
                    assertEquals(correct, actual);
                }
            }
        }
    }

    @Test
    public void randomizedTestBoth() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<Integer>();
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        int N = 10000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 8);
            if (operationNumber <= 1) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                lld.addLast(randVal);
                ad.addLast(randVal);
                assertTrue(lld.size() == ad.size());
            } else if (operationNumber == 2 || operationNumber == 7) {
                // removeFirst
                if (lld.size() > 0) {
                    int correct = lld.removeFirst();
                    int actual = ad.removeFirst();
                    assertEquals(correct, actual);
                    assertTrue(lld.size() == ad.size());
                }
            } else if (operationNumber == 3 || operationNumber == 6) {
                // removeLast
                if (lld.size() > 0) {
                    int correct = lld.removeLast();
                    int actual = ad.removeLast();
                    assertEquals(correct, actual);
                    assertTrue(lld.size() == ad.size());
                }
            } else if (operationNumber == 4) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                lld.addFirst(randVal);
                ad.addFirst(randVal);
                assertTrue(lld.size() == ad.size());
            } else if (operationNumber == 5) {
                // get
                if (lld.size() > 0) {
                    int randVal = StdRandom.uniform(0, lld.size());
                    int correct = lld.get(randVal);
                    int actual = ad.get(randVal);
                    assertEquals(correct, actual);
                }
            }
        }
    }
}
