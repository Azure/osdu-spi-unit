package org.opengroup.osdu.unitservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.opengroup.osdu.unitservice.interfaces.ABCD;
import org.opengroup.osdu.unitservice.helper.Utility;

/**
Container class for the Energistics unit parameterization y = (A+B*x)/(C+D*x).
*/
@Data
@Schema(description = "Energistics standard parameterization y = (A+Bx)/(C+Dx)")
public class ABCDImpl implements ABCD {
    @Expose @SerializedName("a")
    @Schema(description = "Coefficient A in the Energistics unit parameterization y = (A+Bx)/(C+Dx)",type = "number", format = "double")
    private double a;

    @Expose @SerializedName("b")
    @Schema(description = "Coefficient B in the Energistics unit parameterization y = (A+Bx)/(C+Dx)",type = "number", format = "double")
    private double b;

    @Expose @SerializedName("c")
    @Schema(description = "Coefficient C in the Energistics unit parameterization y = (A+Bx)/(C+Dx)",type = "number", format = "double")
    private double c;

    @Expose @SerializedName("d")
    @Schema(description = "Coefficient D in the Energistics unit parameterization y = (A+Bx)/(C+Dx)",type = "number", format = "double")
    private double d;

    /**
     *
    Constructor for an deserialization instance.
    */
    private ABCDImpl() {
    }

    public ABCDImpl(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Gets the coefficient A in the Energistics unit parameterization y = (A+B*x)/(C+D*x)
     * @return coefficient A
     */
	@JsonProperty("a")
    public double getA() { return this.a; }

    /**
     * Gets the coefficient B in the Energistics unit parameterization y = (A+B*x)/(C+D*x)
     * @return coefficient B
     */
	@JsonProperty("b")
    public double getB() { return this.b; }

    /**
     * Gets the coefficient C in the Energistics unit parameterization y = (A+B*x)/(C+D*x)
     * @return coefficient C
     */
	@JsonProperty("c")
    public double getC() { return this.c; }

    /**
     * Gets the coefficient D in the Energistics unit parameterization y = (A+B*x)/(C+D*x)
     * @return coefficient D
     */
	@JsonProperty("d")
    public double getD() { return this.d; }

    /**
     * Checks whether the instance is valid.
     * @return true if the following conditions are all met:
     * <ol>
     *     <li>none of the coefficients is @see Double.NaN</li>
     *     <li>coefficient B is none-zero</li>
     *     <li>Coefficient C and D are not all zero.</li>
     * </ol>
     * otherwise, false
     */
    @JsonIgnore
    public boolean isValid() {
        if(Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c) || Double.isNaN(d))
            return false;

        return !(Utility.isZeroValue(b) || (Utility.isZeroValue(c) && Utility.isZeroValue(d)));
    }

    @Override
    public int hashCode() {
        int hash = Double.valueOf(a).hashCode();
        hash = hash * 17 + Double.valueOf(b).hashCode();
        hash = hash * 31 + Double.valueOf(c).hashCode();
        hash = hash * 13 + Double.valueOf(d).hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof ABCDImpl))
            return false;

        ABCDImpl other = (ABCDImpl) obj;
        return this == other || this.hashCode() == other.hashCode();
    }

}
