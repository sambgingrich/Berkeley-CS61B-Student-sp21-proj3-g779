package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);

    }

    @Test
    public void testSquarePrimes2() {
        IntList lst = IntList.of(7, 8, 12, 5, 10);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("49 -> 8 -> 12 -> 25 -> 10", lst.toString());
        assertTrue(changed);

    }

    @Test
    public void testSquarePrimes3() {
        IntList lst = IntList.of(23, 19, 25);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("529 -> 361 -> 25", lst.toString());
        assertTrue(changed);
    }
}
