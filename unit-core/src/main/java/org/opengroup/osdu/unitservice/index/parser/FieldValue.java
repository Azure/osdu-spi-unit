package org.opengroup.osdu.unitservice.index.parser;

/**
 * A class contains the field value
 */
public class FieldValue {
    private String fieldValue;

    /**
     * Constructor
     * @param fieldValue the field value
     */
    public FieldValue(String fieldValue)
    {
        this.fieldValue = fieldValue;
    }

    @Override
    public String toString()
    {
        return fieldValue;
    }
}
