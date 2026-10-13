package org.opengroup.osdu.unitservice.index;

import org.opengroup.osdu.unitservice.model.MeasurementDeprecationInfoImpl;
import org.opengroup.osdu.unitservice.model.MeasurementEssenceImpl;
import org.opengroup.osdu.unitservice.model.MeasurementImpl;
import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.model.CatalogImpl;

import java.util.List;

/**
 * A converter to do conversion between {@link MeasurementImpl} and {@link IndexRow}.
 */
public class MeasurementConverter {

    /**
     * Converts the measurement, {@link MeasurementImpl}, to indexed row, {@link IndexRow}.
     * @param measurement measurement object
     * @return indexed row
     */
    public static IndexRow toIndexRow(MeasurementImpl measurement) {
        IndexRow indexRow = new IndexRow(measurement.getInternalID(), measurement.getEssence().toJsonString(), LuceneConstants.Type_Measurement);
        indexRow.addFieldValue(LuceneConstants.Ancestry, measurement.getEssence().getAncestry());
        indexRow.addFieldValue(LuceneConstants.Code, measurement.getCode());
        indexRow.addFieldValue(LuceneConstants.DimensionCode, measurement.getDimensionCode());
        indexRow.addFieldValue(LuceneConstants.UnitQuantityCode, measurement.getUnitQuantityCode());
        indexRow.addFieldValue(LuceneConstants.DimensionAnalysis, measurement.getDimensionAnalysis());
        indexRow.addFieldValue(LuceneConstants.Name, measurement.getName());
        indexRow.addFieldValue(LuceneConstants.Namespace, measurement.getNamespace());
        if(measurement.isBaseMeasurement()) {
            indexRow.addFieldValue(LuceneConstants.BaseMeasurement, "True Yes");
        }
        else {
            indexRow.addFieldValue(LuceneConstants.BaseMeasurement, "False No");
        }

        indexRow.addCommonFieldValue(measurement.getLastModified());
        indexRow.addCommonFieldValue(measurement.getDescription());
        List<String> childMeasurements = measurement.getChildMeasurementEssenceJsons();
        if(childMeasurements != null && childMeasurements.size() > 0) {
            for (String childMeasurement : childMeasurements) {
                //TODO: Decode the child measurement and index its content
                indexRow.addCommonFieldValue(childMeasurement);
            }
        }

        List<String> units = measurement.getUnitEssenceJsons();
        if(units != null && units.size() > 0) {
            for (String unit : units) {
                //TODO Decode the unit and index its content
                indexRow.addCommonFieldValue(unit);
            }
        }

        List<String> preferredUnits = measurement.getPreferredUnitEssenceJsons();
        if(preferredUnits != null && preferredUnits.size() > 0) {
            for (String unit : preferredUnits) {
                //TODO Decode the unit and index its content
                indexRow.addCommonFieldValue(unit);
            }
        }

        MeasurementDeprecationInfoImpl deprecationInfo = measurement.getDeprecationInfo();
        if(deprecationInfo != null) {
            indexRow.addFieldValue(LuceneConstants.State, deprecationInfo.getState());
            indexRow.addCommonFieldValue(deprecationInfo.getRemarks());
            indexRow.addCommonFieldValue(deprecationInfo.getSupersededByUnitMeasurement());
        }

        return indexRow;
    }

    /**
     * Resolves the measurement object based on the indexed row's reference using the measurement helper,
     * {@link MeasurementHelper}.
     * @param indexRow indexed row
     * @param helper measurement helper
     * @return the measurement object that the indexed row refers to.
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>{@link #isSupported(IndexRow)} returns false.</li>
     *     <li>The measurement object can not be resolved.</li>
     * </ol>
     */
    public static MeasurementImpl toMeasurement(IndexRow indexRow, CatalogImpl catalogImpl) throws Exception {
        if(!isSupported(indexRow))
            throw new IllegalArgumentException("Unexpected type");
        MeasurementEssenceImpl essence = Utility.fromJsonString(indexRow.toJsonString(), MeasurementEssenceImpl.class);
        return catalogImpl.postMeasurement(essence);
    }

    /**
     * Checks whether the indexed row refers to a measurement object
     * @param indexRow indexed row
     * @return true if the indexed row refers to a measurement object; otherwise, return false.
     */
    public static boolean isSupported(IndexRow indexRow) {
        return indexRow.toJsonString() != null && LuceneConstants.Type_Measurement.equals(indexRow.getType());
    }

}
