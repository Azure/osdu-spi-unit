package org.opengroup.osdu.unitservice.model;

import org.opengroup.osdu.unitservice.helper.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeasurementImplTest {
    private MeasurementImpl measurement;

    @BeforeEach
    public void setup() {
        measurement = new MeasurementImpl();
    }

    @Test
    public void codeAccessor() {
        String code = "Length";
        assertNull(measurement.getCode());
        measurement.setCode(code);
        assertEquals(code, measurement.getCode());
    }

    @Test
    public void nameAccessor() {
        String name = "Length";
        assertNull(measurement.getName());
        measurement.setName(name);
        assertEquals(name, measurement.getName());
    }

    @Test
    public void descriptionAccessor() {
        String description = "Linear extent of a measured distance or dimension.";
        assertNull(measurement.getDescription());
        measurement.setDescription(description);
        assertEquals(description, measurement.getDescription());
    }

    @Test
    public void dimensionCodeAccessor() {
        String dimensionCode = "Length";
        assertNull(measurement.getDimensionCode());
        measurement.setDimensionCode(dimensionCode);
        assertEquals(dimensionCode, measurement.getDimensionCode());
    }

    @Test
    public void unitQuantityCodeAccessor() {
        String unitQuantityCode = "Length";
        assertNull(measurement.getUnitQuantityCode());
        measurement.setUnitQuantityCode(unitQuantityCode);
        assertEquals(unitQuantityCode, measurement.getUnitQuantityCode());
    }

    @Test
    public void namespaceAccessor() {
        String namespace = "SLB";
        assertNull(measurement.getNamespace());
        measurement.setNamespace(namespace);
        assertEquals(namespace, measurement.getNamespace());
    }

    @Test
    public void lastUpdatedAccessor() {
        String lastModified = "19940930";
        assertNull(measurement.getLastModified());
        measurement.setLastModified(lastModified);
        assertEquals(lastModified, measurement.getLastModified());
    }

    @Test
    public void dimensionAnalysisAccessor() {
        String dimensionAnalysis = "L";
        assertNull(measurement.getDimensionAnalysis());
        measurement.setDimensionAnalysis(dimensionAnalysis);
        assertEquals(dimensionAnalysis, measurement.getDimensionAnalysis());
    }

    @Test
    public void essenceAccessor() throws Exception {
        MeasurementEssenceImpl essence = measurement.getEssence();
        assertNotNull(essence);
        assertNull(essence.getAncestry());
        assertNull(essence.getBaseMeasurementEssenceJson());
        assertNull(essence.getParentEssenceJson());
        assertEquals("{}", essence.toJsonString());

        String essenceJson = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        measurement.setEssence(essence);
        assertEquals("Length", essence.getAncestry());
        assertEquals(essenceJson, essence.getBaseMeasurementEssenceJson());
        assertNull(essence.getParentEssenceJson());
        assertEquals(essenceJson, essence.toJsonString());
    }

    @Test
    public void deprecationAccessor() {
        MeasurementDeprecationInfoImpl deprecationInfo = measurement.getDeprecationInfo();
        assertNull(deprecationInfo);

        deprecationInfo = new MeasurementDeprecationInfoImpl();
        measurement.setDeprecationInfo(deprecationInfo);
        assertEquals(deprecationInfo, measurement.getDeprecationInfo());
    }

    @Test
    public void getUnits() {
        assertNotNull(measurement.getUnits());
        assertEquals(0, measurement.getUnits().size());

        UnitImpl unit = new UnitImpl();
        measurement.addUnit(unit);
        assertEquals(1, measurement.getUnits().size());
    }

    @Test
    public void equalsTest() {
        String ancestry = "Length.Millimeter";
        MeasurementImpl measurement1 = new MeasurementImpl();
        assertFalse(measurement1.equals(null));
        assertFalse(measurement1.equals(new Object()));

        MeasurementImpl measurement2 = new MeasurementImpl();
        assertTrue(measurement1.equals(measurement2));

        measurement1.getEssence().setAncestry(ancestry);
        assertFalse(measurement1.equals(measurement2));

        measurement2.getEssence().setAncestry(ancestry);
        assertTrue(measurement1.equals(measurement2));
    }

    @Test
    public void isBaseMeasurementTest() {
        MeasurementImpl measurement = new MeasurementImpl();
        assertNotNull(measurement);

        assertTrue(measurement.isBaseMeasurement());
        assertNotNull(measurement.toString());
    }

    @Test
    public void isChildMeasurementTest() {
        MeasurementImpl measurement = new MeasurementImpl();
        assertNotNull(measurement);
        measurement.setIsBaseMeasurement(false);
        assertFalse(measurement.isBaseMeasurement());
        assertNotNull(measurement.toString());
    }    
}
