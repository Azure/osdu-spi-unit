package org.opengroup.osdu.unitservice.index.parser;

/**
 * A class represents the quote operator, '"'.
 */
public class PhraseQuote {
    private static final char Self = '"';

    /**
     * Check whether a character is the quote operator.
     * @param ch character
     * @return true if the ch is the quote operator; otherwise, false.
     */
    public static boolean isPhraseQuote(char ch)
    {
        return Self == ch;
    }

    @Override
    public String toString()
    {
        return String.valueOf(Self);
    }
}
