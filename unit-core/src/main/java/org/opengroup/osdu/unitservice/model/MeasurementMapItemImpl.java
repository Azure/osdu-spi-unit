package org.opengroup.osdu.unitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.unitservice.interfaces.MeasurementMapItem;

import java.util.Map;
import java.util.logging.Logger;

/**
 * A class defines the mapping state between two {@link MeasurementImpl}s
 */
@Schema(description = "The definition of a uni-directional measurement-to-measurement map pair.")
public class MeasurementMapItemImpl implements MeasurementMapItem {
    private static final Logger log = Logger.getLogger( MeasurementMapItemImpl.class.getName() );

    @Expose @SerializedName("fromMeasurementID")
    @Schema(description = "The full definition of a measurement (base measurement/dimension or child measurement).",type = "string")
    private String fromMeasurementId;

    @Expose @SerializedName("toMeasurementID")
    @Schema(description = "The full definition of a measurement (base measurement/dimension or child measurement)",type = "string")
    private String toMeasurementId;

    @Expose @SerializedName("state")
    @Schema(description = "The state of the mapping; one of the following: 'identical', 'corrected', 'precision', 'conversion', 'conditional', 'unsupported', 'different', 'unresolved'.",type = "string")
    private String state;

    @Expose @SerializedName("note")
    @Schema(description = "Any additional remarks and notes about this mapping.",type = "string")
    private String note;

    private MeasurementImpl fromMeasurement;

    private MeasurementImpl toMeasurement;

    private String fromNamespace;

    private String toNamespace;

    /**
     * Constructor
     */
    public MeasurementMapItemImpl() {}

    /**
     * Constructor
     * @param fromMeasurement from measurement
     * @param toMeasurement to measurement
     * @param state state
     * @param note note or remark
     */
    public MeasurementMapItemImpl(MeasurementImpl fromMeasurement, MeasurementImpl toMeasurement, String state, String note) {
        this.fromMeasurement = fromMeasurement;
        this.toMeasurement = toMeasurement;
        this.state = state;
        this.note = note;
    }

    /**
     * Gets the from{@link MeasurementImpl}.
     * @return  fromMeasurement
     */
    public MeasurementImpl getFromMeasurement() { return this.fromMeasurement; }

    /**
     * Gets the to{@link MeasurementImpl}.
     * @return  toMeasurement
     */
    public MeasurementImpl getToMeasurement() { return this.toMeasurement; }

    /**
     * Gets the 'from' namespace
     *
     * @return The 'from' namespace
     */
    public String getFromNamespace() {
        return this.fromNamespace;
    }

    /**
     * Gets the 'to' namespace
     *
     * @return The 'to' namespace
     */
    public String getToNamespace() {
        return this.toNamespace;
    }

    /**
     * Gets the state definition
     * @return  state definition
     */
    public String getState() { return this.state; }

    /**
     * Gets the note of the mapping
     * @return  note of the mapping
     */
    public String getNote() { return this.note; }

    /**
     * Post deserialization to resolve the measurements based on their Ids
     * @param idMeasurements
     */
    void postDeserialization(Map<String, MeasurementImpl> idMeasurements, String fromNamespace, String toNamespace) {
        if(!idMeasurements.containsKey(fromMeasurementId))
            throw new IllegalArgumentException("Measurement id '" + fromMeasurementId + "' does not have measurement definition associated.");
        if(!idMeasurements.containsKey(toMeasurementId))
            throw new IllegalArgumentException("Measurement id '" + toMeasurementId + "' does not have measurement definition associated.");

        fromMeasurement = idMeasurements.get(fromMeasurementId);
        toMeasurement = idMeasurements.get(toMeasurementId);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }
}
