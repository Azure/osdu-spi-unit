package org.opengroup.osdu.unitservice.interfaces;


import java.util.List;

/**
 * An interface defines the measurement model.
 */
public interface Measurement {

    /**
     * Gets the unique code, which identifies the {@link Measurement}.
     * @return  code of the measurement
     */
    public String getCode();

    /**
     * Gets the human readable name of the {@link Measurement}.
     * @return  name of the measurement
     */
    public String getName();

    /**
     * Gets the description of the {@link Measurement}.
     * @return  description of the measurement
     */
    public String getDescription();

    /**
     * Gets the OSDD Unit_Dimension code.
     * @return  dimension code of the measurement
     */
    public String getDimensionCode();

    /**
     * Gets the OSDD Unit_Quantity code.
     * @return  Unit_Quantity code of the measurement
     */
    public String getUnitQuantityCode();

    /**
     * Gets the OSDD namespace; typical values are "SLB" and "Energistics_UoM".
     * @return  namespace of the measurement
     */
    public String getNamespace();

    /***
     * Gets a short string representation of the date in the form YYYYMMDD enabling simple comparisons.
     * @return  the last modified time in the form YYYYMMDD
     */
    public String getLastModified();

    /**
     * Gets the {@link MeasurementEssence} of the {@link Measurement}.
     * @return  essence object of the measurement
     */
    public MeasurementEssence getEssence();

    /**
     * Gets the {@link MeasurementEssence} as JSON string
     * @return persistable reference string
     */
    public String getEssenceJson();

    /**
     * Gets the {@link MeasurementDeprecationInfo} of the {@link Measurement}.
     * @return  null or deprecation info object. null means measurement is not deprecated by other measurement.
     */
    public MeasurementDeprecationInfo getDeprecationInfo();

    /**
     * Gets the list of the child {@link Measurement}s' Json strings.
     * @return  Json strings of the child measurements.
     */
    public List<String> getChildMeasurementEssenceJsons();

    /**
     * Gets the list of preferred unit Json strings for the given, potentially specialized {@link Measurement}.
     * Null-lists mean that the preferred unit Json strings are derived from the parent {@link Measurement}.
     * @return  Json strings of the units for the measurement.
     */
    public List<String> getUnitEssenceJsons();

    /**
     * Gets the list of preferred unit Json strings for the given, potentially specialized {@link Measurement}.
     * Null-lists mean that the preferred unit Json strings are derived from the parent {@link Measurement}.
     * @return  Json strings of the preferred units for the measurement.
     */
    public List<String> getPreferredUnitEssenceJsons();

    /**
     *  Gets the Json string of the parent measurement. It is null for base measurement.
     * @return  Json string of the parent measurement.
     */
    public String getParentEssenceJson();

    /**
     *  Gets the Json string of the base measurement.
     * @return Json string of the base measurement.
     */
    public String getBaseMeasurementEssenceJson();

    /**
     * Gets the dimension analysis.
     * @return  dimension analysis of the measurement.
     */
    public String getDimensionAnalysis();

    /**
     * Returns whether the measurement is a base measurement or not.
     * @return  true for base measurement; otherwise, false.
     */
    public boolean isBaseMeasurement();

}
