package org.opengroup.osdu.unitservice.index.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by ZMai on 7/14/2016.
 */
public class MissingFieldValueExceptionTest {

    @Test
    public void constructor() {
        int position = 10;
        MissingFieldValueException exception = new MissingFieldValueException(position);
        assertTrue(exception.getMessage().contains("" + position));
    }

}
