package org.opengroup.osdu.unitservice.interfaces;

import java.util.List;

/**
 * An interface allows grouping a set of collections for different types of objects.
 * The types of objects in collections may include
 * <ol>
 *     <li>{@link Unit} collection</li>
 *     <li>{@link Measurement} collection</li>
 *     <li>{@link UnitMapItem} collection</li>
 *     <li>{@link MeasurementMapItem} collection</li>
 * </ol>
 */
public interface QueryResult {
    /**
     * Gets a list of units, {@link Unit}s in the result.
     * @return  a list of units.
     */
    public List<Unit> getUnits();

    /**
     * Gets a list of measurements, {@link Measurement}s in the result.
     * @return  a list of measurements.
     */
    public List<Measurement> getMeasurements();

    /**
     * Gets a list of unit map items, {@link UnitMapItem}s in the result.
     * @return  a list of unit map items.
     */
    public List<UnitMapItem> getUnitMapItems();

    /**
     * Gets a list of measurement map items, {@link MeasurementMapItem}s in the result.
     * @return  a list of measurement map items.
     */
    public List<MeasurementMapItem> getMeasurementMapItems();

    /**
     * Gets the map states {@link MapState}
     * @return The list of map states
     */
    public List<MapState> getMapStates();
    /**
     * Gets the total number of all items from the query.
     * @return total number of all items.
     */
    public int getTotalCount();

    /**
     * Gets the offset of all items from the query.
     * @return offset of all items from the query.
     */
    public int getOffset();

    /**
     * Gets the number of the items in the result.
     * @return number of the items in the result.
     */
    public int getCount();
}
