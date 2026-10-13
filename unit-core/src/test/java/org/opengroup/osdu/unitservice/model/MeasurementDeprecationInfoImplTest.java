package org.opengroup.osdu.unitservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MeasurementDeprecationInfoImplTest {
    MeasurementDeprecationInfoImpl deprecationInfo;

    @BeforeEach
    public void setup() {
        deprecationInfo = new MeasurementDeprecationInfoImpl();
    }

    @Test
    public void emptyConstructor() {
        assertNull(deprecationInfo.getState());
        assertNull(deprecationInfo.getRemarks());
        assertNull(deprecationInfo.getSupersededByUnitMeasurement());
    }

    @Test
    public void stateAccessor() {
        String state = "identical";
        deprecationInfo.setState(state);
        assertEquals(state, deprecationInfo.getState());

        String toString = "P-DeprecationInfo " + state;
        assertEquals(toString, deprecationInfo.toString());
    }

    @Test
    public void remarksAccessor() {
        String remarks = "deprecation state this indicates that the superseded item is identical.";
        deprecationInfo.setRemarks(remarks);
        assertEquals(remarks, deprecationInfo.getRemarks());
    }
}
