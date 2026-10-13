package org.opengroup.osdu.unitservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.unitservice.interfaces.UnitMap;
import org.opengroup.osdu.unitservice.interfaces.UnitMapItem;
import org.opengroup.osdu.unitservice.helper.MapStateHelper;
import org.opengroup.osdu.unitservice.helper.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * An class defines mapping of {@link UnitImpl}s between two namespaces.
 */

@Schema(description = "A list of UnitMapItems given a 'from' and a 'to' namespace.")
public class UnitMapImpl implements UnitMap {
    private static final Logger log = Logger.getLogger( UnitMapImpl.class.getName() );

    @Expose @SerializedName("listOfMaps")
    @Schema(description = "The array of UnitMapItem")
    List<UnitMapItemImpl> mapItems;

    @Expose @SerializedName("fromNamespace")
    @Schema(description = "The source or 'from' namespace")
    private String fromNamespace;

    @Expose @SerializedName("toNamespace")
    @Schema(description = "The source or 'to' namespace")
    private String toNamespace;

    /**
     * Constructor
     */
    public UnitMapImpl() {
        mapItems = new ArrayList<>();
    }

    /**
     * Constructor
     * @param fromNamespace namespace of from unit
     * @param toNamespace namespace of to unit
     */
    public UnitMapImpl(String fromNamespace, String toNamespace) {
        this();
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    /**
     * Gets the number of the {@link UnitMapItemImpl}s
     * @return number of the {@link UnitMapItemImpl}s
     */
    public int getUnitMapItemCount() { return this.mapItems.size(); }

    /**
     * Gets the namespace of the from {@link UnitImpl}
     * @return namespace of the from {@link UnitImpl}
     */
    public String getFromNamespace() { return this.fromNamespace; }

    /**
     * Gets the namespace of the to {@link UnitImpl}
     * @return namespace of the to {@link UnitImpl}
     */
    public String getToNamespace() { return this.toNamespace; }

    /**
     * Gets the {@link UnitMapItem} collection defined in the map
     * @return {@link UnitMapItem} collection
     */
    public List<UnitMapItem> getUnitMapItems() {
        return new ArrayList<UnitMapItem>(mapItems);
    }

    /**
     * Add a {@link UnitMapItemImpl} to the map
     * @param unitMapItem unit map item
     */
    public void addUnitMapItem(UnitMapItemImpl unitMapItem) {
        if(unitMapItem == null || mapItems.contains(unitMapItem))
            return;

        if(unitMapItem.getFromUnit() == null || unitMapItem.getToUnit() == null)
            throw new IllegalArgumentException("Neither fromUnit nor toUnit can be null");

        if(Utility.isNullOrEmpty(unitMapItem.getState()))
            throw new IllegalArgumentException("State can not be null");

        if(!fromNamespace.equals(unitMapItem.getFromUnit().getNamespace()) ||
           !toNamespace.equals(unitMapItem.getToUnit().getNamespace()))
            throw new IllegalArgumentException("The namespaces of the fromUnit and toUnit must be consistent with the namespaces of the unit map.");

        mapItems.add(unitMapItem);
    }

    /**
     * This method is used by the search to resolve the UnitMapItem.
     * The from{@link UnitImpl} and to{@link UnitImpl} should exist in the catalog.
     * @param fromUnit from unit
     * @param toUnit  to unit
     * @return a unit mapped item
     * @throws IllegalArgumentException an exception will be thrown if there is not mapped item found.
     */
    @JsonIgnore
    public UnitMapItemImpl getUnitMapItem(UnitImpl fromUnit, UnitImpl toUnit) throws IllegalArgumentException {
        for (UnitMapItemImpl mapItem: mapItems) {
            if(mapItem.getFromUnit() == fromUnit &&
               mapItem.getToUnit() == toUnit)
                return mapItem;
        }

        throw new IllegalArgumentException("UnitMapItem between units " + fromUnit.getEssence().getSymbol() +
                " and " + toUnit.getEssence().getSymbol() + " is not found");
    }

    /**
     * Checks whether from{@link UnitImpl} and to{@link UnitImpl} is defined in the map as identical
     * @param fromUnit from unit
     * @param toUnit to unit
     * @return return true if there is a mapped item defined as identical or there is a {@link UnitMapItemImpl} with identical state
     * for fromUnit and the {@link UnitMapItemImpl#getToUnit()} has the same base measurement as the toUnit.
     */
    public boolean isMatch(UnitImpl fromUnit, UnitImpl toUnit) {
        if(fromUnit == null || fromUnit.getNamespace() == null ||
           toUnit == null || toUnit.getNamespace() == null)
            return false;

        if(!isNamespaceMatch(fromUnit, toUnit) &&
           !isNamespaceMatch(toUnit, fromUnit))
            return false;

        boolean forwardMatch = isNamespaceMatch(fromUnit, toUnit);
        if(!forwardMatch) {
            //Swap fromUnit with toUnit
            UnitImpl tmp = fromUnit;
            fromUnit = toUnit;
            toUnit = tmp;
        }

        List<UnitImpl> identicalToUnits = new ArrayList<>();
        for(UnitMapItemImpl unitMapItem : mapItems) {
            if(!fromUnit.equals(unitMapItem.getFromUnit()))
                continue;

            //If it is reverse match, then we need to verify the state that must be "identical"
            if(toUnit.equals(unitMapItem.getToUnit()) &&
              (forwardMatch || MapStateHelper.isIdentical(unitMapItem.getState())))
                return true;

            if(MapStateHelper.isIdentical(unitMapItem.getState()))
                identicalToUnits.add(unitMapItem.getToUnit());
        }

        MeasurementImpl toBaseMeasurement = toUnit.getEssence().getBaseMeasurement();
        for (UnitImpl identicalToUnit: identicalToUnits) {
            //If the base measurement of any identical unit matches the base measurement of the toUnit
            //Then it is considerred match
            MeasurementImpl baseMeasurement = identicalToUnit.getEssence().getBaseMeasurement();
            if(baseMeasurement.equals(toBaseMeasurement))
                return true;
        }

        return false;
    }

    private boolean isNamespaceMatch(UnitImpl fromUnit, UnitImpl toUnit) {
        return this.fromNamespace.equals(fromUnit.getNamespace()) &&
                this.toNamespace.equals(toUnit.getNamespace());
    }
}
