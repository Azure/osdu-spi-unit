package org.opengroup.osdu.unitservice.index.parser;

/**
 * An item value defines a word or a list of words that are quoted by '"'.
 */
public class ItemValue {
    private String value;
    private boolean isQuoted;

    /**
     * Constructor. The value is not quoted by '"'
     * @param value a word
     */
    public ItemValue(String value)
    {
        this(value, false);
    }

    /**
     * Constructor.
     * @param value a word or a list of words if isQuoted is true
     * @param isQuoted whether the value is quoted by '"'
     */
    public ItemValue(String value, boolean isQuoted)
    {
        this.value = value;
        this.isQuoted = isQuoted;
    }

    /**
     * Gets the value that is a word or a list of words that are quoted by '"'.
     * @return the value
     */
    public String getValue() { return value;}

    /**
     * Gets whether the value is quoted by '"'
     * @return true if it is quoted; otherwise, false.
     */
    public boolean isQuoted() { return isQuoted; }

    @Override
    public String toString()
    {
        return isQuoted ? "\"" + TokenScanner.escape(value, '"') + "\"" : value;
    }
}
