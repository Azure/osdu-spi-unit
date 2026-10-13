package org.opengroup.osdu.unitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.unitservice.interfaces.MeasurementDeprecationInfo;
import org.opengroup.osdu.unitservice.helper.Utility;

import java.util.Map;

/**
 * A class declaring the owner/container as deprecated.
 * The properties provide additional information about the status and potential recommended {@link MeasurementImpl}s.
 */
@Schema(description = "A deprecation information record for a measurement.")
public class MeasurementDeprecationInfoImpl implements MeasurementDeprecationInfo {

    @Expose @SerializedName("state")
    @Schema(description = "The deprecation state - one of the following: 'identical', 'corrected', 'precision', 'conversion', 'conditional', 'unsupported', 'different', 'unresolved'.",type = "string")
    private String state;

    @Expose @SerializedName("remarks")
    @Schema(description = "Optional additional remarks about this deprecation.",type = "string")
    private String remarks;

    @Expose @SerializedName("supersededByUnitMeasurementID")
    @Schema(description = "If defined, the essence Json string identifying the unit superseding the deprecated unit.",type = "string")
    private String supersededByUnitMeasurementID;

    private MeasurementImpl supersededByUnitMeasurement;

    /*
    Constructor for an empty instance.
     */
    public MeasurementDeprecationInfoImpl() { }

    void postDeserialization(Map<String, MeasurementImpl> idMeasurements) {
        // It is valid that the supersededByUnitMeasurementID is null
        if(Utility.isNullOrEmpty(supersededByUnitMeasurementID))
            return;

        if(!idMeasurements.containsKey(supersededByUnitMeasurementID))
            throw new IllegalArgumentException("Superseded measurement id '" + supersededByUnitMeasurementID
                    + " does not have measurement definition associated.");

        supersededByUnitMeasurement = idMeasurements.get(supersededByUnitMeasurementID);
    }

    /**
     * Gets the deprecation state. The well-known states include:
     * <ul>
     *     <li>identical</li>
     *     <li>precision</li>
     *     <li>corrected</li>
     *     <li>conversion</li>
     *     <li>conditional</li>
     *     <li>unsupported</li>
     *     <li>different</li>
     *     <li>unresolved</li>
     * </ul>
     * @return  deprecation state
     */
    public String getState() { return this.state;}

    /**
     * Sets the deprecation state.
     * @param state deprecation state
     */
    public void setState(String state) { this.state = state; }

    /**
     * Gets further remarks about the deprecation reason.
     * @return remark of the deprecation
     */
    public String getRemarks() { return this.remarks; }

    /**
     * Sets further remarks about the deprecation reason.
     * @param remarks remark of the deprecation
     */
    public void setRemarks(String remarks) { this.remarks = remarks; }

    /**
     * Gets the persistable reference of {@link MeasurementImpl} superseding the owner/container measurement.
     * @return persistable reference of the measurement superseding the owner/container measurement
     */
    public String getSupersededByUnitMeasurement() {
        if(supersededByUnitMeasurement != null)
            return this.supersededByUnitMeasurement.getEssence().toJsonString();
        return null;
    }

    @Override
    public String toString() {
        return "P-DeprecationInfo " + this.state;
    }
}
