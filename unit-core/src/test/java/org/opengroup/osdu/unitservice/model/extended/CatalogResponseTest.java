package org.opengroup.osdu.unitservice.model.extended;

import org.opengroup.osdu.unitservice.interfaces.*;
import org.opengroup.osdu.unitservice.model.*;
import org.opengroup.osdu.unitservice.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CatalogResponseTest {
    CatalogResponse catalogResponse;

    @BeforeEach
    public void setup() {
        catalogResponse = new CatalogResponse();
    }

    @Test
    public void constructor() {
        CatalogResponse catalogResponse = new CatalogResponse();
        assertNotNull(catalogResponse.getLastModified());
        assertNotNull(catalogResponse.getUnits());
        assertNotNull(catalogResponse.getUnitMaps());
        assertNotNull(catalogResponse.getMeasurements());
        assertNotNull(catalogResponse.getMeasurementMaps());
        assertNotNull(catalogResponse.getUnitSystemInfos());
        assertNotNull(catalogResponse.getMapStates());

        assertEquals("", catalogResponse.getLastModified());
        assertEquals(0, catalogResponse.getTotalUnitCount());
        assertEquals(0, catalogResponse.getTotalUnitMapCount());
        assertEquals(0, catalogResponse.getTotalMeasurementCount());
        assertEquals(0, catalogResponse.getTotalMeasurementMapCount());
        assertEquals(0, catalogResponse.getTotalUnitSystemCount());
        assertEquals(0, catalogResponse.getTotalMapStateCount());
    }

    @Test
    public void lastModifiedAccessor() {
        String lastModified = "20180525";
        catalogResponse.setLastModified(lastModified);
        assertEquals(lastModified, catalogResponse.getLastModified());
    }

    @Test
    public void unitListAccessor() {
        List<Unit> units = new ArrayList<Unit>();
        int n = 1;
        for(int i = 0; i < n; i++) {
            units.add(new UnitImpl());
        }

        catalogResponse.setUnits(units);
        catalogResponse.setTotalUnitCount(units.size());
        assertEquals(n, catalogResponse.getTotalUnitCount());
        assertEquals(n, catalogResponse.getUnits().size());
    }

    @Test
    public void unitMapListAccessor() {
        List<UnitMap> unitMaps = new ArrayList<UnitMap>();
        int n = 2;
        for(int i = 0; i < n; i++) {
            unitMaps.add(new UnitMapImpl("from_" + 1, "to_" + i));
        }

        catalogResponse.setUnitMaps(unitMaps);
        catalogResponse.setTotalUnitMapCount(unitMaps.size());
        assertEquals(n, catalogResponse.getTotalUnitMapCount());
        assertEquals(n, catalogResponse.getUnitMaps().size());
    }

    @Test
    public void measurementListAccessor() {
        List<Measurement> measurements = new ArrayList<Measurement>();
        int n = 3;
        for(int i = 0; i < n; i++) {
            MeasurementImpl childMeasurement = new MeasurementImpl();
            childMeasurement.setIsBaseMeasurement(false);
            measurements.add(childMeasurement);
        }

        catalogResponse.setMeasurements(measurements);
        catalogResponse.setTotalMeasurementCount(measurements.size());
        assertEquals(n, catalogResponse.getTotalMeasurementCount());
        assertEquals(n, catalogResponse.getMeasurements().size());
    }

    @Test
    public void measurementMapListAccessor() {
        List<MeasurementMap> measurementMaps = new ArrayList<MeasurementMap>();
        int n = 4;
        for(int i = 0; i < n; i++) {
            measurementMaps.add(new MeasurementMapImpl("from_" + 1, "to_" + i));
        }

        catalogResponse.setMeasurementMaps(measurementMaps);
        catalogResponse.setTotalMeasurementMapCount(measurementMaps.size());
        assertEquals(n, catalogResponse.getTotalMeasurementMapCount());
        assertEquals(n, catalogResponse.getMeasurementMaps().size());
    }

    @Test
    public void unitSystemListAccessor() {
        List<UnitSystemInfo> unitSystems = new ArrayList<>();
        int n = 5;
        for(int i = 0; i < n; i++) {
            unitSystems.add(new UnitSystemInfo("n", "d", "a", "p"));
        }

        catalogResponse.setUnitSystemInfos(unitSystems);
        catalogResponse.setTotalUnitSystemCount(unitSystems.size());
        assertEquals(n, catalogResponse.getTotalUnitSystemCount());
        assertEquals(n, catalogResponse.getUnitSystemInfos().size());
    }

    @Test
    public void mapStateListAccessor() {
        List<MapState> mapStates = new ArrayList<MapState>();
        int n = 6;
        for(int i = 0; i < n; i++) {
            mapStates.add(new MapStateImpl("state" + i, "remark" + i, "source" + i));
        }

        catalogResponse.setMapStates(mapStates);
        catalogResponse.setTotalMapStateCount(mapStates.size());
        assertEquals(n, catalogResponse.getTotalMapStateCount());
        assertEquals(n, catalogResponse.getMapStates().size());
    }
}
