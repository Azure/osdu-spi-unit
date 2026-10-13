package org.opengroup.osdu.unitservice.model;

import org.opengroup.osdu.unitservice.helper.Utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitEssenceImplTest {

    @Test
    public void emptyConstructor() {
        UnitEssenceImpl essence = new UnitEssenceImpl();
        assertNull(essence.getScaleOffset());
        assertNull(essence.getABCD());
        assertNull(essence.getSymbol());
        assertNull(essence.getBaseMeasurement());
        assertEquals("{}", essence.toJsonString());
    }

    @Test
    public void symbolAccessor() {
        UnitEssenceImpl essence = new UnitEssenceImpl();
        String symbol = "ft";
        essence.setSymbol(symbol);
        assertEquals(symbol, essence.getSymbol());
    }

    @Test
    public void scaleOffsetAccessor() {
        UnitEssenceImpl essence = new UnitEssenceImpl();
        ScaleOffsetImpl scaleOffset = new ScaleOffsetImpl(1.0, 0);
        essence.setScaleOffset(scaleOffset);
        assertEquals(scaleOffset, essence.getScaleOffset());
    }

    @Test
    public void abcdAccessor() {
        UnitEssenceImpl essence = new UnitEssenceImpl();
        ABCDImpl abcd = new ABCDImpl(0, 1.0, 1.0, 0);
        essence.setABCD(abcd);
        assertEquals(abcd, essence.getABCD());
    }

    @Test
    public void baseMeasurementAccessor() {
        String baseMeasurementJsonString = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        MeasurementImpl measurement = null;
        try {
            MeasurementEssenceImpl measurementEssence = Utility.fromJsonString(baseMeasurementJsonString, MeasurementEssenceImpl.class);
            measurement = new MeasurementImpl();
            measurement.setEssence(measurementEssence);
        }
        catch(Exception ex) {
            fail(ex.getMessage());
        }
        UnitEssenceImpl essence = new UnitEssenceImpl();
        essence.setBaseMeasurement(measurement);
        assertEquals(measurement, essence.getBaseMeasurement());
        assertEquals(baseMeasurementJsonString, essence.getBaseMeasurement().getEssence().toJsonString());
    }

    @Test
    public void equalsTest() {
        UnitEssenceImpl seed = null;
        try {
            String essenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"FT\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"}}";
            seed = Utility.fromJsonString(essenceJson, UnitEssenceImpl.class);
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }

        UnitEssenceImpl essence1 = new UnitEssenceImpl();
        assertFalse(essence1.equals(null));
        assertFalse(essence1.equals(new Object()));

        UnitEssenceImpl essence2 = new UnitEssenceImpl();
        assertTrue(essence1.equals(essence2));

        // Symbol
        essence1.setSymbol(seed.getSymbol());
        assertFalse(essence1.equals(essence2));
        essence2.setSymbol(seed.getSymbol());
        assertTrue(essence1.equals(essence2));

        // ScaleOffset
        essence1.setScaleOffset(seed.getScaleOffset());
        assertFalse(essence1.equals(essence2));
        essence2.setScaleOffset(seed.getScaleOffset());
        assertTrue(essence1.equals(essence2));

        // ABCD
        ABCDImpl abcd = new ABCDImpl(0,1,1,0);
        essence1.setABCD(abcd);
        assertFalse(essence1.equals(essence2));
        essence2.setABCD(abcd);
        assertTrue(essence1.equals(essence2));
    }

    @Test
    public void hashCodeTest(){
        String u1 = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"LM\",\"baseMeasurement\":{\"ancestry\":\"Luminous_Flux\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String u2 = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"M.\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl ue1 = Utility.fromJsonString(u1, UnitEssenceImpl.class);
        UnitEssenceImpl ue2 = Utility.fromJsonString(u2, UnitEssenceImpl.class);
        int h1 = ue1.hashCode();
        int h2 = ue2.hashCode();
        int m1 = ue1.getBaseMeasurementEssence().hashCode();
        int m2 = ue2.getBaseMeasurementEssence().hashCode();
        assertFalse(h1 == h2);
        assertFalse(m1 == m2);
    }
}
