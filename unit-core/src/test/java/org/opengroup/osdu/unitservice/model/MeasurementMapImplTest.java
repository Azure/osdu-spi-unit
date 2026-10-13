package org.opengroup.osdu.unitservice.model;

import org.opengroup.osdu.unitservice.interfaces.MeasurementMapItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MeasurementMapImplTest {
    String fromNamespace = "SLB";
    String toNamespace = "Energistics";
    MeasurementMapImpl measurementMap;
    MeasurementImpl fromMeasurement;
    MeasurementImpl toMeasurement;
    MeasurementMapItemImpl measurementMapItem;

    @BeforeEach
    public void setup() {
        measurementMap = new MeasurementMapImpl(fromNamespace, toNamespace);
        fromMeasurement = new MeasurementImpl();
        fromMeasurement.setIsBaseMeasurement(false);
        fromMeasurement.setNamespace(fromNamespace);
        toMeasurement = new MeasurementImpl();
        toMeasurement.setIsBaseMeasurement(false);
        toMeasurement.setNamespace(toNamespace);
        measurementMapItem = new MeasurementMapItemImpl(fromMeasurement, toMeasurement, "identical", "note");
        measurementMap.addMeasurementMapItem(measurementMapItem);
    }

    @Test
    public void emptyConstructor() {
        MeasurementMapImpl measurementMap = new MeasurementMapImpl();
        assertNull(measurementMap.getFromNamespace());
        assertNull(measurementMap.getToNamespace());
        assertNotNull(measurementMap.getMeasurementMapItems());
        assertEquals(0, measurementMap.getMeasurementMapItemCount());
        assertEquals(measurementMap.getMeasurementMapItemCount(), measurementMap.getMeasurementMapItems().size());
    }

    @Test
    public void constructor() {
        MeasurementMapImpl measurementMap = new MeasurementMapImpl(fromNamespace, toNamespace);
        assertEquals(fromNamespace, measurementMap.getFromNamespace());
        assertEquals(toNamespace, measurementMap.getToNamespace());
        assertNotNull(measurementMap.getMeasurementMapItems());
        assertEquals(0, measurementMap.getMeasurementMapItemCount());
        assertEquals(measurementMap.getMeasurementMapItemCount(), measurementMap.getMeasurementMapItems().size());
    }

    @Test
    public void addMeasurementMapItem() {
        assertEquals(1, measurementMap.getMeasurementMapItemCount());
        assertEquals(measurementMapItem, measurementMap.getMeasurementMapItems().get(0));

        MeasurementMapItemImpl measurementMapItem2 = new MeasurementMapItemImpl(fromMeasurement, toMeasurement, "identical", "note");
        measurementMap.addMeasurementMapItem(measurementMapItem2);
        measurementMap.addMeasurementMapItem(measurementMapItem2);  // duplicate item won't be added
        measurementMap.addMeasurementMapItem(null);                 // null won't be added
        assertEquals(2, measurementMap.getMeasurementMapItemCount());

        List<MeasurementMapItem> items = measurementMap.getMeasurementMapItems();
        assertTrue(items.contains(measurementMapItem));
        assertTrue(items.contains(measurementMapItem2));
    }

    @Test
    public void addUnitMapItemWithInvalidItem() {
        MeasurementMapItemImpl invalidMeasurementMapItem;
        MeasurementImpl incompleteMeasurement = new MeasurementImpl();
        incompleteMeasurement.setIsBaseMeasurement(false);

        invalidMeasurementMapItem = new MeasurementMapItemImpl(null, toMeasurement, "identical", "note");
        try {
            measurementMap.addMeasurementMapItem(invalidMeasurementMapItem);
            fail("unexpected result from addMeasurementMapItem() with null fromMeasurement.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidMeasurementMapItem = new MeasurementMapItemImpl(fromMeasurement, null, "identical", "note");
        try {
            measurementMap.addMeasurementMapItem(invalidMeasurementMapItem);
            fail("unexpected result from addMeasurementMapItem() with null toMeasurement.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidMeasurementMapItem = new MeasurementMapItemImpl(fromMeasurement, toMeasurement, null, "note");
        try {
            measurementMap.addMeasurementMapItem(invalidMeasurementMapItem);
            fail("unexpected result from addMeasurementMapItem() with null state.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidMeasurementMapItem = new MeasurementMapItemImpl(incompleteMeasurement, toMeasurement, "identical", "note");
        try {
            measurementMap.addMeasurementMapItem(invalidMeasurementMapItem);
            fail("unexpected result from addMeasurementMapItem() with invalid fromMeasurement.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidMeasurementMapItem = new MeasurementMapItemImpl(fromMeasurement, incompleteMeasurement, "identical", "note");
        try {
            measurementMap.addMeasurementMapItem(invalidMeasurementMapItem);
            fail("unexpected result from addMeasurementMapItem() with invalid toMeasurement.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }

        invalidMeasurementMapItem = new MeasurementMapItemImpl(toMeasurement, fromMeasurement, "identical", "note");
        try {
            measurementMap.addMeasurementMapItem(invalidMeasurementMapItem);
            fail("unexpected result from addMeasurementMapItem() with unmatched namespaces on the measurements.");
        }
        catch(IllegalArgumentException ex) {
            //Ignored as it is expected
        }
    }

    @Test
    public void getMeasurementMapItem() {
        MeasurementMapItemImpl measurementMapItem2 = measurementMap.getMeasurementMapItem(fromMeasurement, toMeasurement);
        assertEquals(measurementMapItem, measurementMapItem2);

        try {
            measurementMap.getMeasurementMapItem(toMeasurement, fromMeasurement);
            fail("unexpected result from getMeasurementMapItem()");
        }
        catch(IllegalArgumentException ex) {
            //ignore as it is expected
        }
    }

    @Test
    public void isMatch() {
        MeasurementImpl nullMeasurement = null;
        MeasurementImpl incompleteMeasurement = new MeasurementImpl();
        incompleteMeasurement.setIsBaseMeasurement(false);
        assertFalse(measurementMap.isMatch(nullMeasurement, toMeasurement));
        assertFalse(measurementMap.isMatch(incompleteMeasurement, toMeasurement));
        assertFalse(measurementMap.isMatch(fromMeasurement, nullMeasurement));
        assertFalse(measurementMap.isMatch(fromMeasurement, incompleteMeasurement));

        assertTrue(measurementMap.isMatch(fromMeasurement, toMeasurement));
        assertTrue(measurementMap.isMatch(toMeasurement, fromMeasurement));
    }
}
