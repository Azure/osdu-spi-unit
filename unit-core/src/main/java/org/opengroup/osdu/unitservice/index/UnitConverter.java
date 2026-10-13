package org.opengroup.osdu.unitservice.index;

import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.model.CatalogImpl;
import org.opengroup.osdu.unitservice.model.UnitImpl;
import org.opengroup.osdu.unitservice.model.UnitDeprecationInfoImpl;
import org.opengroup.osdu.unitservice.model.UnitEssenceImpl;

/**
 * A converter to do conversion between {@link UnitImpl} and {@link IndexRow}.
 */
public class UnitConverter {

    /**
     * Converts the unit, {@link UnitImpl}, to indexed row, {@link IndexRow}.
     * @param unit unit object
     * @return indexed row
     */
    public static IndexRow toIndexRow(UnitImpl unit) {
        IndexRow indexRow = new IndexRow(unit.getInternalID(), unit.getEssence().toJsonString(), LuceneConstants.Type_Unit);
        indexRow.addFieldValue(LuceneConstants.Symbol, unit.getEssence().getSymbol());
        indexRow.addFieldValue(LuceneConstants.Name, unit.getName());
        indexRow.addFieldValue(LuceneConstants.Namespace, unit.getNamespace());
        indexRow.addFieldValue(LuceneConstants.Source, unit.getSource());

        indexRow.addCommonFieldValue(unit.getDisplaySymbol());
        indexRow.addCommonFieldValue(unit.getDescription());
        indexRow.addCommonFieldValue(unit.getLastModified());
        UnitEssenceImpl essence = unit.getEssence();
        indexRow.addCommonFieldValue(essence.getBaseMeasurement().getEssence().toJsonString());

        String coefficientType = "";
        if(essence.getABCD() != null)
            coefficientType = LuceneConstants.ABCD;
        if(essence.getScaleOffset() != null)
            coefficientType += " " + LuceneConstants.ScaleOffset;
        indexRow.addFieldValue(LuceneConstants.CType, coefficientType);

        UnitDeprecationInfoImpl deprecationInfo = unit.getDeprecationInfo();
        if(deprecationInfo != null) {
            indexRow.addFieldValue(LuceneConstants.State, deprecationInfo.getState());
            indexRow.addCommonFieldValue(deprecationInfo.getRemarks());
            indexRow.addCommonFieldValue(deprecationInfo.getSupersededByUnit());
        }

        return indexRow;
    }

    /**
     * Resolves the unit object based on the indexed row's reference using the unit helper,
     * {@link UnitHelper}.
     * @param indexRow indexed row
     * @param helper unit helper
     * @return the unit object that the indexed row refers to.
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>{@link #isSupported(IndexRow)} returns false.</li>
     *     <li>The unit object can not be resolved.</li>
     * </ol>
     */
    public static UnitImpl toUnit(IndexRow indexRow, CatalogImpl catalogImpl) throws Exception {
        if(!isSupported(indexRow))
            throw new IllegalArgumentException("Unexpected type");
        UnitEssenceImpl unitEssence = Utility.fromJsonString(indexRow.toJsonString(), UnitEssenceImpl.class);
        return catalogImpl.postUnit(unitEssence);
    }

    /**
     * Checks whether the indexed row refers to a unit object
     * @param indexRow indexed row
     * @return true if the indexed row refers to a unit object; otherwise, return false.
     */
    public static boolean isSupported(IndexRow indexRow) {
        return indexRow.toJsonString() != null && LuceneConstants.Type_Unit.equals(indexRow.getType());
    }
}
