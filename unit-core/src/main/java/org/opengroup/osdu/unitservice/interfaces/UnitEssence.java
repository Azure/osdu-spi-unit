package org.opengroup.osdu.unitservice.interfaces;

/**
 * A compact interface describing the essence of a unit, {@link Unit}.
 */
public interface UnitEssence {

    /**
     * Gets the {@link ABCD} container for the Energistics unit parameterization y = (A+B*x)/(C+D*x).
     * if {@link ABCD} is null, the parameterization is expected to be done by the {@link ScaleOffset} container.
     * @return  ABCD instance
     */
    public ABCD getABCD();

    /**
     * Gets the {@link ScaleOffset} container for the OSDD classic unit parameterization y = scale(x-offset).
     * If {@link ScaleOffset} is null, the parameterization is expected to be done by the {@link ABCD} container.
     * @return  IScaleOffset instance
     */
    public ScaleOffset getScaleOffset();

    /**
     * Gets the unit symbol, the identifier of the unit (but often non-unique in the context of a catalog.
     * @return  unit symbol
     */
    public String getSymbol();

	/**
	 * get the type property
	 * @return the type property
	 */
    public String getType();
    
    /**
     * Gets the base measurement essence {@link MeasurementEssence}.
     * @return  the base measurement essence {@link MeasurementEssence}
     */
    public MeasurementEssence getBaseMeasurementEssence();

    /**
     * Gets the stable, catalog-wide unique Json string for the unit essence.
     * @return  Json string of the unit essence
     */
    public String toJsonString();

}

