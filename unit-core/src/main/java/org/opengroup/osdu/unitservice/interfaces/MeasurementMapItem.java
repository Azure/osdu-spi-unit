package org.opengroup.osdu.unitservice.interfaces;

/**
 * An interface defines the mapping state between two {@link Measurement}s
 */
public interface MeasurementMapItem {
    /**
     * Gets the from{@link Measurement}.
     * @return  fromMeasurement
     */
    public Measurement getFromMeasurement();

    /**
     * Gets the to{@link Measurement}.
     * @return  toMeasurement
     */
    public Measurement getToMeasurement();

    /**
     * Gets the 'from' namespace
     * @return The 'from' namespace
     */
    public String getFromNamespace();

    /**
     * Gets the 'to' namespace
     * @return The 'to' namespace
     */
    public String getToNamespace();

    /**
     * Gets the state definition
     * @return  state definition
     */
    public String getState();

    /**
     * Gets the note of the mapping
     * @return  note of the mapping
     */
    public String getNote();
}
