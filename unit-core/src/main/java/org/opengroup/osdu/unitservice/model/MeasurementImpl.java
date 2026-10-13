package org.opengroup.osdu.unitservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.opengroup.osdu.unitservice.interfaces.Measurement;
import org.opengroup.osdu.unitservice.interfaces.MeasurementEssence;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A measurement, which is either a base {@link Measurement} or a child {@link Measurement}.
 */
@Schema(description = "The full definition of a measurement (base measurement/dimension or child measurement).")
public class MeasurementImpl implements Measurement {
    private static final Logger log = Logger.getLogger( MeasurementImpl.class.getName() );

    @Expose @SerializedName("id")
    @Schema(type = "string")
    private String id;

    @Expose @SerializedName("essence")
    @Schema(implementation = MeasurementEssenceImpl.class)
    private MeasurementEssenceImpl essence;

    @Expose @SerializedName("deprecationInfo")
    @Schema(implementation = MeasurementDeprecationInfoImpl.class)
    private MeasurementDeprecationInfoImpl deprecationInfo;

    @Expose @SerializedName("code")
    @Schema(description = "The measurement code",type = "string")
    private String code;

    @Expose @SerializedName("name")
    @Schema(description = "The name of the current measurement",type = "string")
    private String name;

    @Expose @SerializedName("description")
    @Schema(description = "A description string further describing the meaning and purpose of the current measurement",type = "string")
    private String description;

    @Expose @SerializedName("dimensionCode")
    @Schema(description = "The OSDD dimension code in case of 'SLB' or the dimension for namespace 'Energistics_UoM'.",type = "string")
    private String dimensionCode;

    @Expose @SerializedName("unitQuantityCode")
    @Schema(description = "The OSDD UnitQuantity code associated with this measurement - only meaningful for namespace 'SLB'.",type = "string")
    private String unitQuantityCode;

    @Expose @SerializedName("namespace")
    @Schema(description = "The namespace in which the current code is unique; typical values are 'SLB', 'Energistics_UoM'",type = "string")
    private String namespace;

    @Expose @SerializedName("lastModified")
    @Schema(type = "string")
    private String lastModified;

    @Expose @SerializedName("dimensionAnalysis")
    @Schema(description = "The date this measurement was last updated in the format YYYYMMDD.",type = "string")
    private String dimensionAnalysis;

    @Expose @SerializedName("childMeasurementIDs")
    @Schema(type = "array", implementation = String.class)
    private List<String> childMeasurementIds;

    @Expose @SerializedName("preferredUnitIDs")
    @Schema(type = "array", implementation = String.class)
    private List<String> preferredUnitIds;

    private List<UnitImpl> units;

    private List<UnitImpl> preferredUnits;
    
    private List<MeasurementImpl> childMeasurements;

    private MeasurementImpl parent;

    private boolean isBase;
    /*
    Constructor
     */
    public MeasurementImpl() {
        essence = new MeasurementEssenceImpl();
        this.childMeasurements = new ArrayList<>();
        this.units = new ArrayList<>();
        this.preferredUnits = new ArrayList<>();
        this.isBase = true;
    }

    public void setIsBaseMeasurement(boolean isBase) {
        this.isBase = isBase;
    }

    void postDeserialization(Map<String, MeasurementImpl> idMeasurements, Map<String, UnitImpl> idUnits) {
        if(this.childMeasurementIds != null) {
            for (String id : this.childMeasurementIds) {
                if(!idMeasurements.containsKey(id))
                    throw new IllegalArgumentException("Measurement id '" + id + "' does not have measurement definition associated.");

                MeasurementImpl chidMeasurement = idMeasurements.get(id);
                this.childMeasurements.add(chidMeasurement);
                chidMeasurement.setParent(this);
            }
        }

        if(this.preferredUnitIds != null) {
            for (String id : this.preferredUnitIds) {
                if(!idUnits.containsKey(id))
                    throw new IllegalArgumentException("Unit id '" + id + "' does not have unit definition associated.");

                UnitImpl preferredUnit = idUnits.get(id);
                this.preferredUnits.add(preferredUnit);
            }
        }
    }

    /**
     * Gets the internal id of the measurement.
     * @return  internal id.
     */
    @JsonIgnore
    public String getInternalID() {
        return this.id;
    }

    /**
     * Gets the parent measurement
     * @return parent measurement. It is null if it is a base measurement.
     */
    @JsonIgnore
    public MeasurementImpl getParent() { return this.parent; }

    /**
     * Sets the parent measurement
     * @param parent parent measurement.
     */
    public void setParent(MeasurementImpl parent) {
        //FIXME: potential inconsistent with Ancestry
        this.parent = parent;
    }

    /********************************************
     public methods
     *********************************************/

    /**
     * Gets the unique code, which identifies the {@link MeasurementImpl}
     * @return unique code
     */
    public String getCode() { return this.code; }

    /**
     * Sets the unique code, which identifies the {@link MeasurementImpl}
     * @param code unique code
     */
    public void setCode(String code) { this.code = code;}

    /**
     * Gets the human readable name of the {@link MeasurementImpl}
     * @return readable name
     */
    public String getName() { return this.name; }

    /**
     * Sets the human readable name of the {@link MeasurementImpl}
     * @param name readable name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the description of the {@link MeasurementImpl}
     * @return description of the measurement
     */
    public String getDescription() { return this.description; }

    /**
     * Sets the description of the {@link MeasurementImpl}
     * @param description description of the measurement
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Gets or sets the OSDD Unit_Dimension code.
     * @return OSDD Unit_Dimension code
     */
    public String getDimensionCode() { return this.dimensionCode; }

    /**
     * Sets the OSDD Unit_Dimension code.
     * @param dimensionCode OSDD Unit_Dimension code
     */
    public void setDimensionCode(String dimensionCode) { this.dimensionCode = dimensionCode; }

    /**
     * Gets the OSDD Unit_Quantity code.
     * @return OSDD Unit_Quantity code.
     */
    public String getUnitQuantityCode() { return this.unitQuantityCode; }

    /**
     * Sets the OSDD Unit_Quantity code.
     * @param unitQuantityCode OSDD Unit_Quantity code.
     */
    public void setUnitQuantityCode(String unitQuantityCode) { this.unitQuantityCode = unitQuantityCode; }

    /**
     * Gets the OSDD namespace; typical values are "SLB" and "Energistics_UoM".
     * @return namespace of the measurement.
     */
    public String getNamespace() { return this.namespace; }

    /**
     * Sets the OSDD namespace; typical values are "SLB" and "Energistics_UoM".
     * @param namespace namespace of the measurement.
     */
    public void setNamespace(String namespace) { this.namespace = namespace; }

    /**
     * Gets a short string representation of the date in the form YYYYMMDD enabling simple comparisons.
     * @return short string representation of the date in the form YYYYMMDD
     */
    public String getLastModified() { return this.lastModified; }

    /**
     * Sets a short string representation of the date in the form YYYYMMDD enabling simple comparisons.
     * @param lastModified short string representation of the date in the form YYYYMMDD
     */
    public void setLastModified(String lastModified) { this.lastModified = lastModified; }

    /**
     * Gets the {@link MeasurementEssenceImpl} of the {@link MeasurementImpl}
     * @return measurement essence
     */
    public MeasurementEssenceImpl getEssence() { return this.essence; }

    /**
     * Gets the {@link MeasurementEssence} as JSON string
     * @return persistable reference string
     */
    @JsonProperty("persistableReference")
    public String getEssenceJson() { return this.essence.toJsonString(); }

    /**
     * Sets the {@link MeasurementEssenceImpl} of the {@link MeasurementImpl}
     * @param essence measurement essence
     */
    public void setEssence(MeasurementEssenceImpl essence) { this.essence = essence; }

    /**
     *  Gets the Json string of the parent measurement. It is null for base measurement.
     * @return  Json string of the parent measurement.
     */
    public String getParentEssenceJson() {
        return essence.getParentEssenceJson();
    }

    /**
     *  Gets the Json string of the base measurement.
     * @return  Json string of the base measurement.
     */
    public String getBaseMeasurementEssenceJson() {
        return essence.getBaseMeasurementEssenceJson();
    }

    /**
     * Gets the dimension analysis.
     * @return  dimension analysis of the measurement.
     */
    public String getDimensionAnalysis() {
        return this.dimensionAnalysis;
    }

    /**
     * Sets the dimension analysis.
     * @param dimensionAnalysis dimension analysis of the measurement.
     */
    public void setDimensionAnalysis(String dimensionAnalysis) {
        this.dimensionAnalysis = dimensionAnalysis;
    }

    /**
     * Gets the {@link MeasurementDeprecationInfoImpl} of this {@link MeasurementImpl}.
     * Non-null values mark the measurement as deprecated.
     * @return measurement deprecation info
     */
    public MeasurementDeprecationInfoImpl getDeprecationInfo() { return this.deprecationInfo; }

    /**
     * Sets the {@link MeasurementDeprecationInfoImpl} of this {@link MeasurementImpl}.
     * Non-null values mark the measurement as deprecated.
     * @param deprecationInfo measurement deprecation info
     */
    public void setDeprecationInfo(MeasurementDeprecationInfoImpl deprecationInfo) { this.deprecationInfo = deprecationInfo; }

    /**
     * Gets the list of the essences of Child-{@link Measurement}s.
     * @return list of the child measurements' essences
     */
    public List<String> getChildMeasurementEssenceJsons() {
        List<String> childMeasurementEssenceJsons = new ArrayList<>();
        for (MeasurementImpl childMeasurement: childMeasurements) {
            childMeasurementEssenceJsons.add(childMeasurement.getEssence().toJsonString());
        }
        return childMeasurementEssenceJsons;
    }

    /**
     * Gets the list of preferred unit Json strings for the given, potentially specialized {@link MeasurementImpl}.
     * Null-lists mean that the preferred unit Json strings are derived from the parent {@link MeasurementImpl}
     * @return list of preferred unit Json strings
     */
    public List<String> getPreferredUnitEssenceJsons() {
        List<String> preferredUnitEssenceJsons = new ArrayList<>();
        for (UnitImpl unit : preferredUnits) {
            preferredUnitEssenceJsons.add(unit.getEssence().toJsonString());
        }
        return preferredUnitEssenceJsons;
    }

    /**
     * Gets the list of unit Json strings for the given, potentially specialized {@link MeasurementImpl}.
     * Null-lists mean that the preferred unit Json strings are derived from the parent {@link MeasurementImpl}
     * @return list of unit Json strings
     */
    public List<String> getUnitEssenceJsons() {
        List<String> unitEssenceJsons = new ArrayList<>();
        for (UnitImpl unit : units) {
            unitEssenceJsons.add(unit.getEssence().toJsonString());
        }
        return unitEssenceJsons;
    }

    /**
     * Gets the list of {@link UnitImpl} for the given, potentially specialized {@link MeasurementImpl}.
     * Null-lists mean that the preferred unitReferences are derived from the parent {@link MeasurementImpl}
     * @return list of units
     */
    @JsonIgnore
    public List<UnitImpl> getUnits() { return new ArrayList<>(units); }

    /**
     * Adds a {@link UnitImpl} to the {@link MeasurementImpl}. Null unit won't be added.
     * @param unit unit
     */
    public void addUnit(UnitImpl unit)  {
        if(unit != null)
            this.units.add(unit);
    }

    /**
     * Gets the list of preferred {@link UnitImpl} for the given, potentially specialized {@link MeasurementImpl}.
     * Null-lists mean that the preferred unit Json strings are derived from the parent {@link MeasurementImpl}
     * @return list of preferred units
     */
    @JsonIgnore
    public List<UnitImpl> getPreferredUnits() { return new ArrayList<>(preferredUnits); }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof MeasurementImpl))
            return false;

        MeasurementImpl other = (MeasurementImpl) o;
        return this == other || this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        return this.essence.hashCode();
    }

    /**
     * Returns whether the measurement is a base measurement or not
     * @return true if it's base measurement, false otherwise
     */
    @Override
    public boolean isBaseMeasurement() { return isBase;  }

    @Override
    public String toString() {
        if(isBaseMeasurement()) {
            return "P-BaseMeasurement " + getCode();
        }
        // Otherwise child measurement 
        return "P-ChildMeasurement " + getCode();
    }

}
