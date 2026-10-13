package org.opengroup.osdu.unitservice.interfaces;

/**
 * An interface defines the mapping state between two {@link Unit}s
 */
public interface UnitMapItem {
    /**
     * Gets the from {@link Unit}.
     * @return  from unit
     */
    public Unit getFromUnit();

    /**
     * Gets the to {@link Unit}.
     * @return  to unit
     */
    public Unit getToUnit();

    /**
     * Gets the 'from' namespace
     * @return The 'from' namespace
     */
    public String getFromNamespace();

    /**
     * Gets the 'to' namespace
     * @return The 'to' namespace
     */
    public String getToNamespace();

    /**
     * Gets the mapping state
     * @return  mapping state
     */
    public String getState();

    /**
     * Gets the note or remark of the mapping reason
     * @return  note or remark
     */
    public String getNote();
}
