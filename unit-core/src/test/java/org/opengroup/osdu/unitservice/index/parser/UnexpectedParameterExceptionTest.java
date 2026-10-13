package org.opengroup.osdu.unitservice.index.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by ZMai on 7/14/2016.
 */
public class UnexpectedParameterExceptionTest {
    @Test
    public void constructor() {
        String message = "field value";
        int position = 10;
        UnexpectedParameterException exception = new UnexpectedParameterException(message, position);
        assertTrue(exception.getMessage().contains(message));
        assertTrue(exception.getMessage().contains("" + position));
    }
}
