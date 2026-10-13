package org.opengroup.osdu.unitservice.interfaces;

/**
 * An interface defines the mapping state between units or measurements.
 */
public interface MapState {

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
    public String getState();

    /**
     * Gets the description of the state definition.
     * @return  description of the state definition.
     */
    public String getDescription();

    /**
     * Gets the source of the state definition
     * @return link of the state definition
     */
    public String getSource();
}
