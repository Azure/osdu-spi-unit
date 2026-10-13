package org.opengroup.osdu.unitservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.unitservice.interfaces.UnitAssignment;
import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.interfaces.UnitSystem;
import org.opengroup.osdu.unitservice.model.extended.UnitSystemEssenceImpl;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * A class describing a unit system, i.e. a set of {@link UnitImpl}s assigned to {@link MeasurementImpl}s.
 */
@Schema(description = "A unit system definition with containing unit-to-measurement assignments")
public class UnitSystemImpl implements UnitSystem {
    @Expose @SerializedName("name")
    @Schema(description = "The name of this unit system",type = "string")
    private String name;

    @Expose @SerializedName("description")
    @Schema(description = "Any further description of this unit system",type = "string")
    private String description;

    @Expose @SerializedName("referenceUnitSystem")
    @Schema(description = "The unit system code, from which this unit system is derived - or a blank string.",type = "string")
    private String referenceUnitSystem;

    @Expose @SerializedName("ancestry")
    @Schema(description = "The full ancestry of this unit system",type = "string")
    private String ancestry;

    @Expose @SerializedName("lastModified")
    @Schema(description = "The last modification of this unit system core properties formatted as YYYYMMDD",type = "string")
    private String lastModified;

    @Expose @SerializedName("source")
    @Schema(description = "Source of the unit system definition",type = "string")
    private String source;

    @Expose @SerializedName("unitAssignmentCountInResponse")
    @Schema(type = "integer")
    private int countInResponse;

    private int countInTotal;
    private int offset;

    @Expose @SerializedName("listOfUnitAssignments")
    private List<UnitAssignmentImpl> unitAssignments;

    private UnitSystemEssenceImpl essence;

    private UnitSystemImpl parent;

    @JsonIgnore
    private boolean exportCopy;
    /**
     * Empty constructor
     */
    public UnitSystemImpl() {
        unitAssignments = new ArrayList<>();
        exportCopy = false;
        offset = 0;
    }

    /**
     * Create a (partial) copy of a unit system with a sub-set of unit assignments (offset, limit)
     * @param offset The offset into the array of unit assignments starting at 0
     * @param limit The maximum number of unit assignments in the response (-1 for all).
     * @return An exportable copy of this unit system or this unit system if offset=0, limit=-1.
     */
    public UnitSystemImpl getCopyByOffsetAndLimit(int offset, int limit){
        if (offset == 0 && limit == -1) {
            this.countInTotal = this.getUnitAssignments().size();
            return this;
        }
        int actual_offset = max(0, offset);
        UnitSystemImpl unitSystemCopy = new UnitSystemImpl();
        unitSystemCopy.offset = actual_offset;
        unitSystemCopy.exportCopy = true;
        unitSystemCopy.name = this.name;
        unitSystemCopy.ancestry = this.ancestry;
        unitSystemCopy.description = this.description;
        unitSystemCopy.essence = this.essence;
        unitSystemCopy.lastModified = this.lastModified;
        unitSystemCopy.parent = this.parent;
        unitSystemCopy.referenceUnitSystem = this.referenceUnitSystem;
        unitSystemCopy.source = this.source;
        List<UnitAssignment> ass = this.getUnitAssignments();
        int last = min(actual_offset+limit, ass.size());
        for (int i=actual_offset; i<last; i++) unitSystemCopy.unitAssignments.add((UnitAssignmentImpl) ass.get(i));
        unitSystemCopy.countInResponse = unitSystemCopy.unitAssignments.size();
        unitSystemCopy.countInTotal = ass.size();
        return unitSystemCopy;
    }

    public int getOffset() {
        return this.offset;
    }
    @JsonProperty("unitAssignmentCountInResponse")
    public int getUnitAssignmentCountInResponse(){
        return this.countInResponse;
    }
    @JsonProperty("unitAssignmentCountTotal")
    public int getUnitAssignmentCountTotal()
    {
        return this.countInTotal;
    }
    /**
     * Gets the parent unit system
     * @return parent unit system. It is null if it is a root unit system.
     */
    @JsonIgnore
    public UnitSystemImpl getParent() { return this.parent; }

    /**
     * Sets the parent unit system
     * @param parent parent unit system.
     */
    public void setParent(UnitSystemImpl parent) {
        //FIXME: potential inconsistent with Ancestry
        this.parent = parent;

        //reset the measurementUnitAssignments in order to rebuild it
        measurementUnitAssignments = null;
    }

    /**
     * Gets the number of {@link UnitAssignment}s.
     * @return number of unit assignments
     */
    @JsonIgnore
    public int getUnitAssignmentCount() {
        return getMeasurementUnitAssignments().size();
    }

    /**
     * Gets the persistable reference string (JSON serialized essence)
     * @return the persistable reference string
     */
    @JsonProperty("persistableReference")
    public String getPersistableReference() {
        if (this.getEssence() != null) return this.getEssence().toJsonString();
        return null;
    }
    /**
     * Gets the name of the unit system.
     * @return  name of the unit system.
     */
    public String getName() { return this.name; }

    /**
     * Sets the name of the unit system.
     * @param name  name of the unit system.
     */
    public void setName(String name) { this.name = name; }

    /**
     *  Gets the description of the unit system
     * @return  description of the unit system
     */
    public String getDescription() { return this.description; }

    /**
     * Gets the description of the unit system
     * @param description description of the unit system
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Gets the name of the parent {@link UnitSystemImpl}.
     * This instance overrides and extends the definitions.
     * An empty "ReferenceUnitSystem" indicates a root {@link UnitSystemImpl}.
     * @return name of the parent unit system.
     */
    public String getReferenceUnitSystem() { return this.referenceUnitSystem; }

    /**
     * Sets the name of the parent {@link UnitSystemImpl}.
     * This instance overrides and extends the definitions.
     * An empty "ReferenceUnitSystem" indicates a root {@link UnitSystemImpl}.
     * @param referenceUnitSystem name of the parent unit system.
     */
    public void setReferenceUnitSystem(String referenceUnitSystem) {
        this.referenceUnitSystem = referenceUnitSystem;
    }

    /**
     * Gets the ancestry of the unit system.
     * @return ancestry of the unit system
     */
    public String getAncestry() {
        return ancestry;
    }

    /**
     * Sets the ancestry of the unit system.
     * @param ancestry ancestry of the unit system
     */
    public void setAncestry(String ancestry) {
        this.ancestry = ancestry;
        essence = new UnitSystemEssenceImpl(this.ancestry);
    }

    /**
     * Gets the Json string of the unit system.
     * @return  Json string
     */
    public String toJsonString() {
        return getEssence().toJsonString();
    }

    /***
     * Gets a short string representation of the date in the form YYYYMMDD enabling simple comparisons.
     * @return  the last update time in the form YYYYMMDD
     */
    public String getLastModified() { return this.lastModified; }

    /**
     * Sets a short string representation of the date in the form YYYYMMDD enabling simple comparisons.
     * @param lastModified the last update time in the form YYYYMMDD
     */
    public void setLastModified(String lastModified) { this.lastModified = lastModified; }

    /**
     * Gets the source of the unit system.
     * @return  source of the unit system.
     */
    public String getSource() { return this.source; }

    /**
     * Sets the source of the unit system.
     * @param source of the unit system.
     */
    public void setSource(String source) { this.source = source; }

    /**
     * Gets the {@link UnitAssignmentImpl} for given persistable reference of the {@link MeasurementImpl}
     * @param measurement the measurement.
     * @return unit assignment
     * @throws IllegalArgumentException an exception will be thrown if any of the following condition is met:
     * <ol>
     *     <li>The given measurementReference is null or empty</li>
     *     <li>There is not {@link UnitAssignmentImpl} for the given measurementReference in the unit system</li>
     * </ol>
     */
    public UnitAssignmentImpl getUnitAssignment(MeasurementImpl measurement) throws IllegalArgumentException {
        if(measurement == null)
            throw new IllegalArgumentException("The measurement can not be null");

        Map<MeasurementImpl, UnitAssignmentImpl> map = getMeasurementUnitAssignments();
        while(measurement != null && !map.containsKey(measurement)) {
            //Try the parent measurement
            //See Task 513514: https://tfs.slb.com/tfs/SLB1/DataAtRest/_workitems/edit/513514?fullScreen=false
            measurement = measurement.getParent();
        }
        if(measurement == null)
            throw new IllegalArgumentException("The measurement is not supported by the unit system '" + name + "'");

        return map.get(measurement);
    }

    /**
     * Gets a native list of {@link UnitAssignment}s without inherited assignments
     * @return a native list of unit assignments
     */
    @JsonIgnore
    public List<UnitAssignment> getNativeUnitAssignments() {
        return new ArrayList<UnitAssignment>(this.unitAssignments);
    }

    /**
     * Adds unit assignment for the unit system.
     * @param unitAssignment unit assignment
     */
    public void addNativeUnitAssignment(UnitAssignmentImpl unitAssignment) {
        if(unitAssignment == null || this.unitAssignments.contains(unitAssignment))
            return;

        if(unitAssignment.getMeasurement() == null)
            throw new IllegalArgumentException("The measurement in the assignment can not be null.");
        if(unitAssignment.getUnit() == null)
            throw new IllegalArgumentException("The unit in the assignment can not be null.");
        this.unitAssignments.add(unitAssignment);

        //reset the measurementUnitAssignments in order to rebuild it
        measurementUnitAssignments = null;
    }

    /**
     *  Gets a list of {@link UnitAssignment}s defined in the unit system(s).
     *  It includes the inherited assignments from its ancestry unit system(s).
     * @return a list of unit assignments
     */
    public List<UnitAssignment> getUnitAssignments() {
        Collection<UnitAssignmentImpl> ass;
        if (this.exportCopy) {
             ass = this.unitAssignments;

        } else {
            ass = getMeasurementUnitAssignments().values();
        }
        return new ArrayList<UnitAssignment>(ass);
    }

    @JsonIgnore
    private Map<MeasurementImpl, UnitAssignmentImpl> measurementUnitAssignments = null;
    Map<MeasurementImpl, UnitAssignmentImpl> getMeasurementUnitAssignments() {
        if(measurementUnitAssignments == null) {
            measurementUnitAssignments = new HashMap<>();
            for (UnitAssignmentImpl unitAssignment : unitAssignments) {
                measurementUnitAssignments.put(unitAssignment.getMeasurement(), unitAssignment);
            }

            if(parent != null) {
                Map<MeasurementImpl, UnitAssignmentImpl> inheritedUnitAssignments = parent.getMeasurementUnitAssignments();
                for (UnitAssignmentImpl unitAssignment: inheritedUnitAssignments.values()) {
                    // Inherit the parent assignment if the measurement does not exist in current unit assignment list
                    if(!measurementUnitAssignments.containsKey(unitAssignment.getMeasurement()))
                        measurementUnitAssignments.put(unitAssignment.getMeasurement(), unitAssignment);
                }
            }
        }
        return measurementUnitAssignments;
    }

    /**
     * Gets {@link UnitSystemEssenceImpl} of the unit system.
     * @return unit system essence.
     */
    @JsonIgnore
    public UnitSystemEssenceImpl getEssence() {
        if(essence == null)
            essence = new UnitSystemEssenceImpl(this.ancestry);
        return essence;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof UnitSystemImpl))
            return false;

        UnitSystemImpl other = (UnitSystemImpl) o;
        return this == other || this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        return getEssence().hashCode();
    }

    /**
     * Post deserialization to set the parent unit system
     * @param allUnitSystems
     */
    void postDeserialization(List<UnitSystemImpl> allUnitSystems) {
        if(Utility.isNullOrEmpty(this.referenceUnitSystem)) {
            //Root
            parent = null;
            return;
        }

        for (UnitSystemImpl unitSystem : allUnitSystems) {
            if (this.referenceUnitSystem.equals(unitSystem.getName())) {
                parent = unitSystem;
                break;
            }
        }
    }
}
