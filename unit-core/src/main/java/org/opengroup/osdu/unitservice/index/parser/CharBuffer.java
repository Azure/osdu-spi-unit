package org.opengroup.osdu.unitservice.index.parser;

import org.opengroup.osdu.unitservice.helper.Utility;

/**
 * A buffer to read the characters of the input string.
 */
public class CharBuffer {
    private char[] charArray;
    private int index = 0;

    /**
     * Constructor
     * @param input input string.
     */
    public CharBuffer(String input)
    {
        if (!Utility.isNullOrWhiteSpace(input))
            this.charArray = input.toCharArray();
    }

    /**
     * Returns true if the current cursor does not point to the end of the buffer.
     * @return true if the current cursor does not point to the end of the buffer; otherwise, false.
     */
    public boolean hasNext()
    {
        return (this.charArray != null && this.index < this.charArray.length);
    }

    /**
     * Return next char in buffer without moving the cursor.
     * @return next char in buffer
     */
    public char getPeek()
    {
        if (hasNext())
            return this.charArray[index];

        throw new IndexOutOfBoundsException() ;
    }

    /**
     * Gets next char in buffer and move the cursor to the next position
     * @return next char in buffer
     */
    public char getNext()
    {
        char ch = getPeek();
        this.index++;
        return ch;
    }

    /**
     * Gets current position of the cursor in buffer.
     * @return current position of the cursor in buffer
     */
    public int getPosition()
    {
        return this.index;
    }
}
