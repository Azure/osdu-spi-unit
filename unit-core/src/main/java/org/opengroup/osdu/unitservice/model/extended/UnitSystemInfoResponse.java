package org.opengroup.osdu.unitservice.model.extended;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A type of class to describe a block of string items
 * */
@Data
@Schema(description = "The unit system info structures")
public class UnitSystemInfoResponse {
    @Schema(description = "The array of unit system names")
    private List<UnitSystemInfo> unitSystemInfoList;
    @Schema(description = "The total number of elements in the list defined in the catalog",type = "integer")
    private int totalCount;
    @Schema(description = "The offset into the list as requested",type = "integer")
    private int offset;

    /**
     * Empty constructor
     */
    public UnitSystemInfoResponse() {
        this.unitSystemInfoList = new ArrayList<>();
        this.totalCount = 0;
        this.offset = 0;
    }
    /**
     * Constructor with arguments of a list of items.
     * @param items a list of {@link UnitSystemInfo}
     */
    public UnitSystemInfoResponse(List<UnitSystemInfo> items, int totalCount, int offset) {
        if(items == null)
            items = new ArrayList<>();

        this.unitSystemInfoList = items;
        this.totalCount = totalCount;
        this.offset = offset;
    }

    /**
     * Gets the number of items in the result.
     * @return number of items.
     */
    @JsonProperty("count")
    public int getCount() { return this.unitSystemInfoList.size();  }

    /**
     * Gets the list of {@link UnitSystemInfo}
     * @return the list of {@link UnitSystemInfo}
     */
    @JsonProperty("unitSystemInfoList")
    public List<UnitSystemInfo> getUnitSystemInfoList() { return this.unitSystemInfoList; }

    /**
     * Gets the total number of items in the catalog
     * @return the total number
     */
    @JsonProperty("totalCount")
    public int getTotalCount() {return this.totalCount;}

    /**
     * Gets the offset into the list as requested
     * @return the offset as requested
     */
    @JsonProperty("offset")
    public int getOffset() {return this.offset;}
}
