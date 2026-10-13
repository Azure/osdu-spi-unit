package org.opengroup.osdu.unitservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.unitservice.interfaces.MeasurementEssence;
import org.opengroup.osdu.unitservice.helper.Utility;
import lombok.Data;
import java.util.regex.Pattern;

/**
 * The class holding the essence of a {@link MeasurementBase}
 */
@Data
@Schema(description = "The essence of a measurement definition")
public class MeasurementEssenceImpl implements MeasurementEssence {
    private static final int First = 0;

    @Expose @SerializedName("ancestry")
    @Schema(description = "The measurement ancestry, i.e. the parent codes separated by a period symbol.",type = "string")
    private String ancestry = null;

    @Expose @SerializedName("type")
    @Schema(description = "The type string for this measurement",type = "string")
    private String type = null;

    private String baseMeasurementCode;

    /*
    Constructor of an empty instance.
     */
    public MeasurementEssenceImpl() { }

    /**
     * Gets the ancestry. The ancestry is the '.'-separated list of all {@link MeasurementBase#getCode()} in the parentage,
     * e.g: 'Plane_Angle.Image_Origin_Azimuth'.
     * @return ancestry of the measurement.
     */
	@JsonProperty("ancestry")
    public String getAncestry() {
        if(this.ancestry == null)
            return this.baseMeasurementCode;

        return this.ancestry;
    }

    /**
     * Gets the type identifier, fixed to 'UM'.
     * @return type of the measurement.
     */
	@JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * Sets the ancestry. The ancestry is the '.'-separated list of all {@link MeasurementBase#getCode()} in the parentage,
     * e.g: 'Plane_Angle.Image_Origin_Azimuth'.
     * @param ancestry ancestry of the measurement.
     */
    public void setAncestry(String ancestry) {
        this.ancestry = ancestry;

        if(ancestry != null) {
            this.baseMeasurementCode = getCode(First);
        }
    }

    /**
     * Sets the type identifier.
     * @param type type of the measurement. fixed to 'UM'.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the Json string of the {@link MeasurementBase}.
     * @return The Json string
     */
    public String toJsonString() {
        return Utility.toJsonString(this);
    }

    String getBaseMeasurementEssenceJson() {
        String result = null;
        String baseMeasurementCode = getBaseMeasurementCode();
        if(!Utility.isNullOrEmpty(baseMeasurementCode)) {
            MeasurementEssenceImpl baseEssence = new MeasurementEssenceImpl();
            baseEssence.setAncestry(baseMeasurementCode);
            baseEssence.setType("UM");
            result =  baseEssence.toJsonString();
        }
        return result;
    }
    
    String getParentEssenceJson()
    {
        String result = null;
        String cumulativeCode = getAncestry();
        if (cumulativeCode != null)
        {
            String[] parts = cumulativeCode.split(SplitPattern);
            if (parts.length > 1)
            {
                MeasurementEssenceImpl parent = new MeasurementEssenceImpl();
                String parentCode = join(Utility.AncestryDelimiter, parts, parts.length - 1);
                parent.setAncestry(parentCode);
                parent.setType("UM");
                result = parent.toJsonString();
            }
        }
        return result;
    }

    private String getBaseMeasurementCode() {
        if(this.baseMeasurementCode == null && this.ancestry != null) {
            this.baseMeasurementCode = getCode(First);
        }

        return this.baseMeasurementCode;
    }

    private String join(String conjunction, String[] parts, int maxLength) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        maxLength = Math.min(parts.length, maxLength);
        for(int i = 0; i < maxLength; i++) {
            if (first)
                first = false;
            else
                sb.append(conjunction);
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    private String getCode(int level) {
        String result = "";
        String cumulativeCode = getAncestry();
        if (cumulativeCode != null)
        {
            String[] parts = cumulativeCode.split(SplitPattern);
            if (parts.length > 0)
            {
                int index = level == First ? 0 : parts.length - 1;
                return parts[index];
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (ancestry == null)? 0 : ancestry.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof MeasurementEssenceImpl))
            return false;

        MeasurementEssenceImpl other = (MeasurementEssenceImpl) obj;
        return this == other || this.hashCode() == other.hashCode();
    }

    private final static String SplitPattern = Pattern.quote(Utility.AncestryDelimiter);
}


