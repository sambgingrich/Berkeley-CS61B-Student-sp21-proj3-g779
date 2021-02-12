package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class ArrayDequeTest {

    private Object LinkedListDeque;

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<String>();

        assertTrue("A newly initialized aDeque should be empty", ad1.isEmpty());
        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ad1.size());
        assertFalse("ad1 should now contain 1 item", ad1.isEmpty());

        ad1.addLast("middle");
        assertEquals(2, ad1.size());

        ad1.addLast("back");
        assertEquals(3, ad1.size());

        System.out.println("Printing out deque: ");
        ad1.printDeque();
    }

    @Test
    /* Check if ArrayDeque is generic for all parameter types. */
    public void multipleParamTest() {
        ArrayDeque<String>  ad1 = new ArrayDeque<String>();
        ArrayDeque<Double>  ad2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> ad3 = new ArrayDeque<Boolean>();

        ad1.addFirst("string");
        ad2.addFirst(3.14159);
        ad3.addFirst(true);

        String s = ad1.removeFirst();
        double d = ad2.removeFirst();
        boolean b = ad3.removeFirst();
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(3);

        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();

        int size = ad1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /** Adds an item, then removes an item, and ensures that da is empty afterwards. */
    public void addRemoveTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("ad1 should be empty upon initialization", ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        assertFalse("ad1 should contain 1 item", ad1.isEmpty());

        ad1.removeFirst();
        // should be empty
        assertTrue("ad1 should be empty after removal", ad1.isEmpty());
    }

    @Test
    /** Adds an item, then removes an item, then adds an item and ensures that da is empty afterwards.  */
    public void addRemoveaddTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("ad1 should be empty upon initialization", ad1.isEmpty());

        for (int i = 0; i < 6; i++){
            ad1.addFirst(i);
        }
        // should not be empty
        assertFalse("ad1 should contain 1 item", ad1.isEmpty());

        for (int i = 5; i >= 0; i--){
            int first = ad1.removeFirst();
            assertEquals(first, i);
        }
        // should be empty
        assertTrue("ad1 should be empty after removal", ad1.isEmpty());

        for (int i = 0; i < 6; i++){
            ad1.addLast(i);
        }
        // should not be empty
        assertFalse("ad1 should contain 1 item", ad1.isEmpty());
        int size = ad1.size();
        assertEquals( 6, size);
        int secondItem = ad1.get(1);
        assertEquals(1, secondItem);
    }

    @Test
    /** Test whether multiple ADs can be made properly*/
    public void multipleADs() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ArrayDeque<Integer> ad2 = new ArrayDeque<Integer>();
        ArrayDeque<Integer> ad3 = new ArrayDeque<Integer>();

        ad1.addFirst(3);
        ad2.addFirst(35);
        ad3.addFirst(6);
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigADequeTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 50; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 25; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 49; i > 25; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    /* Trying to replicate autograder */
    public void AG1Test(){
        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 10; i++) {
            int sizeC = lld1.size();
            assertEquals(i, sizeC);
            lld1.addFirst(i);
        }
    }

}