package org.opengroup.osdu.unitservice.index.parser;

/**
 * A class represents the excluded operator, "OR".
 */
public class OrOperator {
    private static final String Self = "OR";

    /**
     * Check whether a string is the OR operator.
     * @param op string
     * @return true if the string is the OR operator. otherwise, false.
     */
    public static boolean isOrOperator(String op)
    {
        return Self.equals(op);
    }

    @Override
    public String toString()
    {
        return Self;
    }
}
