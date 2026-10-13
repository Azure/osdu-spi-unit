package org.opengroup.osdu.unitservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.unitservice.interfaces.CatalogLastModified;

public class CatalogLastModifiedImpl implements CatalogLastModified {
    @Schema(description = "The unit of measure catalog's last modification date",type = "string")
    private String lastModified;

    public CatalogLastModifiedImpl() {
    }

    public CatalogLastModifiedImpl(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModified() {
        return this.lastModified;
    }
}
