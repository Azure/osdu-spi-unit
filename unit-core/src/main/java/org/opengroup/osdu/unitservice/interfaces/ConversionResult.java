package org.opengroup.osdu.unitservice.interfaces;

/**
 * An interface to encapsulate the conversion result in requested type, {@link ABCD} or
 * {@link ScaleOffset} with the from and to units, {@link Unit}.
 */
public interface ConversionResult {
    /**
     * Gets the conversion result in type {@link ABCD}
     * @return ABCD result
     */
    public ABCD getABCD();

    /**
     * Gets the conversion result in type {@link ScaleOffset}
     * @return Scale offset result
     */
    public ScaleOffset getScaleOffset();

    /**
     * Gets the fromUnit {@link Unit}
     * @return from unit
     */
    public Unit getFromUnit();

    /**
     * Gets the toUnit {@link Unit}
     * @return to unit
     */
    public Unit getToUnit();
}
