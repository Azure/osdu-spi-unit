package org.opengroup.osdu.unitservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UnitDeprecationInfoImplTest {

    @Test
    public void emptyConstructor() {
        UnitDeprecationInfoImpl deprecationInfo = new UnitDeprecationInfoImpl();
        assertNull(deprecationInfo.getState());
        assertNull(deprecationInfo.getRemarks());
        assertNull(deprecationInfo.getSupersededByUnit());
    }

    @Test
    public void stateAccessor() {
        UnitDeprecationInfoImpl deprecationInfo = new UnitDeprecationInfoImpl();
        String state = "identical";
        deprecationInfo.setState(state);
        assertEquals(state, deprecationInfo.getState());
    }

    @Test
    public void remarksAccessor() {
        UnitDeprecationInfoImpl deprecationInfo = new UnitDeprecationInfoImpl();
        String remarks = "deprecation state this indicates that the superseded item is identical.";
        deprecationInfo.setRemarks(remarks);
        assertEquals(remarks, deprecationInfo.getRemarks());
    }
}
