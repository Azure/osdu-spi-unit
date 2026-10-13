package org.opengroup.osdu.unitservice.index;

import org.opengroup.osdu.unitservice.helper.Utility;

/**
 * A class defines the keyword search constraints and
 * paging parameters of the query result.
 */
public class SearchInput {
    /**
     * Maximum size of query result.
     */
    public static final int MaxSize = 10000;

    /**
     * Default size of the query result.
     * The size of the query result can be overridden by size parameter, {@link #getSize()}
     */
    public static final int DefaultSize = 100;

    private String keyword;
    private String dataType;
    private int offset;
    private int size;

    /**
     * Constructor
     * @param keyword searched keyword
     * @throws IllegalArgumentException an exception will be thrown if keyword is null or empty
     */
    public SearchInput(String keyword) throws IllegalArgumentException {
        this(keyword, null);
    }

    /**
     *
     * @param keyword searched keyword
     * @param dataType searched data type
     * @throws IllegalArgumentException an exception will be thrown
     * if both keyword and data type are null or empty
     */
    public SearchInput(String keyword, String dataType) throws IllegalArgumentException{
        if(Utility.isNullOrEmpty(keyword) && Utility.isNullOrEmpty(dataType))
            throw new IllegalArgumentException("");

        this.keyword = keyword;
        this.dataType = dataType;
        offset = 0;
        size = DefaultSize;
    }

    /**
     * Gets the searched keyword
     * @return searched keyword
     */
    public String getKeyword() { return this.keyword; }

    /**
     * Gets the searched data type
     * @return searched data type
     */
    public String getDataType() { return this.dataType; }

    /**
     * Gets the offset of the result.
     * @return offset of the result
     */
    public int getOffset() { return this.offset; }

    /**
     * Sets the offset of the result. Negative offset will be reset to 0.
     * @param offset offset of the result.
     */
    public void setOffset(int offset) {
        if(offset < 0) {
            offset = 0;
        }
        this.offset = offset;
    }

    /**
     * Gets the maximum size of the result.
     * @return maximum size of the result.
     */
    public int getSize() { return this.size; }

    /**
     * Sets the maximum size of the result. If the size is larger than
     * {@link #MaxSize} or less than 0, it will be reset to {@link #MaxSize}.
     * @param size maximum size of the result.
     */
    public void setSize(int size) {
        if(size < 0 || size > MaxSize)
            size = MaxSize;
        this.size = size;
    }

}
