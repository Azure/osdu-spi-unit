package org.opengroup.osdu.unitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.unitservice.interfaces.UnitAssignment;

import java.util.Map;

/**
 * A {@link UnitAssignmentImpl} assigns a {@link UnitImpl} to a {@link MeasurementImpl} for a given {@link UnitSystemImpl}
 */
@Schema(description = "A unit to measurement assignment in the conetxt of a unit system.")
public class UnitAssignmentImpl implements UnitAssignment {

    @Expose @SerializedName("measurementID")
    @Schema(type = "string")
    private String measurementId;

    @Expose @SerializedName("unitID")
    @Schema(type = "string")
    private String unitId;

    @Expose @SerializedName("lastModified")
    @Schema(description = "The last modification date of the assignment in the format YYYYMMDD.", type = "string")
    private String lastModified;

    private MeasurementImpl measurement;

    private UnitImpl unit;

    /*
    Constructor of an empty instances.
     */
    public UnitAssignmentImpl() {}

    /**
     * Constructor
     * @param measurement measurement
     * @param unit unit
     */
    public UnitAssignmentImpl(MeasurementImpl measurement, UnitImpl unit) {
        this.measurement = measurement;
        this.unit = unit;
    }

    /**
     * Gets the {@link MeasurementImpl}
     * @return measurement instance
     */
    public MeasurementImpl getMeasurement() { return this.measurement; }

    /**
     * Gets the {@link UnitImpl}
     * @return unit instance
     */
    public UnitImpl getUnit() { return this.unit; }

    /**
     * Gets the last modification date in short form as string YYYYMMDD.
     * @return last modification date in short form as string YYYYMMDD.
     */
    public String getLastModified() { return this.lastModified; }

    /**
     * Sets the last modification date in short form as string YYYYMMDD.
     * @param lastModified last modification date in short form as string YYYYMMDD.
     */
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Post deserialization to resolve the measurement and unit based on their Ids
     * @param idMeasurements
     * @param idUnits
     */
    void postDeserialization(Map<String, MeasurementImpl> idMeasurements, Map<String, UnitImpl> idUnits) {
        if(!idMeasurements.containsKey(measurementId))
            throw new IllegalArgumentException("Measurement id '" + measurementId + "' does not have measurement definition associated.");
        if(!idUnits.containsKey(unitId))
            throw new IllegalArgumentException("Unit id '" + unitId + "' does not have unit definition associated.");

        measurement = idMeasurements.get(measurementId);
        unit = idUnits.get(unitId);
    }
}
