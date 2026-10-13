package org.opengroup.osdu.unitservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitSystemImplTest {
    UnitSystemImpl unitSystem;

    @BeforeEach
    public void setup() {
        unitSystem = new UnitSystemImpl();
    }

    @Test
    public void constructor() {
        UnitSystemImpl unitSystem = new UnitSystemImpl();
        assertNull(unitSystem.getParent());
        assertEquals(0, unitSystem.getUnitAssignmentCount());
        assertNull(unitSystem.getName());
        assertNull(unitSystem.getDescription());
        assertNull(unitSystem.getReferenceUnitSystem());
        assertNull(unitSystem.getAncestry());
        assertEquals("{}", unitSystem.toJsonString());
        assertNull(unitSystem.getLastModified());
        assertNull(unitSystem.getSource());
        assertNotNull(unitSystem.getNativeUnitAssignments());
        assertEquals(0, unitSystem.getNativeUnitAssignments().size());
        assertNotNull(unitSystem.getUnitAssignments());
        assertEquals(0, unitSystem.getUnitAssignments().size());
        assertNotNull(unitSystem.getEssence());
    }

    @Test
    public void parentAccessor() {
        UnitSystemImpl parent = new UnitSystemImpl();
        unitSystem.setParent(parent);
        assertEquals(parent, unitSystem.getParent());
    }

    @Test
    public void nameAccessor() {
        String name = "abc";
        unitSystem.setName(name);
        assertEquals(name, unitSystem.getName());
    }

    @Test
    public void descriptionAccessor() {
        String description = "abc";
        unitSystem.setDescription(description);
        assertEquals(description, unitSystem.getDescription());
    }

    @Test
    public void referenceUnitSystemAccessor() {
        String referenceUnitSystem = "abc";
        unitSystem.setReferenceUnitSystem(referenceUnitSystem);
        assertEquals(referenceUnitSystem, unitSystem.getReferenceUnitSystem());
    }

    @Test
    public void ancestryAccessor() {
        assertEquals("{}", unitSystem.toJsonString());

        String ancestry = "abc";
        unitSystem.setAncestry(ancestry);
        assertEquals(ancestry, unitSystem.getAncestry());
        assertNotNull(unitSystem.toJsonString());
        assertEquals("{\"ancestry\":\"abc\"}", unitSystem.toJsonString());
    }

    @Test
    public void lastModifiedAccessor() {
        String lastModified = "20160712";
        unitSystem.setLastModified(lastModified);
        assertEquals(lastModified, unitSystem.getLastModified());
    }

    @Test
    public void sourceAccessor() {
        String source = "SLB";
        unitSystem.setSource(source);
        assertEquals(source, unitSystem.getSource());
    }

    @Test
    public void addUnitAssignment() {
        // Child unit system
        assertEquals(0, unitSystem.getUnitAssignmentCount());
        assertEquals(0, unitSystem.getNativeUnitAssignments().size());
        assertEquals(0, unitSystem.getUnitAssignments().size());

        UnitAssignmentImpl unitAssignment = createUnitAssignment("Length.Diameter");
        unitSystem.addNativeUnitAssignment(unitAssignment);
        assertEquals(1, unitSystem.getUnitAssignmentCount());
        assertEquals(1, unitSystem.getNativeUnitAssignments().size());
        assertEquals(1, unitSystem.getUnitAssignments().size());

        // Parent unit system
        UnitSystemImpl parent = new UnitSystemImpl();
        assertEquals(0, parent.getUnitAssignmentCount());
        assertEquals(0, parent.getNativeUnitAssignments().size());
        assertEquals(0, parent.getUnitAssignments().size());

        UnitAssignmentImpl unitAssignment1 = createUnitAssignment("Length.Diameter"); // Same as unitAssignment
        UnitAssignmentImpl unitAssignment2 = createUnitAssignment("Length");
        parent.addNativeUnitAssignment(unitAssignment1);
        parent.addNativeUnitAssignment(unitAssignment2);
        assertEquals(2, parent.getUnitAssignmentCount());
        assertEquals(2, parent.getNativeUnitAssignments().size());
        assertEquals(2, parent.getUnitAssignments().size());

        // Once the parent is changed, the unit assignment will be recalculated
        unitSystem.setParent(parent);
        assertEquals(2, unitSystem.getUnitAssignmentCount());    // Duplicate measurement won't be inherited from parent
        assertEquals(1, unitSystem.getNativeUnitAssignments().size());
        assertEquals(2, unitSystem.getUnitAssignments().size()); // Duplicate measurement won't be inherited from parent
        assertEquals(2, parent.getUnitAssignmentCount());
        assertEquals(2, parent.getNativeUnitAssignments().size());
        assertEquals(2, parent.getUnitAssignments().size());
    }

    @Test
    public void addIncompleteUnitAssignment() {
        MeasurementImpl measurement = new MeasurementImpl();
        measurement.setIsBaseMeasurement(false);
        MeasurementEssenceImpl measurementEssence = new MeasurementEssenceImpl();
        measurementEssence.setAncestry("Length");
        measurement.setEssence(measurementEssence);
        UnitImpl unit = new UnitImpl();

        try {
            unitSystem.addNativeUnitAssignment(new UnitAssignmentImpl(null, unit));
            fail("unexpected result from addNativeUnitAssignment() with null measurement.");
        }
        catch(IllegalArgumentException ex) {
            //ignore as it is expected
        }

        try {
            unitSystem.addNativeUnitAssignment(new UnitAssignmentImpl(measurement, null));
            fail("unexpected result from addNativeUnitAssignment() with null unit.");
        }
        catch(IllegalArgumentException ex) {
            //ignore as it is expected
        }
    }

    @Test
    public void equalsTest() {
        UnitSystemImpl unitSystem1 = new UnitSystemImpl();
        assertFalse(unitSystem1.equals(null));
        assertFalse(unitSystem1.equals(new Object()));

        UnitSystemImpl unitSystem2 = new UnitSystemImpl();
        assertTrue(unitSystem1.equals(unitSystem2));

        String ancestry = "abc";
        unitSystem1.setAncestry(ancestry);
        assertFalse(unitSystem1.equals(unitSystem2));

        unitSystem2.setAncestry(ancestry);
        assertTrue(unitSystem1.equals(unitSystem2));
    }

    private UnitAssignmentImpl createUnitAssignment(String measurementAncestry) {
        MeasurementImpl measurement = new MeasurementImpl();
        measurement.setIsBaseMeasurement(false);
        MeasurementEssenceImpl measurementEssence = new MeasurementEssenceImpl();
        measurementEssence.setAncestry(measurementAncestry);
        measurement.setEssence(measurementEssence);
        UnitImpl unit = new UnitImpl();
        return new UnitAssignmentImpl(measurement, unit);
    }

}
