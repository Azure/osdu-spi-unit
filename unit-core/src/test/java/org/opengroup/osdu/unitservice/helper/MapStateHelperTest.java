package org.opengroup.osdu.unitservice.helper;

import org.opengroup.osdu.unitservice.model.MapStateImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapStateHelperTest {
    private MapStateImpl identical = new MapStateImpl(
            "identical",
            "Indicates that the mapsTo conversion factor is identical to the mapsFrom conversion factor...",
            "http://w3.energistics.org/uom/Energistics_Unit_of_Measure_Usage_Guide_V1.pdf");

    private MapStateImpl unresolved = new MapStateImpl(
            "unresolved",
            "The exact definition is not found in the catalog...",
            "SLB");

    private static MapStateImpl defaultIdenticalState;
    private static MapStateImpl defaultUnresolvedState;

    @BeforeAll
    public static void beforeClassSetup() {
        // Get the default states before tests
        defaultIdenticalState = MapStateHelper.getIdenticalMapState();
        defaultUnresolvedState = MapStateHelper.getUnresolvedMapState();
    }

    @AfterEach
    public void teardown() {
        //Set the states back
        MapStateHelper.setIdenticalState(defaultIdenticalState);
        MapStateHelper.setUnresolvedMapState(defaultUnresolvedState);
    }

    @Test
    public void isIdentical() {
        assertTrue(MapStateHelper.isIdentical(identical.getState()));
        assertFalse(MapStateHelper.isIdentical("different"));
        assertFalse(MapStateHelper.isIdentical((String)null));
    }

    @Test
    public void isUnresolved() {
        assertTrue(MapStateHelper.isUnresolved(unresolved.getState()));
        assertFalse(MapStateHelper.isUnresolved("different"));
        assertFalse(MapStateHelper.isUnresolved((String)null));
    }

    @Test
    public void identicalMapStateAccessor() {
        MapStateImpl state;
        state = MapStateHelper.getIdenticalMapState();
        assertEquals(identical.getState(), state.getState());
        assertTrue(MapStateHelper.isIdentical(identical.getState()));

        // null map state won't take any effect
        MapStateHelper.setIdenticalState(null);
        state = MapStateHelper.getIdenticalMapState();
        assertEquals(identical.getState(), state.getState());
        assertTrue(MapStateHelper.isIdentical(identical.getState()));

        // empty map state won't take any effect
        MapStateHelper.setIdenticalState(new MapStateImpl("", "", ""));
        state = MapStateHelper.getIdenticalMapState();
        assertEquals(identical.getState(), state.getState());
        assertTrue(MapStateHelper.isIdentical(identical.getState()));

        // change the default identical mapstate
        String newIdentical = "new-identical";
        MapStateHelper.setIdenticalState(new MapStateImpl(newIdentical, "", ""));
        state = MapStateHelper.getIdenticalMapState();
        assertEquals(newIdentical, state.getState());
        assertTrue(MapStateHelper.isIdentical(newIdentical));
    }

    @Test
    public void unresolvedMapStateAccessor() {
        MapStateImpl state;
        state = MapStateHelper.getUnresolvedMapState();
        assertEquals(unresolved.getState(), state.getState());
        assertTrue(MapStateHelper.isUnresolved(unresolved.getState()));

        // null map state won't take any effect
        MapStateHelper.setUnresolvedMapState(null);
        state = MapStateHelper.getUnresolvedMapState();
        assertEquals(unresolved.getState(), state.getState());
        assertTrue(MapStateHelper.isUnresolved(unresolved.getState()));

        // empty map state won't take any effect
        MapStateHelper.setUnresolvedMapState(new MapStateImpl("", "", ""));
        state = MapStateHelper.getUnresolvedMapState();
        assertEquals(unresolved.getState(), state.getState());
        assertTrue(MapStateHelper.isUnresolved(unresolved.getState()));

        // change the default unresolved mapstate
        String newIdentical = "new-unresolved";
        MapStateHelper.setUnresolvedMapState(new MapStateImpl(newIdentical, "", ""));
        state = MapStateHelper.getUnresolvedMapState();
        assertEquals(newIdentical, state.getState());
        assertTrue(MapStateHelper.isUnresolved(newIdentical));
    }

}
