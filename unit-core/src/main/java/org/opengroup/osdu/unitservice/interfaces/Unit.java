package org.opengroup.osdu.unitservice.interfaces;

/**
 * An interface defines unit model.
 */
public interface Unit {

    /**
     * Gets the display symbol of the unit
     * @return  display symbol
     */
    public String getDisplaySymbol();

    /**
     * Gets the name of the unit
     * @return  name
     */
    public String getName();

    /**
     * Gets the description of the unit
     * @return  description
     */
    public String getDescription();

    /***
     * Gets a short string representation of the date in the form YYYYMMDD enabling simple comparisons.
     * @return  the last update time in the form YYYYMMDD
     */
    public String getLastModified();

    /**
     *  Gets the source of the unit definition.
     * @return  source
     */
    public String getSource();

    /**
     *  Gets the namespace that the unit belongs to.
     * @return  namespace
     */
    public String getNamespace();

    /**
     *  Gets the {@link UnitEssence} of the unit.
     * @return  {@link UnitEssence} instance.
     */
    public UnitEssence getEssence();

    /**
     * Gets the {@link UnitEssence}, the characteristic unit properties as JSON string, aka persistable reference.
     * @return The persistable reference string
     */
    public String getEssenceJson();

    /**
     *  Gets the {@link UnitDeprecationInfo} of the unit if existing.
     * @return  null or {@link UnitDeprecationInfo} instance
     */
    public UnitDeprecationInfo getDeprecationInfo();

}
