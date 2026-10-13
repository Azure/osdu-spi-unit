package org.opengroup.osdu.unitservice.interfaces;

/**
 * A interface declaring the owner/container as deprecated.
 * The properties provide additional information about the status and potential recommended {@link Unit}s.
 */
public interface UnitDeprecationInfo {

    /**
     * Gets the deprecation state. The well-known deprecation states include:
     * <ul>
     *     <li>identical</li>
     *     <li>precision</li>
     *     <li>corrected</li>
     *     <li>conversion</li>
     *     <li>conditional</li>
     *     <li>unsupported</li>
     *     <li>different</li>
     *     <li>unresolved</li>
     * </ul>
     * @return  deprecation state
     */
    public String getState();

    /**
     * Gets the remark of the deprecation reason.
     * @return  remark of the deprecation reason.
     */
    public String getRemarks();

    /**
     * Gets the {@link UnitEssence#getJsonString()} of the unit superseding the owner/container unit.
     * @return Json string of the unit superseding the owner/container unit.
     */
    public String getSupersededByUnit();
}
