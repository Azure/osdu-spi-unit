package org.opengroup.osdu.unitservice.index.parser;

/**
 * Exception representing an unexpected parameter in the keyword
 */
public class UnexpectedParameterException extends Exception {
    private String msg;
    private int position;

    /**
     * Constructor
     * @param msg error message
     * @param position the position of the unexpected parameter in the keyword
     */
    public UnexpectedParameterException(String msg, int position) {
        this.msg = msg;
        this.position = position;
    }

    @Override
    public String getMessage() {
        return "Expecting " + msg + " in position " + position;
    }
}
