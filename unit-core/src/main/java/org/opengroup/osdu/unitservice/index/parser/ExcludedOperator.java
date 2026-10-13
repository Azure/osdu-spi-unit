package org.opengroup.osdu.unitservice.index.parser;

/**
 * A class represents the excluded operator, '-'.
 */
public class ExcludedOperator {
    private static final char Self = '-';

    /**
     * Check whether a character is the excluded operator.
     * @param ch character
     * @return true if the ch is the excluded operator; otherwise, false.
     */
    public static boolean isExcludedOperator(char ch)
    {
        return Self == ch;
    }

    @Override
    public String toString()
    {
        return String.valueOf(Self);
    }
}
