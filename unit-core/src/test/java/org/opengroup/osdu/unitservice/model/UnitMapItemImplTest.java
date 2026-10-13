package org.opengroup.osdu.unitservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UnitMapItemImplTest {
    @Test
    public void emptyConstructor() {
        UnitMapItemImpl unitMapItem = new UnitMapItemImpl();
        assertNull(unitMapItem.getFromUnit());
        assertNull(unitMapItem.getToUnit());
        assertNull(unitMapItem.getState());
        assertNull(unitMapItem.getNote());
    }

    @Test
    public void constructor() {
        UnitImpl fromUnit = new UnitImpl();
        UnitImpl toUnit = new UnitImpl();
        String state = "identical";
        String note = "Indicates that the mapsTo conversion factor is identical to the mapsFrom conversion factor";
        UnitMapItemImpl unitMapItem = new UnitMapItemImpl(fromUnit, toUnit, state, note);
        assertEquals(fromUnit, unitMapItem.getFromUnit());
        assertEquals(toUnit, unitMapItem.getToUnit());
        assertEquals(state, unitMapItem.getState());
        assertEquals(note, unitMapItem.getNote());
    }
}
