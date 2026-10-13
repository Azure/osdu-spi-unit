package org.opengroup.osdu.unitservice.interfaces;

/**
 * An interface of deprecation info model for {@link Measurement}.
 */
public interface MeasurementDeprecationInfo {

    /**
     * Gets the deprecation state. The well-known states include:
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
     * Gets further remarks about the deprecation reason.
     * @return  remark of the deprecation
     */
    public String getRemarks();

    /**
     * Gets the Json string of {@link Measurement} superseding the owner/container measurement.
     * @return Json string of the measurement superseding the owner/container measurement
     */
    public String getSupersededByUnitMeasurement();
}
