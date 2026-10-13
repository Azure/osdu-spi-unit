package org.opengroup.osdu.unitservice.interfaces;

/**
 * An interface defines the scale/offset model for {@link Unit}
 */
public interface ScaleOffset {

    /**
     * Gets the {@link Unit} scale coefficient as in y = scale*(x-offset)
     * @return  scale coefficient
     */
    public double getScale();

    /**
     * Gets the {@link Unit} offset in the y = scale*(x-offset) parameterization.
     * @return offset coefficient
     */
    public double getOffset();
}
