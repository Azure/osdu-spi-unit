package org.opengroup.osdu.unitservice.index.parser;

/**
 * A class represents the wildcard operator, '*'.
 */
public class Wildcard {
    private static final char Self = '*';

    /**
     * Check whether a character is the wildcard operator.
     * @param ch character
     * @return true if the ch is the wildcard operator; otherwise, false.
     */
    public static boolean isWildcard(char ch)
    {
        return Self == ch;
    }

    @Override
    public String toString()
    {
        return String.valueOf(Self);
    }
}
