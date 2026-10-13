package org.opengroup.osdu.unitservice.interfaces;

import org.opengroup.osdu.unitservice.model.extended.UnitSystemInfo;

import java.util.List;

/**
 * A copy of the catalog that includes
 * <ul>
 *     <li>{@link Unit} collection</li>
 *     <li>{@link Measurement} collection</li>
 *     <li>{@link UnitSystem} collection</li>
 *     <li>{@link UnitMap} collection</li>
 *     <li>{@link MeasurementMap} collection</li>
 *     <li>{@link MapState} collection</li>
 * </ul>
 */
public interface Catalog {
    /***
     * Gets a short string representation of the date in the form YYYYMMDD enabling simple comparisons.
     * @return  the last update time in the form YYYYMMDD
     */
    public String getLastModified();

    /**
     * Gets the {@link Unit} collection from the catalog.
     * @return  {@link Unit} collection.
     */
    public List<Unit> getUnits();

    /**
     * Gets the {@link Measurement} collection from the catalog.
     * @return  {@link Measurement} collection.
     */
    public List<Measurement> getMeasurements();

    /**
     * Gets the {@link UnitSystemInfo} collection from the catalog.
     * @return  {@link UnitSystemInfo} collection.
     */
    public List<UnitSystemInfo> getUnitSystemInfos();

    /**
     * Gets the {@link UnitMap} collection from the catalog.
     * @return  {@link UnitMap} collection.
     */
    public List<UnitMap> getUnitMaps();

    /**
     * Gets the {@link MeasurementMap} collection from the catalog.
     * @return  {@link MeasurementMap} collection.
     */
    public List<MeasurementMap> getMeasurementMaps();

    /**
     * Gets the {@link MapState} collection from the catalog.
     * @return  {@link MapState} collection
     */
    public List<MapState> getMapStates();

    /**
     * Gets the total number of Units in the catalog
     * @return the total count
     */
    public int getTotalUnitCount();

    /**
     * Gets the total number of Measurements in the catalog
     * @return the total count
     */
    public int getTotalMeasurementCount();

    /**
     * Gets the total number of UnitSystems in the catalog
     * @return the total count
     */
    public int getTotalUnitSystemCount();

    /**
     * Gets the total number of UnitMaps in the catalog
     * @return the total count
     */
    public int getTotalUnitMapCount();

    /**
     * Gets the total number of MeasurementMaps in the catalog
     * @return the total count
     */
    public int getTotalMeasurementMapCount();

    /**
     * Gets the total number of MapStates in the catalog
     * @return the total count
     */
    public int getTotalMapStateCount();
}
