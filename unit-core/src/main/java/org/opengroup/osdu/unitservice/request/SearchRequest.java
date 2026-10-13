/*
 * Copyright 2018 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.unitservice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The search query request container class, which is used for generic and specific unit or measurement search.
 */
@Data
@Schema(description = "Request to containing a Lucene style query string.")
public class SearchRequest {
    @Schema(description ="The Lucene style query string with keywords", type = "string")
    private String query;

    /**
     * Constructor
     */
    public SearchRequest() {
    }

    /**
     * Constructor with query string
     *
     * @param query The Lucene style query string
     */
    public SearchRequest(String query) {
        this.query = query;
    }

    /**
     * Gets the Lucene style query string
     *
     * @return The Lucene query string
     */
    public String getQuery() {
        return this.query;
    }
}
