package org.opengroup.osdu.unitservice.model;

import org.opengroup.osdu.unitservice.interfaces.UnitMapItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnitMapImplTest {
    String fromNamespace = "SLB";
    String toNamespace = "Energistics";
    UnitMapImpl unitMap;
    UnitImpl fromUnit;
    UnitImpl toUnit;
    UnitMapItemImpl unitMapItem;

    @BeforeEach
    public void setup() {
        unitMap = new UnitMapImpl(fromNamespace, toNamespace);
        fromUnit = new UnitImpl();
        fromUnit.setNamespace(fromNamespace);
        toUnit = new UnitImpl();
        toUnit.setNamespace(toNamespace);
        unitMapItem = new UnitMapItemImpl(fromUnit, toUnit, "identical", "note");
        unitMap.addUnitMapItem(unitMapItem);
    }

    @Test
    public void emptyConstructor() {
        UnitMapImpl unitMap = new UnitMapImpl();
        assertNull(unitMap.getFromNamespace());
        assertNull(unitMap.getToNamespace());
        assertNotNull(unitMap.getUnitMapItems());
        assertEquals(0, unitMap.getUnitMapItemCount());
        assertEquals(unitMap.getUnitMapItemCount(), unitMap.getUnitMapItems().size());
    }

    @Test
    public void constructor() {
        UnitMapImpl unitMap = new UnitMapImpl(fromNamespace, toNamespace);
        assertEquals(fromNamespace, unitMap.getFromNamespace());
        assertEquals(toNamespace, unitMap.getToNamespace());
        assertNotNull(unitMap.getUnitMapItems());
        assertEquals(0, unitMap.getUnitMapItemCount());
        assertEquals(unitMap.getUnitMapItemCount(), unitMap.getUnitMapItems().size());
    }

    @Test
    public void addUnitMapItem() {
        assertEquals(1, unitMap.getUnitMapItemCount());
        assertEquals(unitMapItem, unitMap.getUnitMapItems().get(0));

        UnitMapItemImpl unitMapItem2 = new UnitMapItemImpl(fromUnit, toUnit, "identical", "note");
        unitMap.addUnitMapItem(unitMapItem2);
        unitMap.addUnitMapItem(unitMapItem2);    // duplicate item won't be added
        unitMap.addUnitMapItem(null);           // null won't be added
        assertEquals(2, unitMap.getUnitMapItemCount());

        List<UnitMapItem> items = unitMap.getUnitMapItems();
        assertTrue(items.contains(unitMapItem));
        assertTrue(items.contains(unitMapItem2));
    }

    @Test
    public void addUnitMapItemWithInvalidItem() {
        UnitMapItemImpl invalidUnitMapItem;
        UnitImpl incompleteUnit = new UnitImpl();

        invalidUnitMapItem = new UnitMapItemImpl(null, toUnit, "identical", "note");
        try {
            unitMap.addUnitMapItem(invalidUnitMapItem);
            fail("unexpected result from addUnitMapItem() with null fromUnit.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidUnitMapItem = new UnitMapItemImpl(fromUnit, null, "identical", "note");
        try {
            unitMap.addUnitMapItem(invalidUnitMapItem);
            fail("unexpected result from addUnitMapItem() with null toUnit.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidUnitMapItem = new UnitMapItemImpl(fromUnit, toUnit, null, "note");
        try {
            unitMap.addUnitMapItem(invalidUnitMapItem);
            fail("unexpected result from addUnitMapItem() with null state.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidUnitMapItem = new UnitMapItemImpl(incompleteUnit, toUnit, "identical", "note");
        try {
            unitMap.addUnitMapItem(invalidUnitMapItem);
            fail("unexpected result from addUnitMapItem() with invalid fromUnit.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidUnitMapItem = new UnitMapItemImpl(fromUnit, incompleteUnit, "identical", "note");
        try {
            unitMap.addUnitMapItem(invalidUnitMapItem);
            fail("unexpected result from addUnitMapItem() with invalid toUnit.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidUnitMapItem = new UnitMapItemImpl(toUnit, fromUnit, "identical", "note");
        try {
            unitMap.addUnitMapItem(invalidUnitMapItem);
            fail("unexpected result from addUnitMapItem() with unmatched namespaces on the units.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }
    }

    @Test
    public void getUnitMapItem() {
        UnitMapItemImpl unitMapItem2 = unitMap.getUnitMapItem(fromUnit, toUnit);
        assertEquals(unitMapItem, unitMapItem2);

        try {
            unitMap.getUnitMapItem(toUnit, fromUnit);
            fail("unexpected result from getUnitMapItem()");
        }
        catch(IllegalArgumentException ex) {
            //ignore as it is expected
        }
    }

    @Test
    public void isMatch() {
        UnitImpl nullUnit = null;
        UnitImpl incompleteUnit = new UnitImpl();
        assertFalse(unitMap.isMatch(nullUnit, toUnit));
        assertFalse(unitMap.isMatch(incompleteUnit, toUnit));
        assertFalse(unitMap.isMatch(fromUnit, nullUnit));
        assertFalse(unitMap.isMatch(fromUnit, incompleteUnit));

        assertTrue(unitMap.isMatch(fromUnit, toUnit));
        assertTrue(unitMap.isMatch(toUnit, fromUnit));
    }

}
