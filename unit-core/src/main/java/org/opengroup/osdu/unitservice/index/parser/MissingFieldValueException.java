package org.opengroup.osdu.unitservice.index.parser;

/**
 * Exception representing a missing field value in the keyword
 */
public class MissingFieldValueException extends Exception {
    private int position;

    /**
     * Constructor
     * @param position the position of the expected field value that is missed in the keyword.
     */
    public MissingFieldValueException(int position) {
        this.position = position;
    }

    @Override
    public String getMessage() {
        return "Expecting field value in position " + position;
    }
}
