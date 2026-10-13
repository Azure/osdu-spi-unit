package org.opengroup.osdu.unitservice.model;

import org.opengroup.osdu.unitservice.helper.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitImplTest {
    private UnitImpl unit;

    @BeforeEach
    public void setup() {
        unit = new UnitImpl();
    }

    @Test
    public void displaySymbolAccessor() {
        String displaySymbol = "foot";
        assertNull(unit.getDisplaySymbol());
        unit.setDisplaySymbol(displaySymbol);
        assertEquals(displaySymbol, unit.getDisplaySymbol());
    }

    @Test
    public void nameAccessor() {
        String name = "ft";
        assertNull(unit.getName());
        unit.setName(name);
        assertEquals(name, unit.getName());
    }

    @Test
    public void descriptionAccessor() {
        String description = "hello";
        assertNull(unit.getDescription());
        unit.setDescription(description);
        assertEquals(description, unit.getDescription());
    }

    @Test
    public void lastUpdatedAccessor() {
        String lastUpdated = "19940930";
        assertNull(unit.getLastModified());
        unit.setLastModified(lastUpdated);
        assertEquals(lastUpdated, unit.getLastModified());
    }

    @Test
    public void sourceAccessor() {
        String source = "APIRP661";
        assertNull(unit.getSource());
        unit.setSource(source);
        assertEquals(source, unit.getSource());
    }

    @Test
    public void namespaceAccessor() {
        String namespace = "RP66";
        assertNull(unit.getNamespace());
        unit.setNamespace(namespace);
        assertEquals(namespace, unit.getNamespace());
    }

    @Test
    public void essenceAccessor() throws Exception {
        UnitEssenceImpl essence = unit.getEssence();
        assertNotNull(essence);
        assertNull(essence.getABCD());
        assertNull(essence.getScaleOffset());
        assertNull(essence.getSymbol());
        assertEquals("{}",essence.toJsonString());
        assertNull(essence.getBaseMeasurement());

        // Essence with scale offset
        String baseMeasurementJson = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        String essenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"FT\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"}}";
        essence = Utility.fromJsonString(essenceJson, UnitEssenceImpl.class);
        unit.setEssence(essence);
        assertEquals(essence, unit.getEssence());
        assertEquals("FT", essence.getSymbol());
        assertEquals(essenceJson, essence.toJsonString());
        assertEquals(baseMeasurementJson, essence.getBaseMeasurementEssence().toJsonString());
        assertNotNull(essence.getScaleOffset());
        assertNull(essence.getABCD());
        // We don't create base measurement here
        assertNull(essence.getBaseMeasurement());

        //Essence with abcd
        essenceJson = "{\"abcd\":{\"a\":0.0,\"b\":0.3048,\"c\":1.0,\"d\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"L\",\"type\":\"UM\"}}";
        baseMeasurementJson = "{\"ancestry\":\"L\",\"type\":\"UM\"}";
        essence = Utility.fromJsonString(essenceJson, UnitEssenceImpl.class);
        unit.setEssence(essence);
        assertEquals(essence, unit.getEssence());
        assertEquals("ft", essence.getSymbol());
        assertEquals(essenceJson, essence.toJsonString());
        assertEquals(baseMeasurementJson, essence.getBaseMeasurementEssence().toJsonString());
        assertNotNull(essence.getABCD());
        assertNull(essence.getScaleOffset());
        // We don't create base measurement here
        assertNull(essence.getBaseMeasurement());
    }

    @Test
    public void deprecationAccessor() {
        UnitDeprecationInfoImpl deprecationInfo = unit.getDeprecationInfo();
        assertNull(deprecationInfo);

        deprecationInfo = new UnitDeprecationInfoImpl();
        unit.setDeprecationInfo(deprecationInfo);
        assertEquals(deprecationInfo, unit.getDeprecationInfo());
    }

    @Test
    public void equalsTest() throws Exception {
        UnitImpl unit1 = new UnitImpl();
        assertFalse(unit1.equals(null));
        assertFalse(unit1.equals(new Object()));

        UnitImpl unit2 = new UnitImpl();
        assertTrue(unit1.equals(unit2));

        String essenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"FT\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"}}";
        UnitEssenceImpl essence = Utility.fromJsonString(essenceJson, UnitEssenceImpl.class);
        unit1.setEssence(essence);
        assertFalse(unit1.equals(unit2));

        unit2.setEssence(essence);
        assertTrue(unit1.equals(unit2));
    }
}
