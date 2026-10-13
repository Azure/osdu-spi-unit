package org.opengroup.osdu.unitservice.index.parser;

/**
 * A class represents the field operator, ':'.
 */
public class FieldOperator {
    private static final char Self = ':';

    /**
     * Check whether a character is the field operator.
     * @param ch character
     * @return true if the ch is the field operator; otherwise, false.
     */
    public static boolean isFieldOperator(char ch)
    {
        return Self == ch;
    }

    @Override
    public String toString()
    {
        return String.valueOf(Self);
    }
}
