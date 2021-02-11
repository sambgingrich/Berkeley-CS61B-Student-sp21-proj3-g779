package flik;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class lab4Tests {
    @Test
    public void testFlik(){
        assertTrue("These numbers are the same", Flik.isSameNumber(2, 2));
        assertFalse("These numbers are not the same", Flik.isSameNumber(2, 3));
    }

    @Test
    public void test128(){
        assertTrue("These numbers are the same", Flik.isSameNumber(128, 128));
    }
}
