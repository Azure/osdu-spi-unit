package org.opengroup.osdu.unitservice.index.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by ZMai on 6/17/2016.
 */
public class WildcardTest {

    @Test
    public void isWildcardTest() {
        assertTrue(Wildcard.isWildcard('*'));
        assertFalse(Wildcard.isWildcard('?')); // We don't support ? as wildcard
        assertFalse(Wildcard.isWildcard('a'));
    }

    @Test
    public void toStringTest() {
        assertEquals("*", new Wildcard().toString());
    }
}
