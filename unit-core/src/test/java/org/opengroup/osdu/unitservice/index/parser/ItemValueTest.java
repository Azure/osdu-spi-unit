package org.opengroup.osdu.unitservice.index.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by ZMai on 7/14/2016.
 */
public class ItemValueTest {
    private String value = "A10";

    @Test
    public void constructor1() {
        ItemValue itemValue = new ItemValue(value);
        assertEquals(value, itemValue.getValue());
        assertFalse(itemValue.isQuoted());
        assertEquals(value, itemValue.toString());
    }

    @Test
    public void constructor2() {
        ItemValue itemValue = new ItemValue(value, true);
        assertEquals(value, itemValue.getValue());
        assertTrue(itemValue.isQuoted());
        assertEquals("\"" + value + "\"", itemValue.toString());
    }
}
