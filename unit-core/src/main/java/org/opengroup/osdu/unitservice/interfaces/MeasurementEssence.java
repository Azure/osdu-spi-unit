package org.opengroup.osdu.unitservice.interfaces;

/**
 * An interface defines the essence of {@link Measurement}.
 */
public interface MeasurementEssence {

    /**
     * Gets the ancestry. The ancestry is the '.'-separated list of all {@link Measurement#getCode()} in the parentage,
     * e.g: 'Plane_Angle.Image_Origin_Azimuth'.
     * @return ancestry of the measurement.
     */
    public String getAncestry();

    /**
     * Gets the type identifier, fixed to 'UM'.
     * @return type of the measurement.
     */
    public String getType();

    /**
     * Gets the Json string of the measurement.
     * @return  Json string of the measurement
     */
    public String toJsonString();

}
