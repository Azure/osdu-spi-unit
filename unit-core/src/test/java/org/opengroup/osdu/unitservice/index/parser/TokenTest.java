package org.opengroup.osdu.unitservice.index.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by ZMai on 7/14/2016.
 */
public class TokenTest {

    @Test
    public void constructor() {
        ItemValue itemValue = new ItemValue("abc");
        int startPosition = 5;
        int endPosition = 8;
        Token token = new Token(itemValue, startPosition, endPosition);
        assertEquals(itemValue, token.getObject());
        assertEquals(startPosition, token.getStartPosition());
        assertEquals(endPosition, token.getEndPosition());
        assertEquals(itemValue.toString(), token.toString());

        assertFalse(token.isOrOperator());
        assertFalse(token.isExcludedOperator());
        assertFalse(token.isFieldOperator());
        assertFalse(token.isPhraseQuote());
    }

    @Test
    public void isOrOperator() {
        int startPosition = 5;
        int endPosition = 7;
        Token token = new Token(new OrOperator(), startPosition, endPosition);
        assertTrue(token.isOrOperator());
    }

    @Test
    public void isExcludedOperator() {
        int startPosition = 5;
        int endPosition = 6;
        Token token = new Token(new ExcludedOperator(), startPosition, endPosition);
        assertTrue(token.isExcludedOperator());
    }

    @Test
    public void isFieldOperator() {
        int startPosition = 5;
        int endPosition = 6;
        Token token = new Token(new FieldOperator(), startPosition, endPosition);
        assertTrue(token.isFieldOperator());
    }

    @Test
    public void isPhraseQuote() {
        int startPosition = 5;
        int endPosition = 6;
        Token token = new Token(new PhraseQuote(), startPosition, endPosition);
        assertTrue(token.isPhraseQuote());
    }
}
