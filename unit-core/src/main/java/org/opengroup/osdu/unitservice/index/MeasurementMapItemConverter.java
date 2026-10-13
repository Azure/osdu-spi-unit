package org.opengroup.osdu.unitservice.index;

import org.opengroup.osdu.unitservice.model.MeasurementEssenceImpl;
import org.opengroup.osdu.unitservice.model.MeasurementImpl;
import org.opengroup.osdu.unitservice.model.MeasurementMapImpl;
import org.opengroup.osdu.unitservice.model.MeasurementMapItemImpl;
import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.model.CatalogImpl;

/**
 *  A converter to do conversion between {@link MeasurementMapItemImpl} and {@link IndexRow}.
 */
public class MeasurementMapItemConverter {
    private static final String Delimiter = "<>";

    /**
     * Converts the measurement map item, {@link MeasurementMapItemImpl}, to indexed row, {@link IndexRow}.
     * @param mapItem measurement map item
     * @return indexed row
     */
    public static IndexRow toIndexRow(MeasurementMapItemImpl mapItem)  {
        MeasurementImpl fromMeasurement = mapItem.getFromMeasurement();
        MeasurementImpl toMeasurement = mapItem.getToMeasurement();
        String id = fromMeasurement.getInternalID() + Delimiter + toMeasurement.getInternalID();
        String reference = fromMeasurement.getEssence().toJsonString() + Delimiter + toMeasurement.getEssence().toJsonString();
        IndexRow indexRow = new IndexRow(id, reference, LuceneConstants.Type_MeasurementMapItem);

        String namespace = fromMeasurement.getNamespace() + " " + toMeasurement.getNamespace();
        indexRow.addFieldValue(LuceneConstants.Namespace, namespace);
        indexRow.addFieldValue(LuceneConstants.State, mapItem.getState());
        indexRow.addCommonFieldValue(mapItem.getNote());

        addMeasurementProperties(indexRow, fromMeasurement);
        addMeasurementProperties(indexRow, toMeasurement);

        return indexRow;
    }

    private static void addMeasurementProperties(IndexRow indexRow, MeasurementImpl measurement) {
        IndexRow measurementRow = MeasurementConverter.toIndexRow(measurement);

        for (String propertyValue : measurementRow.getFieldValuePairs().values()) {
            indexRow.addCommonFieldValue(propertyValue);
        }

        for (String propertyValue : measurementRow.getCommonFieldValues()) {
            indexRow.addCommonFieldValue(propertyValue);
        }
    }

    /**
     * Resolves the measurement map item based on the indexed row's reference using the measurement helper,
     * {@link MeasurementHelper}.
     * @param indexRow indexed row
     * @param helper measurement helper
     * @return the measurement map item that the indexed row refers to.
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>{@link #isSupported(IndexRow)} returns false.</li>
     *     <li>The measurement map item can not be resolved.</li>
     * </ol>
     */
    public static MeasurementMapItemImpl toMeasurementMapItem(IndexRow indexRow, CatalogImpl catalogImpl) throws Exception {
        if(!isSupported(indexRow))
            throw new IllegalArgumentException("Unexpected type");

        String[] essenceJsons = indexRow.toJsonString().split(Delimiter);
        if(essenceJsons == null || essenceJsons.length != 2)
            throw new IllegalArgumentException("Invalid reference format for UnitMapItem");
        MeasurementEssenceImpl fromEssence = Utility.fromJsonString(essenceJsons[0], MeasurementEssenceImpl.class);
        MeasurementEssenceImpl toEssence = Utility.fromJsonString(essenceJsons[1], MeasurementEssenceImpl.class);
        MeasurementImpl fromMeasurement = catalogImpl.postMeasurement(fromEssence);
        MeasurementImpl toMeasurement = catalogImpl.postMeasurement(toEssence);
        MeasurementMapImpl measurementMap = catalogImpl.getMeasurementMap(fromMeasurement.getNamespace(), toMeasurement.getNamespace());
        return measurementMap.getMeasurementMapItem(fromMeasurement, toMeasurement);
    }

    /**
     * Checks whether the indexed row refers to a measurement map item
     * @param indexRow indexed row
     * @return true if the indexed row refers to a measurement map item; otherwise, return false.
     */
    public static boolean isSupported(IndexRow indexRow) {
        return indexRow.toJsonString() != null && LuceneConstants.Type_MeasurementMapItem.equals(indexRow.getType());
    }

}
