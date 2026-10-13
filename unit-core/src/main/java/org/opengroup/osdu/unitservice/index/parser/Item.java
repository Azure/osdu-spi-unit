package org.opengroup.osdu.unitservice.index.parser;

/**
 * A class defines an atom of keyword constraint.
 */
public class Item {
    private ItemValue field;
    private ItemValue value;
    private boolean isExcluded;

    public ItemValue getField() { return field; }
    public ItemValue getValue() { return value; }
    public boolean isExcluded() { return isExcluded; }

    /**
     * Constructor for the keyword constraint that do not have field specified.
     * The isExcluded is set to false.
     * @param value value item
     */
    public Item(ItemValue value)
    {
        this(value, false);
    }

    /**
     * Constructor for the keyword constraint that do not have field specified.
     * @param value value item
     * @param excluded defines whether the value is excluded or not
     */
    public Item(ItemValue value, boolean excluded)
    {
        this.value = value;
        this.isExcluded = excluded;
    }

    /**
     * Constructor for the keyword constraint that do have field specified.
     * The isExcluded is set to false.
     * @param field field item
     * @param value value item
     */
    public Item(ItemValue field, ItemValue value)
    {
        this(field, value, false);
    }

    /**
     * Constructor for the keyword constraint that do have field specified.
     * @param field field item
     * @param value value item
     * @param excluded defines whether the value is excluded or not
     */
    public Item(ItemValue field, ItemValue value, boolean excluded)
    {
        this(value, excluded);
        this.field = field;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        if (isExcluded)
            stringBuilder.append("-");
        if (field != null)
        {
            stringBuilder.append(field.toString());
            stringBuilder.append(":");
        }
        if (value != null)
            stringBuilder.append(value.toString());

        return stringBuilder.toString();
    }
}
