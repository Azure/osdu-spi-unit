package org.opengroup.osdu.unitservice.model.extended;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic type of class to describe a block of the items, such as offset of
 * the block in all the items, the total size of the result, a list of the items in current block.
 * */
public class Result<T> {
    private int totalCount;
    private int offset;
    private List<T> items;

    /**
     * Empty constructor
     */
    public Result() {
        this(new ArrayList<T>());
    }

    /**
     * Constructor with arguments of a list of items.
     * The default values for offset and totalCount are 0 and items.size().
     * null items is considered as an empty list.
     * @param items a list of items
     */
    public Result(List<T> items) {
        if(items == null)
            items = new ArrayList<>();

        this.items = items;
        this.offset = 0;
        this.totalCount = items.size();
    }

    /**
     * Constructor with arguments of a list of items, offset and totalCount.
     * @param items a list of items
     * @param offset offset of the items in all the items
     * @param totalCount total number of all the items.
     * @throws IllegalArgumentException an exception will be thrown if one of following conditions is met:
     * <ol>
     *     <li>offset is negative</li>
     *     <li>totalCount is negative</li>
     *     <li>offset + items.size() &lt; totalCount</li>
     * </ol>
     */
    public Result(List<T> items, int offset, int totalCount) throws IllegalArgumentException {
        if(items == null)
            items = new ArrayList<>();

        if(offset < 0)
            throw new IllegalArgumentException("offset can not be negative");
        if(totalCount < 0)
            throw new IllegalArgumentException("totalCount can not be negative");
        if(offset + items.size() > totalCount)
            throw new IllegalArgumentException("Invalid range as offset + items.size() > totalCount");

        this.items = items;
        this.offset = offset;
        this.totalCount = totalCount;
    }

    /**
     * Gets the total number of all the items
     * @return total number of all the items
     */
    public int getTotalCount() { return this.totalCount; }

    /**
     * Gets the number of items in the result.
     * @return number of items.
     */
    public int getCount() { return this.items.size();  }

    /**
     * Gets the offset of items in all the items.
     * @return offset of items in all the items.
     */
    public int getOffset() { return this.offset; }

    /**
     * Gets a list of items.
     * @return a list of items.
     */
    public List<T> getItems() { return this.items;}
}
