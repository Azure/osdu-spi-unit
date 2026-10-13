package org.opengroup.osdu.unitservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.opengroup.osdu.unitservice.interfaces.Unit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.logging.Logger;

/**
 * The class describing a unit of measure, {@link MeasurementImpl}.
 */

@Schema(description = "The full description of a unit")
public class UnitImpl implements Unit {
    private static final Logger log = Logger.getLogger( UnitImpl.class.getName() );

    @Expose @SerializedName("id")
    @Schema(type = "string")
    private String id;

    @Expose @SerializedName("essence")
    @Schema(description = "The essence of a unit parameterization",implementation = UnitEssenceImpl.class)
    private UnitEssenceImpl essence;

    @Expose @SerializedName("deprecationInfo")
    @Schema(implementation = UnitDeprecationInfoImpl.class)
    private UnitDeprecationInfoImpl deprecationInfo;

    @Expose @SerializedName("displaySymbol")
    @Schema(description = "The unit symbol or unit abbreviation",type = "string")
    private String displaySymbol;

    @Expose @SerializedName("name")
    @Schema(description = "The full name of this unit",type = "string")
    private String name;

    @Expose @SerializedName("description")
    @Schema(description = "Any additional remarks about this unit or blank.",type = "string")
    private String description;

    @Expose @SerializedName("lastModified")
    @Schema(description = "The last modification date of this unit in the format YYYYMMDD",type = "string")
    private String lastModified;

    @Expose @SerializedName("source")
    @Schema(description = "The source of this unit definition",type = "string")
    private String source;

    @Expose @SerializedName("namespace")
    @Schema(description = "The namespace, in which this unit's symbol is unique. Example namespaces: 'Energistics_UoM', 'RP66', 'ECL', 'LIS'.",type = "string")
    private String namespace;

    /**
    The constructor for an empty unit instance.
     */
    public UnitImpl() {
        essence = new UnitEssenceImpl();
    }

    /**
     * Gets the internal id of the unit
     * @return internal id
     */
    @JsonIgnore
    public String getInternalID() {
        return this.id;
    }

    /********************************************
     public methods
     *********************************************/
    /**
     * Gets the unit's display symbol; it may contain unicode symbols.
     * @return unit display symbol
     */
    public String getDisplaySymbol() { return this.displaySymbol; }

    /**
     * Sets the unit's display symbol; it may contain unicode symbols.
     * @param displaySymbol unit display symbol
     */
    public void setDisplaySymbol(String displaySymbol) { this.displaySymbol = displaySymbol;}

    /**
     * Gets the unit's name.
     * @return unit's name
     */
    public String getName() { return this.name; }

    /**
     * Sets the unit's name.
     * @param name unit's name
     */
    public void setName(String name) { this.name = name;}

    /**
     * Gets the unit's description string.
     * @return unit's description
     */
    public String getDescription() { return this.description; }

    /**
     * Sets the unit's description string.
     * @param description unit's description
     */
    public void setDescription(String description) { this.description = description;}

    /**
     * Gets the unit's last modification date in short form as string YYYYMMDD.
     * @return unit's last modification date in short form as string YYYYMMDD
     */
    public String getLastModified() { return this.lastModified; }

    /**
     * Gets or sets the unit's last modification date in short form as string YYYYMMDD.
     * @param lastModified unit's last modification date in short form as string YYYYMMDD
     */
    public void setLastModified(String lastModified) { this.lastModified = lastModified;}

    /**
     * Gets the source of the unit
     * @return source of the unit
     */
    public String getSource() { return this.source; }

    /**
     * Sets the source of the unit
     * @param source source of the unit
     */
    public void setSource(String source) { this.source = source;}

    /**
     * Gets the unit namespace; It is 'Energistics_UoM' for Energistics standard unitReferences;
     * other SLB unitReferences can come in different legacy namespaces.
     * @return unit namespace
     */
    public String getNamespace() { return this.namespace; }

    /**
     * Sets the unit namespace; It is 'Energistics_UoM' for Energistics standard unitReferences;
     * other SLB unitReferences can come in different legacy namespaces.
     * @param namespace unit namespace
     */
    public void setNamespace(String namespace) { this.namespace = namespace; }

    /**
     * Gets the {@link UnitEssenceImpl}, the characteristic unit properties.
     * @return UnitEssence object
     */
    public UnitEssenceImpl getEssence() { return this.essence; }

    /**
     * Sets the {@link UnitEssenceImpl}, the characteristic unit properties.
     * @param essence UnitEssence object
     */
    public void setEssence(UnitEssenceImpl essence) { this.essence = essence; }

    /**
     * Gets the {@link UnitEssenceImpl}, the characteristic unit properties as JSON string, aka persistable reference.
     * @return The persistable reference string
     */
    @JsonProperty("persistableReference")
    public String getEssenceJson() { return this.essence.toJsonString(); }

    /**
     * Gets the {@link UnitDeprecationInfoImpl}; non-null properties mean the unit is deprecated.
     * @return UnitDeprecationInfo object
     */
    public UnitDeprecationInfoImpl getDeprecationInfo() { return this.deprecationInfo; }

    /**
     * Sets the {@link UnitDeprecationInfoImpl}; non-null properties mean the unit is deprecated.
     * @param deprecationInfo UnitDeprecationInfo object
     */
    public void setDeprecationInfo(UnitDeprecationInfoImpl deprecationInfo) { this.deprecationInfo = deprecationInfo; }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof UnitImpl))
            return false;

        UnitImpl other = (UnitImpl) o;
        return this == other || this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        return essence.hashCode();
    }


}
