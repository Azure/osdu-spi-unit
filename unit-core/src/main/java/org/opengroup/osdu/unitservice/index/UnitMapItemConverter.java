package org.opengroup.osdu.unitservice.index;

import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.model.CatalogImpl;
import org.opengroup.osdu.unitservice.model.UnitEssenceImpl;
import org.opengroup.osdu.unitservice.model.UnitImpl;
import org.opengroup.osdu.unitservice.model.UnitMapImpl;
import org.opengroup.osdu.unitservice.model.UnitMapItemImpl;

/**
 * A converter to do conversion between {@link UnitMapItemImpl} and {@link IndexRow}.
 */
public class UnitMapItemConverter {
    public static final String Delimiter = "<>";

    /**
     * Converts the unit map item, {@link UnitMapItemImpl}, to indexed row, {@link IndexRow}.
     * @param mapItem unit map item
     * @return indexed row
     */
    public static IndexRow toIndexRow(UnitMapItemImpl mapItem) {
        UnitImpl fromUnit = mapItem.getFromUnit();
        UnitImpl toUnit = mapItem.getToUnit();
        String id = fromUnit.getInternalID() + Delimiter + toUnit.getInternalID();
        String reference = fromUnit.getEssence().toJsonString() + Delimiter + toUnit.getEssence().toJsonString();
        IndexRow indexRow = new IndexRow(id, reference, LuceneConstants.Type_UnitMapItem);

        String namespace = fromUnit.getNamespace() + " " + toUnit.getNamespace();
        indexRow.addFieldValue(LuceneConstants.Namespace, namespace);
        indexRow.addFieldValue(LuceneConstants.State, mapItem.getState());
        indexRow.addCommonFieldValue(mapItem.getNote());

        addUnitProperties(indexRow, fromUnit);
        addUnitProperties(indexRow, toUnit);

        return indexRow;
    }

    private static void addUnitProperties(IndexRow indexRow, UnitImpl unit)  {
        IndexRow unitRow = UnitConverter.toIndexRow(unit);

        for (String propertyValue : unitRow.getFieldValuePairs().values()) {
            indexRow.addCommonFieldValue(propertyValue);
        }

        for (String propertyValue : unitRow.getCommonFieldValues()) {
            indexRow.addCommonFieldValue(propertyValue);
        }
    }

    /**
     * Resolves the unit map item based on the indexed row's reference using the unit helper,
     * {@link UnitHelper}.
     * @param indexRow indexed row
     * @param helper unit helper
     * @return the unit map item that the indexed row refers to.
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>{@link #isSupported(IndexRow)} returns false.</li>
     *     <li>The unit helper is null</li>
     *     <li>The unit map item can not be resolved.</li>
     * </ol>
     */
    public static UnitMapItemImpl toUnitMapItem(IndexRow indexRow, CatalogImpl catalogImpl) throws Exception {
        if(!isSupported(indexRow))
            throw new IllegalArgumentException("Unexpected type");

        String[] essenceJsons = indexRow.toJsonString().split(Delimiter);
        if(essenceJsons == null || essenceJsons.length != 2)
            throw new IllegalArgumentException("Invalid reference format for UnitMapItem");
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(essenceJsons[0], UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(essenceJsons[1], UnitEssenceImpl.class);       
        UnitImpl fromUnit = catalogImpl.postUnit(fromUnitEssence);
        UnitImpl toUnit = catalogImpl.postUnit(toUnitEssence);
        UnitMapImpl unitMap = catalogImpl.getUnitMap(fromUnit.getNamespace(), toUnit.getNamespace());
        return unitMap.getUnitMapItem(fromUnit, toUnit);
    }

    /**
     * Checks whether the indexed row refers to a unit map item
     * @param indexRow indexed row
     * @return true if the indexed row refers to a unit map item; otherwise, return false.
     */
    public static boolean isSupported(IndexRow indexRow) {
        return indexRow.toJsonString() != null && LuceneConstants.Type_UnitMapItem.equals(indexRow.getType());
    }
}
