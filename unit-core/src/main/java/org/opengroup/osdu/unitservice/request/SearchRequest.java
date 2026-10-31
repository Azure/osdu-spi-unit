/*
 * Copyright 2018 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.unitservice.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.opengroup.osdu.unitservice.configuration.StrictStringDeserializer;

/**
 * The search query request container class, which is used for generic and specific unit or measurement search.
 */
@Data
@Schema(description = "Request to containing a Lucene style query string.")
public class SearchRequest {
    @Schema(description ="The Lucene style query string with keywords", type = "string")
    @JsonDeserialize(using = StrictStringDeserializer.class)
    private String query;

    /**
     * Constructor
     */
    public SearchRequest() {
    }

    /**
     * Constructor with query string.
     *
     * <p>Jackson's single-argument heuristic would treat this as a delegating creator,
     * so a JSON string body ({@code "AAA"}) would become a search. The OpenAPI schema
     * is an object. Keep the constructor for Java callers and disable that creator.
     *
     * @param query The Lucene style query string
     */
    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
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
