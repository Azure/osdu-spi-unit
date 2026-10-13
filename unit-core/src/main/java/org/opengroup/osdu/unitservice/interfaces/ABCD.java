package org.opengroup.osdu.unitservice.interfaces;

/**
 * An interface defines the Energistics ABCD model for {@link Unit}
 */
public interface ABCD {

    /**
     * Gets the {@link Unit} coefficient A in the Energistics unit parameterization y = (A+B*x)/(C+D*x)
     * @return  coefficient A
     */
    public double getA();

    /**
     * Gets the {@link Unit} coefficient B in the Energistics unit parameterization y = (A+B*x)/(C+D*x)
     * @return  coefficient B
     */
    public double getB();

    /**
     * Gets the {@link Unit} coefficient C in the Energistics unit parameterization y = (A+B*x)/(C+D*x)
     * @return  coefficient C
     */
    public double getC();

    /**
     * Gets the {@link Unit} coefficient D in the Energistics unit parameterization y = (A+B*x)/(C+D*x)
     * @return  coefficient D
     */
    public double getD();
}
