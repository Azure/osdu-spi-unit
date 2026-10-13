package org.opengroup.osdu.unitservice.interfaces;

import java.util.List;

/**
 * An interface defines mapping of {@link Measurement}s between two namespaces,e.g. 'SLB' vs. 'Energistic"
 */
public interface MeasurementMap {
    /**
     * Gets the number of the {@link MeasurementMapItem}s
     * @return number of the {@link MeasurementMapItem}s
     */
    public int getMeasurementMapItemCount();

    /**
     * Gets the namespace of the from {@link Measurement}
     * @return namespace of the from {@link Measurement}
     */
    public String getFromNamespace();

    /**
     * Gets the namespace of the to {@link Measurement}
     * @return namespace of the to {@link Measurement}
     */
    public String getToNamespace();

    /**
     * Gets a list of {@link MeasurementMapItem}s
     * @return list of {@link MeasurementMapItem}s
     */
    public List<MeasurementMapItem> getMeasurementMapItems();
}
