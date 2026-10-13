package org.opengroup.osdu.unitservice.interfaces;

import java.util.List;

/**
 * An interface describing a unit system, i.e. a set of {@link Unit}s assigned to {@link Measurement}s.
 */
public interface UnitSystem {

    /**
     * Gets the number of {@link UnitAssignment}s.
     * @return number of unit assignments
     */
    public int getUnitAssignmentCount();

    /**
     * Gets the name of the unit system.
     * @return  name
     */
    public String getName();

    /**
     *  Gets the description of the unit system
     * @return  description
     */
    public String getDescription();

    /**
     * Gets the name of the reference unit system
     * @return name of the reference unit system
     */
    public String getReferenceUnitSystem();

    /**
     * Gets the ancestry of the unit system.
     * @return ancestry of the unit system
     */
    public String getAncestry();

    /**
     * Gets the Json string of the unit system.
     * @return  Json string of the unit system
     */
    public String toJsonString();

    /***
     * Gets a short string representation of the date in the form YYYYMMDD enabling simple comparisons.
     * @return  the last update time in the form YYYYMMDD
     */
    public String getLastModified();

    /**
     * Gets the source of the unit system.
     * @return  source
     */
    public String getSource();

    /**
     *  Gets a list of {@link UnitAssignment}s defined in the unit system.
     * @return a list of unit assignments
     */
    public List<UnitAssignment> getUnitAssignments();

    /**
     * Gets the number of unit assignment items in this response
     * @return the size of getUnitAssignments()
     */
    public int getUnitAssignmentCountInResponse();

    /**
     * Gets the total number of unit assignments for this unit system
     * @return the total number of unit assignments defined for this unit system
     */
    public int getUnitAssignmentCountTotal();

    /**
     * Gets the offset, which was requested
     * @return The offset into the array of unit assignments as requested.
     */
    public int getOffset();

    /**
     * Gets the JSON serialized essence string aka persistable reference
     * @return
     */
    public String getPersistableReference();
}
