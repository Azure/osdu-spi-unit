package org.opengroup.osdu.unitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.opengroup.osdu.unitservice.interfaces.MapState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * An class defines the mapping state between units or measurements.
 */
@Data
@Schema(description = "The status definition of a unit-to-unit or measurement-to-measurement map")
public class MapStateImpl implements MapState {
    @Expose @SerializedName("state")
    @Schema(description = "The unique MapState code; one of the following: 'identical', 'corrected', 'precision', 'conversion', 'conditional', 'unsupported', 'different', 'unresolved'.",type = "string")
    private String state;

    @Expose @SerializedName("description")
    @Schema(description ="example: Indicates that the mapsTo conversion factor represents a correction to the mapsFrom conversion factor. That is, the mapsFrom conversion factor has a problem. The value should be treated the same as for a state of precision. For deprecation state this indicates that the superseded item has corrected behavior.",type = "string")
    private String description;

    @Expose @SerializedName("source")
    @Schema(description = "The source of this MapState, e.g. Energistics or SLB.",type = "string")
    private String source;

    /**
     * Constructor
     */
    public MapStateImpl() {}

    /***
     * Constructor
     * @param state         state definition.
     * @param description   description of the state definition.
     * @param source        link of the state definition.
     */
    public MapStateImpl(String state, String description, String source) {
        this.state = state;
        this.description = description;
        this.source = source;
    }

    /**
     * Gets the state definition. The well-known states include:
     * <ul>
     *     <li>identical</li>
     *     <li>precision</li>
     *     <li>corrected</li>
     *     <li>conversion</li>
     *     <li>conditional</li>
     *     <li>unsupported</li>
     *     <li>different</li>
     *     <li>unresolved</li>
     * </ul>
     * @return  state definition
     */
    public String getState() { return this.state; }

    /**
     * Gets the description of the state definition.
     * @return  description of the state definition.
     */
    public String getDescription() { return this.description; }

    /**
     * Gets the source of the state definition
     * @return link of the state definition
     */
    public String getSource() { return this.source; }
}
