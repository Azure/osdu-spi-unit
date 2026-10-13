package org.opengroup.osdu.unitservice.model.extended;

import org.opengroup.osdu.unitservice.interfaces.*;
import org.opengroup.osdu.unitservice.model.MapStateImpl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * An class allows grouping a set of collections for different types of objects.
 * The types of objects in collections may include
 * <ol>
 *     <li>{@link Unit} collection</li>
 *     <li>{@link Measurement} collection</li>
 *     <li>{@link UnitMapItem} collection</li>
 *     <li>{@link MeasurementMapItem} collection</li>
 * </ol>
 */
@Data
@Schema(description ="A result of a query containing unit of measure items.")
public class QueryResultImpl implements QueryResult {
    @Schema(description = "Number of items contained in the catalog",type = "integer", example = "0")
    private int totalCount = 0;
    @Schema(description = "The offset position for this query. Default is 0",type = "integer", example = "0")
    private int offset = 0;
    @Schema(description = "The array of Unit items",type = "array", implementation = Unit.class)
    private List<Unit> units;
    @Schema(description = "The array of Measurement items",type = "array", implementation = Measurement.class)
    private List<Measurement> measurements;
    @Schema(description = "The array of UnitMapItems describing unit-to-unit pairs",type = "array", implementation = UnitMapItem.class)
    private List<UnitMapItem> unitMapItems;
    @Schema(description = "The array of measurement-to-measurement maps",type = "array", implementation = MeasurementMapItem.class)
    private List<MeasurementMapItem> measurementMapItems;
    @Schema(type = "array", implementation = MapStateImpl.class)
    private List<MapStateImpl> mapStates;

    /**
     * Empty constructor
     */
    public QueryResultImpl() {
        this.units = new ArrayList<>();
        this.unitMapItems = new ArrayList<>();
        this.measurements = new ArrayList<>();
        this.measurementMapItems = new ArrayList<>();
        this.mapStates = new ArrayList<>();
        this.totalCount = 0;
        this.offset = 0;
    }

    /**
     * Gets a list of units, {@link Unit}s in the result.
     * @return a list of units.
     */
    public List<Unit> getUnits() {
        return this.units;
    }

    /**
     * Adds a unit, {@link Unit}, to the unit collection.
     * @param unit unit
     */
    public void addUnit(Unit unit) { this.units.add(unit); }

    /**
     * Gets a list of units, {@link Measurement}s in the result.
     * @return  a list of measurements.
     */
    public List<Measurement> getMeasurements() {
        return this.measurements;
    }

    /**
     * Adds a measurement, {@link Measurement}, to the measurement collection.
     * @param measurement a measurement
     */
    public void addMeasurement(Measurement measurement) { this.measurements.add(measurement);  }

    /**
     * Gets a list of unit map items, {@link UnitMapItem}s in the result.
     * @return  a list of unit map items.
     */
    public List<UnitMapItem> getUnitMapItems() {
        return this.unitMapItems;
    }

    /**
     * Adds a unit map item, {@link UnitMapItem}, to the unit map item collection.
     * @param unitMapItem a unit map item
     */
    public void addUnitMapItem(UnitMapItem unitMapItem) { this.unitMapItems.add(unitMapItem); }

    /**
     * Gets a list of measurement map items, {@link MeasurementMapItem}s in the result.
     * @return  a list of measurement map items.
     */
    public List<MeasurementMapItem> getMeasurementMapItems() {
        return this.measurementMapItems;
    }

    /**
     * Adds a measurement map item, {@link MeasurementMapItem}, to the measurement map item collection.
     * @param measurementMapItem a measurement map item
     */
    public void addMeasurementMapItem(MeasurementMapItem measurementMapItem) { this.measurementMapItems.add(measurementMapItem); }

    /**
     * Gets the map states {@link MapState}
     * @return The list of map states
     */
    public List<MapState> getMapStates() {
        List<MapState> result = new ArrayList<>();
        for (MapStateImpl mapState: this.mapStates) result.add((MapState)mapState);
        return result;
    }

    public void setMapStates(List<MapStateImpl> mapStates) {
        if (mapStates == null) this.mapStates = new ArrayList<>();
        else this.mapStates = mapStates;
    }
    /**
     * Gets the total number of all items from the query.
     * @return total number of all items.
     */
    public int getTotalCount() { return this.totalCount;  }

    /**
     * Sets the total number of all items from the query.
     * @param totalCount total number of all items.
     * @throws IllegalArgumentException an exception will be thrown if totalCount is negative.
     */
    public void setTotalCount(int totalCount) throws IllegalArgumentException {
        if(totalCount < 0)
            throw new IllegalArgumentException("totalCount can not be negative");
        this.totalCount = totalCount;
    }

    /**
     * Gets the offset of all items from the query.
     * @return offset of all items from the query.
     */
    public int getOffset() { return this.offset; }

    /**
     * Sets the offset of all items from the query.
     * @param offset offset of all items from the query.
     * @throws IllegalArgumentException an exception will be thrown if offset is negative.
     */
    public void setOffset(int offset) throws IllegalArgumentException {
        if(offset < 0)
            throw new IllegalArgumentException("offset can not be negative");
        this.offset = offset;
    }

    /**
     * Gets the number of the items in the result.
     * @return number of the items in the result.
     * @throws IllegalArgumentException an exception will be thrown if offset + count &lt; totalCount.
     */
    public int getCount() throws IllegalArgumentException {
        int count = 0;
        count += units.size();
        count += measurements.size();
        count += unitMapItems.size();
        count += measurementMapItems.size();
        count += mapStates.size();
        if(offset + count > totalCount)
            throw new IllegalArgumentException("offset + count > totalCount is impossible");

        return count;
    }
}
