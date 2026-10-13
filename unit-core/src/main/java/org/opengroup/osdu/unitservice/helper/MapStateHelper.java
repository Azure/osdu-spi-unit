package org.opengroup.osdu.unitservice.helper;

import org.opengroup.osdu.unitservice.model.MapStateImpl;

/**
 * A static helper class for MapState
 */
public class MapStateHelper {
    private static MapStateImpl unresolvedState;
    private static MapStateImpl identicalState;

    /**
     * Checks whether the state text refers to identical state
     * @param state the state text
     * @return true if the text matches the identical state; otherwise, false
     */
    public static boolean isIdentical(String state) {
        return getIdenticalMapState().getState().equalsIgnoreCase(state);
    }

    /**
     * Checks whether the state text refers to unresolved state
     * @param state the state text
     * @return true if the text matches the unresolved state; otherwise, false
     */
    public static boolean isUnresolved(String state) {
        return getUnresolvedMapState().getState().equalsIgnoreCase(state);
    }

    /**
     * Gets the identical map state, {@link MapStateImpl}
     * @return the identical map state
     */
    public static MapStateImpl getIdenticalMapState() {
        if(identicalState == null)
            return createDefaultIdenticalState();
        return identicalState;
    }

    /**
     * Sets the identical map state, {@link MapStateImpl}
     * @param identical the identical map state
     */
    public static void setIdenticalState(MapStateImpl identical) {
        if(identical != null && !Utility.isNullOrEmpty(identical.getState()))
            identicalState = identical;
    }

    /**
     * Gets the unresolved map state, {@link MapStateImpl}
     * @return the unresolved map state
     */
    public static MapStateImpl getUnresolvedMapState() {
        if(unresolvedState == null)
            return createDefaultUnresolvedState();
        return unresolvedState;
    }

    /**
     * Sets the unresolved map state, {@link MapStateImpl}
     * @param unresolved the unresolved map state
     */
    public static void setUnresolvedMapState(MapStateImpl unresolved) {
        if(unresolved != null && !Utility.isNullOrEmpty(unresolved.getState()))
            unresolvedState = unresolved;
    }

    private static MapStateImpl createDefaultUnresolvedState() {
        return new MapStateImpl(
                "unresolved",
                "The exact definition is not found in the catalog...",
                "SLB");
    }

    private static MapStateImpl createDefaultIdenticalState() {
        return new MapStateImpl(
                "identical",
                "Indicates that the mapsTo conversion factor is identical to the mapsFrom conversion factor...",
                "http://w3.energistics.org/uom/Energistics_Unit_of_Measure_Usage_Guide_V1.pdf");
    }
}
