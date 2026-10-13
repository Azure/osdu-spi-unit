package org.opengroup.osdu.unitservice.model.extended;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitSystemEssenceImplTest {

    @Test
    public void constructor() {
        UnitSystemEssenceImpl essence = new UnitSystemEssenceImpl("");
        assertEquals("{\"ancestry\":\"\"}", essence.toJsonString());
        essence = new UnitSystemEssenceImpl("abc");
        assertEquals("{\"ancestry\":\"abc\"}", essence.toJsonString());
    }

    @Test
    public void equalsTest() {
        String ancestry = "abc";
        UnitSystemEssenceImpl essence1 = new UnitSystemEssenceImpl(ancestry);
        assertFalse(essence1.equals(null));
        assertFalse(essence1.equals(new Object()));

        UnitSystemEssenceImpl essence2 = new UnitSystemEssenceImpl("123");
        assertFalse(essence1.equals(essence2));

        UnitSystemEssenceImpl essence3 = new UnitSystemEssenceImpl(ancestry);
        assertTrue(essence1.equals(essence3));
    }
}
