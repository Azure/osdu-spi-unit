package org.opengroup.osdu.unitservice.model.extended;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "A container for UnitSystem properties")
public class UnitSystemInfo {
    @Schema(description = "The unit system name",type = "string")
    private String name;
    @Schema(description = "The unit system description",type = "string")
    private String description;
    @Schema(description = "The unit system ancestry, i.e. names separated by '.'",type = "string")
    private String ancestry;
    @Schema(description = "The unit system's persistable reference string.",type = "string")
    private String persistableReference;

    public UnitSystemInfo(String name, String description, String ancestry, String persistableReference) {
        this.name = name;
        this.description = description;
        this.ancestry = ancestry;
        this.persistableReference = persistableReference;
    }

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return this.description;
    }
    @JsonProperty("ancestry")
    public String getAncestry() {
        return this.ancestry;
    }

    @JsonProperty("persistableReference")
    public String getPersistableReference() {
        return this.persistableReference;
    }
}
