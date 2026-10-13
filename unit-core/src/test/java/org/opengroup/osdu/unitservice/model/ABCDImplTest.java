package org.opengroup.osdu.unitservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ABCDImplTest {

    @Test
    public void constructor() {
        ABCDImpl abcd = new ABCDImpl(1.0, 2.0, 3.0, 4.0);
        assertEquals(1.0, abcd.getA());
        assertEquals(2.0, abcd.getB());
        assertEquals(3.0, abcd.getC());
        assertEquals(4.0, abcd.getD());

        assertTrue(abcd.isValid());
    }

    @Test
    public void isValid() {
        ABCDImpl abcd = new ABCDImpl(1.0, 2.0, 3.0, 4.0);
        assertTrue(abcd.isValid());

        abcd = new ABCDImpl(Double.NaN, 2.0, 3.0, 4.0);
        assertFalse(abcd.isValid());

        abcd = new ABCDImpl(1.0, Double.NaN, 3.0, 4.0);
        assertFalse(abcd.isValid());

        abcd = new ABCDImpl(1.0, 2.0, Double.NaN, 4.0);
        assertFalse(abcd.isValid());

        abcd = new ABCDImpl(1.0, 2.0, 3.0, Double.NaN);
        assertFalse(abcd.isValid());

        abcd = new ABCDImpl(1.0, 0, 3.0, 4.0);
        assertFalse(abcd.isValid());

        abcd = new ABCDImpl(1.0, 2.0, 0, 0);
        assertFalse(abcd.isValid());
    }

    @Test
    public void hashCodeTest() {
        ABCDImpl abcd1 = new ABCDImpl(1.0, 2.0, 3.0, 4.0);
        ABCDImpl abcd2 = new ABCDImpl(1.0, 2.0, 3.0, 4.0);
        assertEquals(abcd1.hashCode(), abcd2.hashCode());

        ABCDImpl abcd3 = new ABCDImpl(2.0, 2.0, 3.0, 4.0);
        assertFalse(abcd1.hashCode()== abcd3.hashCode());
    }

    @Test
    public void equalsTest() {
        ABCDImpl abcd1 = new ABCDImpl(1.0, 2.0, 3.0, 4.0);
        ABCDImpl abcd2 = new ABCDImpl(1.0, 2.0, 3.0, 4.0);
        assertTrue(abcd1.equals(abcd2));

        ABCDImpl abcd3 = new ABCDImpl(2.0, 2.0, 3.0, 4.0);
        assertFalse(abcd1.equals(abcd3));

        assertFalse(abcd1.equals(null));
        assertFalse(abcd1.equals(new Object()));
    }
}
