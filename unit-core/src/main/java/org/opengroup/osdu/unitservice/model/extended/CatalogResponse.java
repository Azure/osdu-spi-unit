package org.opengroup.osdu.unitservice.model.extended;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.opengroup.osdu.unitservice.interfaces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A class implements API {@link Catalog} to return a customized copy of the catalog.
 * */
@Data
public class CatalogResponse implements Catalog {
    @Schema(type = "string")
    private String lastModified;
    @Schema(type = "array", implementation = Unit.class)
    private List<Unit> units;
    @Schema(type = "integer")
    private int totalUnitCount;
    @Schema(type = "array", implementation = Measurement.class)
    private List<Measurement> measurements;
    @Schema(type = "integer")
    private int totalMeasurementCount;
    @Schema(type = "array", implementation = UnitSystemInfo.class)
    private List<UnitSystemInfo> unitSystemInfos;
    @Schema(type = "integer")
    private int totalUnitSystemCount;
    @Schema(type = "array", implementation = UnitMap.class)
    private List<UnitMap> unitMaps;
    @Schema(type = "integer")
    private int totalUnitMapCount;
    @Schema(type = "array", implementation = MeasurementMap.class)
    private List<MeasurementMap> measurementMaps;
    @Schema(type = "integer")
    private int totalMeasurementMapCount;
    @Schema(type = "array", implementation = MapState.class)
    private List<MapState> mapStates;
    @Schema(type = "integer")
    private int totalMapStateCount;

    /**
     * Constructor
     */
    public CatalogResponse() {
        units = new ArrayList<>();
        measurements = new ArrayList<>();
        unitSystemInfos = new ArrayList<>();
        unitMaps = new ArrayList<>();
        measurementMaps = new ArrayList<>();
        mapStates = new ArrayList<>();
        lastModified = "";
    }

    /**
     * Get a string representation of the last modified date
     * @return the last modified date in string format
     */
    public String getLastModified() { return lastModified;  }

    /**
     * Sets the last modified date of the catalog
     * @param lastModified last modified date in string format
     */
    public void setLastModified(String lastModified) { this.lastModified = lastModified; }

    /**
     * Gets a list of {@link Unit}s.
     * @return Unit collection
     */
    public List<Unit> getUnits() {
        return this.units;
    }

    /**
     * Sets {@link Unit} collection
     * @param units Unit collection
     */
    public void setUnits(List<Unit> units) {
        this.units = (units != null)? new ArrayList<>(units) : new ArrayList<Unit>();
    }

    /**
     * Gets a list of {@link Measurement}s.
     * @return Measurement collection
     */
    public List<Measurement> getMeasurements() {
        return this.measurements;
    }

    /**
     * Sets {@link Measurement} collection
     * @param measurements Measurement collection
     */
    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = (measurements != null)? new ArrayList<>(measurements) : new ArrayList<Measurement>();
    }

    /**
     * Gets a list of {@link UnitSystem}s.
     * @return Unit system collection
     */
    public List<UnitSystemInfo> getUnitSystemInfos() {
        return this.unitSystemInfos;
    }

    /**
     * Sets {@link UnitSystem} collection
     * @param unitSystemInfos Unit system collection
     */
    public void setUnitSystemInfos(List<UnitSystemInfo> unitSystemInfos) {
        this.unitSystemInfos = (unitSystemInfos != null)? new ArrayList<>(unitSystemInfos) : new ArrayList<UnitSystemInfo>();
    }

    /**
     * Gets a list of {@link UnitMap}s.
     * @return Unit map collection
     */
    public List<UnitMap> getUnitMaps() {
        return this.unitMaps;
    }

    /**
     * Sets {@link UnitMap} collection
     * @param unitMaps Unit map collection
     */
    public void setUnitMaps(List<UnitMap> unitMaps) {
        this.unitMaps = (unitMaps != null)? new ArrayList<>(unitMaps) : new ArrayList<UnitMap>();
    }

    /**
     * Gets a list of {@link MeasurementMap}s.
     * @return Measurement map collection
     */
    public List<MeasurementMap> getMeasurementMaps() {
        return this.measurementMaps;
    }

    /**
     * Sets {@link MeasurementMap} collection
     * @param measurementMaps Measurement map collection
     */
    public void setMeasurementMaps(List<MeasurementMap> measurementMaps) {
        this.measurementMaps = (measurementMaps != null)? new ArrayList<>(measurementMaps) : new ArrayList<MeasurementMap>();
    }

    /**
     * Gets a list of {@link MapState}s.
     * @return Map state collection
     */
    public List<MapState> getMapStates() {
        return this.mapStates;
    }

    /**
     * Sets {@link MapState} collection
     * @param mapStates Map state collection
     */
    public void setMapStates(List<MapState> mapStates) {
        this.mapStates = (mapStates != null)? new ArrayList<>(mapStates) : new ArrayList<MapState>();
    }

    public int getTotalUnitCount() {
        return totalUnitCount;
    }

    public void setTotalUnitCount(int totalUnitCount) {
        this.totalUnitCount = totalUnitCount;
    }

    public int getTotalMeasurementCount() {
        return totalMeasurementCount;
    }

    public void setTotalMeasurementCount(int totalMeasurementCount) {
        this.totalMeasurementCount = totalMeasurementCount;
    }

    public int getTotalUnitSystemCount() {
        return totalUnitSystemCount;
    }

    public void setTotalUnitSystemCount(int totalUnitSystemCount) {
        this.totalUnitSystemCount = totalUnitSystemCount;
    }

    public int getTotalUnitMapCount() {
        return totalUnitMapCount;
    }

    public void setTotalUnitMapCount(int totalUnitMapCount) {
        this.totalUnitMapCount = totalUnitMapCount;
    }

    public int getTotalMeasurementMapCount() {
        return totalMeasurementMapCount;
    }

    public void setTotalMeasurementMapCount(int totalMeasurementMapCount) {
        this.totalMeasurementMapCount = totalMeasurementMapCount;
    }

    public int getTotalMapStateCount() {
        return totalMapStateCount;
    }

    public void setTotalMapStateCount(int totalMapStateCount) {
        this.totalMapStateCount = totalMapStateCount;
    }
}
