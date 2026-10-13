package org.opengroup.osdu.unitservice.interfaces;

import java.util.List;

/**
 * An interface defines mapping of {@link Unit}s between two namespaces.
 */
public interface UnitMap {
    /**
     * Gets the number of the {@link UnitMapItem}s
     * @return number of the {@link UnitMapItem}s
     */
    public int getUnitMapItemCount();

    /**
     * Gets the namespace of the from {@link Unit}
     * @return namespace of the from {@link Unit}
     */
    public String getFromNamespace();

    /**
     * Gets the namespace of the to {@link Unit}
     * @return namespace of the to {@link Unit}
     */
    public String getToNamespace();

    /**
     * Gets the {@link UnitMapItem} collection defined in the map
     * @return {@link UnitMapItem} collection
     */
    public List<UnitMapItem> getUnitMapItems();
}
