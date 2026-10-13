package org.opengroup.osdu.unitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.unitservice.interfaces.UnitMapItem;

import java.util.Map;

/**
 * A class defines the mapping state between two {@link UnitImpl}s
 */
@Schema(description = "The definition of a uni-directional unit-to-unit map pair")
public class UnitMapItemImpl implements UnitMapItem {

    @Expose @SerializedName("fromUnitID")
    @Schema(type = "string")
    private String fromUnitId;

    @Expose @SerializedName("toUnitID")
    @Schema(type = "string")
    private String toUnitId;

    @Expose @SerializedName("state")
    @Schema(description ="The state of the mapping; one of the following: 'identical', 'corrected', 'precision', 'conversion', 'conditional', 'unsupported', 'different', 'unresolved'.", type = "string")
    private String state;

    @Expose @SerializedName("note")
    @Schema(description = "Any additional remarks and notes about this mapping",type = "string")
    private String note;

    private UnitImpl fromUnit;

    private UnitImpl toUnit;

    private String fromNamespace;

    private String toNamespace;

    /**
     * Empty constructor
     */
    public UnitMapItemImpl() {}

    /**
     * Constructor
     * @param fromUnit from unit
     * @param toUnit to unit
     * @param state state
     * @param note note or remark
     */
    public UnitMapItemImpl(UnitImpl fromUnit, UnitImpl toUnit, String state, String note) {
        this.fromUnit = fromUnit;
        this.toUnit = toUnit;
        this.state = state;
        this.note = note;
    }

    /**
     * Gets the from {@link UnitImpl}.
     * @return  from unit
     */
    public UnitImpl getFromUnit() {
        return fromUnit;
    }

    /**
     * Gets the to {@link UnitImpl}.
     * @return  to unit
     */
    public UnitImpl getToUnit() {
        return toUnit;
    }

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
     * Gets the mapping state
     * @return  mapping state
     */
    public String getState() { return this.state; }

    /**
     * Gets the note or remark of the mapping reason
     * @return  note or remark
     */
    public String getNote() { return this.note; }

    /**
     * Post deserialization to resolve the units based on their Ids
     * @param idUnits
     */
    void postDeserialization(Map<String, UnitImpl> idUnits, String fromNamespace, String toNamespace) {
        if(!idUnits.containsKey(fromUnitId))
            throw new IllegalArgumentException("Unit id '" + fromUnitId + "' does not have unit definition associated.");
        if(!idUnits.containsKey(toUnitId))
            throw new IllegalArgumentException("Unit id '" + toUnitId + "' does not have unit definition associated.");

        this.fromUnit = idUnits.get(fromUnitId);
        this.toUnit = idUnits.get(toUnitId);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }
}
