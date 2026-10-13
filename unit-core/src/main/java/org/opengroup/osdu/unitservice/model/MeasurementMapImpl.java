package org.opengroup.osdu.unitservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.opengroup.osdu.unitservice.interfaces.MeasurementMap;
import org.opengroup.osdu.unitservice.interfaces.MeasurementMapItem;
import org.opengroup.osdu.unitservice.helper.MapStateHelper;
import org.opengroup.osdu.unitservice.helper.Utility;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

/**
 * An class defines mapping of {@link MeasurementImpl}s between two namespaces.
 */
@Schema(description = "A list of MeasurementMapItems given a 'from' and a 'to' namespace.")
public class MeasurementMapImpl implements MeasurementMap {
    @Expose @SerializedName("listOfMaps")
    @Schema(type = "array", implementation = MeasurementMapItemImpl.class)
    private List<MeasurementMapItemImpl> mapItems;

    @Expose @SerializedName("fromNamespace")
    @Schema(description = "The source or 'from' namespace",type = "string")
    private String fromNamespace;

    @Expose @SerializedName("toNamespace")
    @Schema(description = "The source or 'from' namespace",type = "string")
    private String toNamespace;

    /**
     * Constructor
     */
    public MeasurementMapImpl() {
        mapItems = new ArrayList<>();
    }

    /**
     * Constructor
     * @param fromNamespace namespace of from measurement
     * @param toNamespace namespace of to measurement
     */
    public MeasurementMapImpl(String fromNamespace, String toNamespace) {
        this();

        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    /**
     * Gets the number of {@link MeasurementMapItem}s.
     * @return number of {@link MeasurementMapItem}s
     */
    public int getMeasurementMapItemCount() { return this.mapItems.size();}

    /**
     * Gets the namespace of the from {@link MeasurementImpl}
     * @return namespace of the from {@link MeasurementImpl}
     */
    public String getFromNamespace() { return this.fromNamespace; }

    /**
     * Gets the namespace of the to {@link MeasurementImpl}
     * @return namespace of the to {@link MeasurementImpl}
     */
    public String getToNamespace() { return this.toNamespace; }

    /**
     * Gets a list of {@link MeasurementMapItem}s
     * @return list of {@link MeasurementMapItem}s
     */
    public List<MeasurementMapItem> getMeasurementMapItems() {
        return new ArrayList<MeasurementMapItem>(mapItems);
    }

    /**
     * Add a {@link MeasurementMapItemImpl} to the map
     * @param measurementMapItem measurement map item
     */
    public void addMeasurementMapItem(MeasurementMapItemImpl measurementMapItem) {
        if(measurementMapItem == null || mapItems.contains(measurementMapItem))
            return;

        if(measurementMapItem.getFromMeasurement() == null || measurementMapItem.getToMeasurement() == null)
            throw new IllegalArgumentException("Neither fromMeasurement nor toMeasurement can be null");

        if(Utility.isNullOrEmpty(measurementMapItem.getState()))
            throw new IllegalArgumentException("State can not be null");

        if(!fromNamespace.equals(measurementMapItem.getFromMeasurement().getNamespace()) ||
                !toNamespace.equals(measurementMapItem.getToMeasurement().getNamespace()))
            throw new IllegalArgumentException("The namespaces of the fromMeasurement and toMeasurement must be consistent with the namespaces of the unit map.");

        mapItems.add(measurementMapItem);
    }

    /**
     * This method is used by the search to resolve the {@link MeasurementMapItemImpl}.
     * The from{@link MeasurementImpl} and to{@link MeasurementImpl} should exist in the catalog.
     * @param fromMeasurement   from measurement
     * @param toMeasurement     to measurement
     * @return  a measurement mapped item.
     * @throws IllegalArgumentException an exception will be thrown if there is not mapped item found.
     */
    @JsonIgnore
    public MeasurementMapItemImpl getMeasurementMapItem(MeasurementImpl fromMeasurement, MeasurementImpl toMeasurement) throws IllegalArgumentException {
        for (MeasurementMapItemImpl mapItem: mapItems) {
            if(mapItem.getFromMeasurement() == fromMeasurement &&
               mapItem.getToMeasurement() == toMeasurement)
                return mapItem;
        }

        throw new IllegalArgumentException("MeasurementMapItem between measurements " + fromMeasurement.getEssence().getAncestry() +
                " and " + toMeasurement.getEssence().getAncestry() + " is not found");
    }

    /**
     * Checks whether from{@link MeasurementImpl} and to{@link MeasurementImpl} is defined in the map as identical
     * @param fromMeasurement from measurement
     * @param toMeasurement to measurement
     * @return return true if there is a {@link MeasurementMapItemImpl} defined as identical. otherwise, false.
     */
    public boolean isMatch(MeasurementImpl fromMeasurement, MeasurementImpl toMeasurement) {
        if(fromMeasurement == null || fromMeasurement.getNamespace() == null ||
           toMeasurement == null || toMeasurement.getNamespace() == null)
            return false;

        if(!isNamespaceMatch(fromMeasurement, toMeasurement) &&
           !isNamespaceMatch(toMeasurement, fromMeasurement))
                return false;

        boolean forwardMatch = isNamespaceMatch(fromMeasurement, toMeasurement);
        if(!forwardMatch) {
            //Swap fromMeasurement with toMeasurement
            MeasurementImpl tmp = fromMeasurement;
            fromMeasurement = toMeasurement;
            toMeasurement = tmp;
        }

        for(MeasurementMapItemImpl measurementMapItem : mapItems) {
            if(!fromMeasurement.equals(measurementMapItem.getFromMeasurement()))
                continue;

            //If it is reverse match, then we need to verify the state that must be "identical"
            if(toMeasurement.equals(measurementMapItem.getToMeasurement()) &&
               (forwardMatch || MapStateHelper.isIdentical(measurementMapItem.getState())))
                return true;
        }

        return false;
    }

    private boolean isNamespaceMatch(MeasurementImpl fromMeasurement, MeasurementImpl toMeasurement) {
        return this.fromNamespace.equals(fromMeasurement.getNamespace()) &&
               this.toNamespace.equals(toMeasurement.getNamespace());
    }
}
