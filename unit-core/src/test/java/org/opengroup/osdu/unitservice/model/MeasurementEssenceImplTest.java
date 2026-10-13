package org.opengroup.osdu.unitservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasurementEssenceImplTest {

    @Test
    public void emptyConstructor() {
        MeasurementEssenceImpl essence = new MeasurementEssenceImpl();
        assertNull(essence.getAncestry());
        assertEquals("{}", essence.toJsonString());
        assertNull(essence.getBaseMeasurementEssenceJson());
        assertNull(essence.getParentEssenceJson());
    }

    @Test
    public void ancestryAccessor() {
        String baseMeasurementAncestry = "Length";
        String baseMeasurementEssenceJson = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        String childMeasurementAncestry = "Length.Millimeter";
        String childMeasurementEssenceJson = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = new MeasurementEssenceImpl();

        essence.setAncestry(baseMeasurementAncestry);
        essence.setType("UM");
        assertEquals(baseMeasurementAncestry, essence.getAncestry());
        assertEquals(baseMeasurementEssenceJson, essence.toJsonString());
        assertEquals(baseMeasurementEssenceJson, essence.getBaseMeasurementEssenceJson());
        assertNull(essence.getParentEssenceJson());

        essence.setAncestry(childMeasurementAncestry);
        essence.setType("UM");
        assertEquals(childMeasurementAncestry, essence.getAncestry());
        assertEquals(childMeasurementEssenceJson, essence.toJsonString());
        assertEquals(baseMeasurementEssenceJson, essence.getBaseMeasurementEssenceJson());
        assertEquals(baseMeasurementEssenceJson, essence.getParentEssenceJson());
    }

    @Test
    public void equalsTest() {
        String ancestry = "Length.Millimeter";

        MeasurementEssenceImpl essence1 = new MeasurementEssenceImpl();
        assertFalse(essence1.equals(null));
        assertFalse(essence1.equals(new Object()));

        MeasurementEssenceImpl essence2 = new MeasurementEssenceImpl();
        assertTrue(essence1.equals(essence2));

        essence1.setAncestry(ancestry);
        assertFalse(essence1.equals(essence2));

        essence2.setAncestry(ancestry);
        assertTrue(essence1.equals(essence2));
    }
}
