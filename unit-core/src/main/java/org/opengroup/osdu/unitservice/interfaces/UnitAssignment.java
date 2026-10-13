package org.opengroup.osdu.unitservice.interfaces;

/**
 * A {@link UnitAssignment} assigns a {@link Unit} to a {@link Measurement} for a given {@link UnitSystem}
 */
public interface UnitAssignment {

    /**
     * Gets the {@link Measurement} in the assignment.
     * @return {@link Measurement} instance.
     */
    public Measurement getMeasurement();

    /**
     * Gets the {@link Unit} in the assignment.
     * @return {@link Unit} instance.
     */
    public Unit getUnit();

    /***
     * Gets a short string representation of the date in the form YYYYMMDD enabling simple comparisons.
     * @return  the last update time in the form YYYYMMDD
     */
    public String getLastModified();
}
