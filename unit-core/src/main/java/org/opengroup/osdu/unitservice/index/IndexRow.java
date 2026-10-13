package org.opengroup.osdu.unitservice.index;

import org.opengroup.osdu.unitservice.helper.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A object of the {@link IndexRow} contains the indexed properties of the object when doing index.
 * It contains the stored properties from the searched doc when searching index.
 */
public class IndexRow {
    private String id;
    private String essenceJson;
    private String type;

    private List<String> commonFieldValues;
    private Map<String, String> fieldValues;


    /**
     * Constructor with stored properties as arguments. All the arguments will be searchable and stored with the document.
     * @param id id of the indexed object
     * @param essenceJson the essence of the indexed object
     * @param type the short type of the indexed object, such as Unit, Measurement, UnitMapItem and MeasurementMapItem
     * @throws IllegalArgumentException an exception will be thrown if any argument is null or empty.
     */
    public IndexRow(String id, String essenceJson, String type) throws IllegalArgumentException
    {
        if(Utility.isNullOrEmpty(id))
            throw new IllegalArgumentException("uniqueId can not be null.");
        if(Utility.isNullOrEmpty(essenceJson))
            throw new IllegalArgumentException("The essence Json can not be null.");
        if(Utility.isNullOrEmpty(type))
            throw new IllegalArgumentException("type can not be null.");

        this.id = id;
        this.essenceJson = essenceJson;
        this.type = type;
        commonFieldValues = new ArrayList<>();
        fieldValues = new HashMap<>();
    }

    /**
     * Gets the id of the indexed object. The id is used to update/delete the indexed object from the index
     * @return the id of the indexed object.
     */
    public String getId() { return this.id; }

    /**
     * Gets the essence of the indexed object. The essence is used to search and resolve the object by essence
     * @return the essence of the indexed object
     */
    public String toJsonString() { return essenceJson;  }

    /**
     * Gets the type of the object. Currently, four types of the objects are indexed for unit service catalog:
     * <ol>
     *     <li>Unit</li>
     *     <li>Measurement</li>
     *     <li>UnitMapItem</li>
     *     <li>MeasurementMapItem</li>
     * </ol>
     * @return the type of the object
     */
    public String getType() { return type; }

    /**
     * Gets the common field values that is a list of property values that are indexed with default field,
     * {@link LuceneConstants#DefaultField}.
     * @return A list of the property values
     */
    public List<String> getCommonFieldValues() { return this.commonFieldValues; }

    /**
     * Adds the property value to the common field value list, {@link #getCommonFieldValues()}
     * @param value value added to the common field value list. Null or empty value will be ignored.
     */
    public void addCommonFieldValue(String value) {
        if(!Utility.isNullOrEmpty(value))
            this.commonFieldValues.add(value);
    }

    /**
     * Gets the field name/value pairs. The property value in each pair is indexed with its field name.
     * All the property values are automatically added to common field value list.
      * @return Pairs of the field name/value in map.
     */
    public Map<String, String> getFieldValuePairs() { return this.fieldValues; }

    /**
     * Adds the field name/value pair to the list of field value pairs.
     * @param fieldName Name of the field
     * @param fieldValue String value of the field
     */
    public void addFieldValue(String fieldName, String fieldValue) {
        if(Utility.isNullOrEmpty(fieldName)) return;

        //TODO: should we support null value search
        if(Utility.isNullOrEmpty(fieldValue)) return;

        fieldValues.put(fieldName, fieldValue);
    }
}
