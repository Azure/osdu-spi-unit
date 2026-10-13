package org.opengroup.osdu.unitservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.opengroup.osdu.unitservice.interfaces.UnitEssence;
import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.interfaces.MeasurementEssence;

/**
 * A compact class describing the essence of a unit, {@link UnitImpl}.
 */
@Data
@Schema(description = "The essence of a unit parameterization")
public class UnitEssenceImpl implements UnitEssence {
    @Expose @SerializedName("abcd")
    @Schema(description = "Energistics standard parameterization y = (A+Bx)/(C+Dx)")
    private ABCDImpl abcd;

    @Expose @SerializedName("scaleOffset")
    @Schema(implementation = ScaleOffsetImpl.class)
    private ScaleOffsetImpl scaleOffset;

    @Expose @SerializedName("symbol")
    @Schema(description = "Unit in y = scale*(x-offset) parameterization", type = "string")
    private String symbol;

    @Expose @SerializedName("baseMeasurement")
    private MeasurementEssenceImpl baseMeasurementEssence;

	@Expose @SerializedName("type")
	@JsonProperty("type")
    @Schema(description = "The type string for this unit essence, either 'USO' for ScaleOffset or 'UAD' for Abcd", type = "string")
    private String type;

    private MeasurementImpl baseMeasurement;

    /**
     * Empty constructor
     */
    public UnitEssenceImpl() {}

    /**
     * Gets the {@link ABCDImpl} container for the Energistics unit parameterization y = (A+B*x)/(C+D*x).
     * if {@link ABCDImpl} is null, the parameterization is expected to be done by the {@link ScaleOffsetImpl} container.
     * @return  ABCD instance
     */
	@JsonProperty("abcd")
    public ABCDImpl getABCD() { return this.abcd; }

    /**
     * Sets the {@link ABCDImpl} container for the Energistics unit parameterization y = (A+B*x)/(C+D*x).
     * if {@link ABCDImpl} is null, the parameterization is expected to be done by the {@link ScaleOffsetImpl} container.
     * @param abcd ABCD instance
     */
    public void setABCD(ABCDImpl abcd) {
        this.abcd = abcd;
    }

    /**
     * Gets the {@link ScaleOffsetImpl} container for the OSDD classic unit parameterization y = scale(x-offset).
     * If {@link ScaleOffsetImpl} is null, the parameterization is expected to be done by the {@link ABCDImpl} container.
     * @return ScaleOffset instance
     */
    @JsonProperty("scaleOffset")
    public ScaleOffsetImpl getScaleOffset() { return this.scaleOffset; }

    /**
     * Sets the {@link ScaleOffsetImpl} container for the OSDD classic unit parameterization y = scale(x-offset).
     * If {@link ScaleOffsetImpl} is null, the parameterization is expected to be done by the {@link ABCDImpl} container.
     * @param scaleOffset ScaleOffset instance
     */
    public void setScaleOffset(ScaleOffsetImpl scaleOffset) {
        this.scaleOffset = scaleOffset;
    }

    /**
     * Gets the unit symbol, the identifier of the unit (but often non-unique in the context of a catalog.
     * @return unit symbol
     */
	@JsonProperty("symbol")
    public String getSymbol() { return this.symbol; }

    /**
     * Sets the unit symbol, the identifier of the unit (but often non-unique in the context of a catalog.
     * @param symbol unit symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the base measurement essence {@link MeasurementImpl}
     * @return base measurement essence
     */
    @JsonProperty("baseMeasurement")
    public MeasurementEssence getBaseMeasurementEssence() { return this.baseMeasurementEssence; }

	/**
	 * get the type property
	 * @return the type property
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

    /**
     * Set the unit essence type (USO or UAD)
     * @param type The essence type (USO or AUD).
     */
	public void setType(String type){
	    this.type = type;
    }

    /**
     * Sets the base measurement essence {@link MeasurementImpl}
     * @param measurementEssence base measurement essence
     */
    public void setBaseMeasurementEssence(MeasurementEssenceImpl measurementEssence) {
        this.baseMeasurementEssence = measurementEssence;
    }

    /**
     * Gets the base measurement {@link MeasurementImpl}
     * @return base measurement
     */
    @JsonIgnore
    public MeasurementImpl getBaseMeasurement() { return this.baseMeasurement; }

    /**
     * Sets the base measurement {@link MeasurementImpl}
     * @param measurement base measurement
     */
    void setBaseMeasurement(MeasurementImpl measurement) {
        this.baseMeasurement = measurement;
        baseMeasurementEssence = this.baseMeasurement.getEssence();
    }

    /**
     * Gets the stable, catalog-wide unique identifier for the unit.
     * @return Json string of the unit
     */
    public String toJsonString() {
        return Utility.toJsonString(this);
    }

    @Override
    public int hashCode() {
        int hash = symbol == null? 0 : symbol.hashCode();
        hash = hash * 13 + (baseMeasurementEssence == null? 0: baseMeasurementEssence.hashCode());
        hash = hash * 17 + (scaleOffset == null? 0: scaleOffset.hashCode());
        hash = hash * 31 + (type == null? 0 : type.hashCode());
        hash = hash * 35 + (abcd == null? 0 : abcd.hashCode());

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof UnitEssenceImpl))
            return false;

        UnitEssenceImpl other = (UnitEssenceImpl) obj;
        return this == other || this.hashCode() == other.hashCode();
    }

    private int getBaseMeasurementHashCode() {
        MeasurementEssenceImpl essence = null;
        if(baseMeasurement != null)
            essence = baseMeasurement.getEssence();
        if(essence == null)
            return 0;
        else
            return essence.hashCode();
    }

    @JsonIgnore
    public boolean isValid() {
        boolean ok = this.getSymbol() != null && !this.getSymbol().isEmpty();
        ok = ok && this.getBaseMeasurementEssence() != null &&
                this.getBaseMeasurementEssence().getAncestry() != null &&
                !this.getBaseMeasurementEssence().getAncestry().isEmpty();
        ok = ok && this.getSymbol() != null && !this.getSymbol().isEmpty();
        if (this.getType().equals("USO")) {
            ok = ok && this.getScaleOffset() != null &&
                    !Double.isNaN(this.getScaleOffset().getScale()) &&
                    !Double.isNaN(this.getScaleOffset().getOffset());
        } else if (this.getType().equals("UAD")) {
            ok = ok && this.getABCD() != null &&
                    !Double.isNaN(this.getABCD().getA()) &&
                    !Double.isNaN(this.getABCD().getB()) &&
                    !Double.isNaN(this.getABCD().getC()) &&
                    !Double.isNaN(this.getABCD().getD());
        }
        else ok = false;
        return ok;
    }
}
