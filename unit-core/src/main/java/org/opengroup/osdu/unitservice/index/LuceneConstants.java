package org.opengroup.osdu.unitservice.index;

import java.util.Map;
import java.util.TreeMap;

/**
 * A constant/utility class defines the indexing related constants and utility methods.
 */
public class LuceneConstants {

    public static final int MaximumItems = 10000;

    public static final String ABCD = "ABCD";
    public static final String ScaleOffset = "ScaleOffset";

    public static final String Type_Unit = "Unit";
    public static final String Type_Measurement = "Measurement";
    public static final String Type_UnitMapItem = "UnitMapItem";
    public static final String Type_MeasurementMapItem = "MeasurementMapItem";

    // Mandatory and reserved fields
    public static final String Id = "index-id";
    public static final String Content = "index-content";
    public static final String Reference = "index-reference";
    public static final String Type = "index-type";

    // Optional fields
    public static final String Name = "Name";
    public static final String Namespace = "Namespace";
    public static final String Source = "Source";
    public static final String Symbol = "Symbol";
    public static final String CType = "CType"; // Coefficient type
    public static final String Ancestry = "Ancestry";
    public static final String Code = "Code";
    public static final String DimensionCode = "DimensionCode";
    public static final String UnitQuantityCode = "UnitQuantityCode";
    public static final String DimensionAnalysis = "DimensionAnalysis";
    public static final String State = "State";
    public static final String BaseMeasurement = "BaseMeasurement";

    /**
     * Default field
     */
    public static final String DefaultField = Content;

    /**
     * Resolve the field name based on a predefine map.
     * If the fieldName can not be resolved from the map, returns the default field, {@link #DefaultField}
     * @param fieldName field name to be resolved
     * @return a field name used in the index
     */
    public static String resolveFieldName(String fieldName) {
        if(fieldName == null ||
           !fieldMaps.containsKey(fieldName))
            return DefaultField;

        return fieldMaps.get(fieldName);
    }

    private static Map<String, String> fieldMaps;
    static {
        fieldMaps = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        fieldMaps.put("id", Id);
        fieldMaps.put("content", Content);
        fieldMaps.put("type", Type);
        fieldMaps.put("reference", Reference);
        fieldMaps.put("name", Name);
        fieldMaps.put("namespace", Namespace);
        fieldMaps.put("source", Source);
        fieldMaps.put("symbol", Symbol);
        fieldMaps.put("cType", CType);
        fieldMaps.put("ancestry", Ancestry);
        fieldMaps.put("code", Code);
        fieldMaps.put("dimensionCode", DimensionCode);
        fieldMaps.put("quantityCode", UnitQuantityCode);
        fieldMaps.put("dimensionAnalysis", DimensionAnalysis);
        fieldMaps.put("state", State);
        fieldMaps.put("baseMeasurement", BaseMeasurement);
    }
}
