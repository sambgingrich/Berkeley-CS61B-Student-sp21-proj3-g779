package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    /** Adds the same value to both the correct and buggy AList implementations,
     * then checks that the results of three subsequent removeLast calls are the same */
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> goodlist = new AListNoResizing<>();
        BuggyAList<Integer> buggylist = new BuggyAList<>();
        for (int i = 4; i < 7; i += 1) {
            goodlist.addLast(i);
            buggylist.addLast(i);
        }

        int buggy1 = buggylist.removeLast();
        int good1 = goodlist.removeLast();
        String errmsg = "The removed items should be equal \n.";
        assertEquals(errmsg, good1, buggy1);

        int buggy2 = buggylist.removeLast();
        int good2 = goodlist.removeLast();
        assertEquals(errmsg, good2, buggy2);

        int buggy3 = buggylist.removeLast();
        int good3 = goodlist.removeLast();
        assertEquals(errmsg, good3, buggy3);
    }

    @Test
    /** Randomly tests the AList. */
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int correct = L.size();
                int actual = B.size();
                assertEquals(correct, actual);
            } else if (operationNumber == 2) {
                // getLast
                if (L.size() > 0) {
                    int correct = L.getLast();
                    int actual = B.getLast();
                    assertEquals(correct, actual);
                }
            } else if (operationNumber == 3) {
                // removeLast
                if (L.size() > 0) {
                    int correct = L.removeLast();
                    int actual = B.removeLast();
                    assertEquals(correct, actual);
                }
            }
        }
    }
}
