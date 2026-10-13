package org.opengroup.osdu.unitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.unitservice.interfaces.UnitDeprecationInfo;
import org.opengroup.osdu.unitservice.helper.Utility;

import java.util.Map;
import java.util.logging.Logger;

/**
 * A class declaring the owner/container as deprecated.
 * The properties provide additional information about the status and potential recommended {@link UnitImpl}s.
 */
@Schema(description = "The deprecation information record for a unit")
public class UnitDeprecationInfoImpl implements UnitDeprecationInfo {
    private static final Logger log = Logger.getLogger( UnitDeprecationInfoImpl.class.getName() );

    @Expose @SerializedName("state")
    @Schema(description = "The deprecation state - one of the following: 'identical', 'corrected', 'precision', 'conversion', 'conditional', 'unsupported', 'different', 'unresolved'.",type = "string")
    private String state;

    @Expose @SerializedName("remarks")
    @Schema(description = "Optional additional remarks about this deprecation",type = "string")
    private String remarks;

    @Expose @SerializedName("supersededByUnitID")
    @Schema(description = "If defined, the essence Json string identifying the unit superseding the deprecated unit.",type = "string")
    String supersededByUnitID;

    private UnitImpl supersededByUnit;

    /**
     * Constructor for an empty instance.
     */
    public UnitDeprecationInfoImpl() { }

    void postDeserialization(Map<String, UnitImpl> idUnits) {
        // It is valid that the supersededByUnitID is null
        if(Utility.isNullOrEmpty(supersededByUnitID))
            return;
        
        if(!idUnits.containsKey(supersededByUnitID))
            throw new IllegalArgumentException("Superseded unit id '" + supersededByUnitID +
                    " does not have unit definition associated.");

        supersededByUnit = idUnits.get(supersededByUnitID);
    }

    /**
     * Gets the deprecation state
     * @return deprecation state
     */
    public String getState() { return this.state;}

    /**
     * Sets the deprecation state
     * @param state deprecation state
     */
    public void setState(String state) { this.state = state; }

    /**
     * Gets further remarks about the deprecation reason.
     * @return remark of the deprecation reason.
     */
    public String getRemarks() { return this.remarks; }

    /**
     * Sets further remarks about the deprecation reason
     * @param remarks remark of the deprecation reason.
     */
    public void setRemarks(String remarks) { this.remarks = remarks; }

    /**
     * Gets the {@link UnitEssenceImpl#toJsonString()} of the unit superseding the owner/container unit.
     * @return Json string of the unit superseding the owner/container unit.
     */
    public String getSupersededByUnit() {
        if(supersededByUnit != null) {
            return this.supersededByUnit.getEssence().toJsonString();
        }
        return null;
    }
}
