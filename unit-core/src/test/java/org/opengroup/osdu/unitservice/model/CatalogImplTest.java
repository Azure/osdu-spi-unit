package org.opengroup.osdu.unitservice.model;

import org.opengroup.osdu.unitservice.interfaces.*;
import org.opengroup.osdu.unitservice.helper.MapStateHelper;
import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.model.extended.*;
import org.opengroup.osdu.unitservice.interfaces.*;
import org.opengroup.osdu.unitservice.helper.*;
import org.opengroup.osdu.unitservice.model.extended.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;

public class CatalogImplTest {
    private static CatalogImpl catalog;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        InputStream inputStream = CatalogImplTest.class.getResourceAsStream("/TestCatalog.json");
        if(inputStream == null)
            throw new IOException("Catalog resource file does not exist.");
        Reader reader = new InputStreamReader(inputStream);
        catalog = CatalogImpl.createCatalog(reader);
        assertNotNull(catalog);
        catalog.validate();
    }

    @Test
    public void getName() {
        assertNotNull(catalog);
        assertEquals("OSDD 2.0", catalog.getName());
    }

    @Test
    public void getLastModified() {
        assertNotNull(catalog);
        String lastModified = catalog.getLastModified();
        assertEquals("2017-11-01T16:11:13.748643Z", lastModified);
    }

    @Test
    public void getBaseMeasurements() {
        assertNotNull(catalog);
        List<MeasurementImpl> baseMeasurements = catalog.getBaseMeasurements();
        assertEquals(266, baseMeasurements.size());
    }

    @Test
    public void getChildMeasurements() {
        assertNotNull(catalog);
        List<MeasurementImpl> childMeasurements = catalog.getChildMeasurements();
        assertEquals(4677, childMeasurements.size());
    }

    @Test
    public void getUnitSystems() {
        assertNotNull(catalog);
        List<UnitSystemImpl> unitSystems = catalog.getUnitSystems();
        assertEquals(14, unitSystems.size());
    }

    @Test
    public void getMeasurementMaps() {
        assertNotNull(catalog);

        List<MeasurementMapImpl> measurementMaps = catalog.getMeasurementMaps();
        assertEquals(1, measurementMaps.size());
    }

    @Test
    public void getMeasurementMapWithMatchedNamespaces() throws Exception {
        assertNotNull(catalog);

        List<MeasurementMapImpl> measurementMaps = catalog.getMeasurementMaps();
        assertTrue(measurementMaps.size() > 0);

        for(MeasurementMapImpl measurementMap : measurementMaps) {
            // Getting measurement map is based on the namespaces
            MeasurementImpl fromMeasurement = new MeasurementImpl();
            fromMeasurement.setNamespace(measurementMap.getFromNamespace());
            MeasurementImpl toMeasurement = new MeasurementImpl();
            toMeasurement.setNamespace(measurementMap.getToNamespace());
            MeasurementMapImpl map = null;
            MeasurementMapImpl inverseMap = null;

            map = catalog.getMeasurementMap(fromMeasurement.getNamespace(), toMeasurement.getNamespace());
            assertEquals(measurementMap, map);
            inverseMap = catalog.getMeasurementMap(toMeasurement.getNamespace(), fromMeasurement.getNamespace());
            assertNull(inverseMap);
        }
    }

    @Test
    public void getMeasurementMapWithUnmatchedNamespaces() throws Exception {
        assertNotNull(catalog);

        List<MeasurementMapImpl> measurementMaps = catalog.getMeasurementMaps();
        assertTrue(measurementMaps.size() > 0);
        MeasurementMapImpl measurementMap = measurementMaps.get(0);
        assertNotNull(measurementMap);

        // Set the fromMeasurement and toMeasurement with the same namespace
        MeasurementImpl fromMeasurement = new MeasurementImpl();
        fromMeasurement.setNamespace(measurementMap.getFromNamespace());
        MeasurementImpl toMeasurement = new MeasurementImpl();
        toMeasurement.setNamespace(measurementMap.getFromNamespace());

        measurementMap = catalog.getMeasurementMap(fromMeasurement.getNamespace(), toMeasurement.getNamespace());
        assertNull(measurementMap);
    }

    @Test
    public void geMeasurementMapWithInvalidFromMeasurement() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            List<MeasurementImpl> measurements = catalog.getBaseMeasurements();
            MeasurementImpl fromMeasurement = new MeasurementImpl();
            assertNull(fromMeasurement.getNamespace());
            catalog.getMeasurementMap(fromMeasurement.getNamespace(), measurements.get(0).getNamespace());
            });
    }

    @Test
    public void geMeasurementMapWithNullFromMeasurement() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            List<MeasurementImpl> measurements = catalog.getBaseMeasurements();
            catalog.getMeasurementMap(null, measurements.get(0).getNamespace());
            });
    }

    @Test
    public void geMeasurementMapWithInvalidToMeasurement() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            List<MeasurementImpl> measurements = catalog.getBaseMeasurements();
            MeasurementImpl toMeasurement = new MeasurementImpl();
            assertNull(toMeasurement.getNamespace());
            catalog.getMeasurementMap(measurements.get(0).getNamespace(), toMeasurement.getNamespace());
            });
    }

    @Test
    public void geMeasurementMapWithNullToMeasurement() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            List<MeasurementImpl> measurements = catalog.getBaseMeasurements();
            catalog.getMeasurementMap(measurements.get(0).getNamespace(), null);
            });
    }

    @Test
    public void getUnitMaps() {
        assertNotNull(catalog);
        List<UnitMapImpl> unitMaps = catalog.getUnitMaps();
        assertEquals(11, unitMaps.size());
    }

    @Test
    public void getUnitMapWithMatchedNamespaces() throws Exception {
        assertNotNull(catalog);

        List<UnitMapImpl> unitMaps = catalog.getUnitMaps();
        assertTrue(unitMaps.size() > 0);

        for (UnitMapImpl unitMap : unitMaps) {
            // Getting unit map is based on the namespaces
            UnitImpl fromUnit = new UnitImpl();
            fromUnit.setNamespace(unitMap.getFromNamespace());
            UnitImpl toUnit = new UnitImpl();
            toUnit.setNamespace(unitMap.getToNamespace());
            UnitMapImpl map = null;
            UnitMapImpl inverseMap = null;

            map = catalog.getUnitMap(fromUnit.getNamespace(), toUnit.getNamespace());
            assertEquals(unitMap, map);
            inverseMap = catalog.getUnitMap(toUnit.getNamespace(), fromUnit.getNamespace());
            assertNull(inverseMap);
        }
    }

    @Test
    public void getUnitMapWithUnmatchedNamespaces() throws Exception {
        assertNotNull(catalog);

        List<UnitMapImpl> unitMaps = catalog.getUnitMaps();
        assertTrue(unitMaps.size() > 0);
        UnitMapImpl unitMap = unitMaps.get(0);
        assertNotNull(unitMap);

        // Set the fromUnit and toUnit with the same namespace
        UnitImpl fromUnit = new UnitImpl();
        fromUnit.setNamespace(unitMap.getFromNamespace());
        UnitImpl toUnit = new UnitImpl();
        toUnit.setNamespace(unitMap.getFromNamespace());
        unitMap = catalog.getUnitMap(fromUnit.getNamespace(), toUnit.getNamespace());
        assertNull(unitMap);
    }

    @Test
    public void geUnitMapWithInvalidFromUnit() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            List<UnitImpl> units = catalog.getUnits();
            UnitImpl fromUnit = new UnitImpl();
            assertNull(fromUnit.getNamespace());
            catalog.getUnitMap(fromUnit.getNamespace(), units.get(0).getNamespace());
            });
    }

    @Test
    public void geUnitMapWithNullFromUnit() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            List<UnitImpl> units = catalog.getUnits();
            catalog.getUnitMap(null, units.get(0).getNamespace());
            });
    }

    @Test
    public void geUnitMapWithInvalidToUnit() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            List<UnitImpl> units = catalog.getUnits();
            UnitImpl toUnit = new UnitImpl();
            assertNull(toUnit.getNamespace());
            catalog.getUnitMap(units.get(0).getNamespace(), toUnit.getNamespace());
            });
    }

    @Test
    public void geUnitMapWithNullToUnit() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            List<UnitImpl> units = catalog.getUnits();
            catalog.getUnitMap(units.get(0).getNamespace(), null);
            });
    }

    @Test
    public void getMapStates() throws Exception {
        assertNotNull(catalog);

        List<MapStateImpl> mapStates = catalog.getWellknownMapStates();
        assertEquals(8, mapStates.size());

        // make sure that the catalog include MapStates 'unresolved' and 'identical'
        boolean unresolvedFound = false;
        boolean identicalFound = false;
        for ( MapStateImpl mapState : catalog.getWellknownMapStates()) {
            if(MapStateHelper.isUnresolved(mapState.getState()))
                unresolvedFound = true;
            if(MapStateHelper.isIdentical(mapState.getState()))
                identicalFound = true;
        }
        assertTrue(unresolvedFound, "'unresolved' map state is missing from the catalog");
        assertTrue(identicalFound, "'identical' map state is missing from the catalog");
    }

    /********************************************
     Test Search related API
     *********************************************/
    @Test
    public void searchUnits() throws Exception {
        assertNotNull(catalog);

        // test symbol with special char '/'
        Result<UnitImpl> result = catalog.searchUnits("mW/degC", 0, -1);
        assertNotNull(result);
        assertEquals(0, result.getOffset());
        assertEquals(1, result.getCount());
        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getItems().size());

        // test symbol with space ' '
        result = catalog.searchUnits("1/96487 mS.m2/C", 0, -1);
        assertNotNull(result);
        assertEquals(0, result.getOffset());
        assertEquals(1, result.getCount());
        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getItems().size());

        // test symbol with special char '(' ')'
        result = catalog.searchUnits("(m/s)2", 0, -1);
        assertNotNull(result);
        assertEquals(0, result.getOffset());
        assertEquals(1, result.getCount());
        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getItems().size());

        // test the offset/limit
        result = catalog.searchUnits("RP66", 0, -1);
        assertNotNull(result);
        assertEquals(0, result.getOffset());
        assertEquals(821, result.getCount());
        assertEquals(821, result.getTotalCount());
        assertEquals(821, result.getItems().size());

        result = catalog.searchUnits("RP66", 10, 100);
        assertNotNull(result);
        assertEquals(10, result.getOffset());
        assertEquals(100, result.getCount());
        assertEquals(821, result.getTotalCount());
        assertEquals(100, result.getItems().size());
    }

    @Test
    public void fieldSearchForUnits() throws Exception {
        assertNotNull(catalog);
        // Field search
        Result<UnitImpl> result = catalog.searchUnits("Namespace:rp66", 0, -1);
        assertEquals(821, result.getCount());

        result = catalog.searchUnits("Symbol:ft", 0, -1);
        assertEquals(11, result.getCount());

        // Generic search
        result = catalog.searchUnits("rp66", 0, -1);
        assertEquals(821, result.getCount());

        result = catalog.searchUnits("ft", 0, -1);
        assertEquals(11, result.getCount());
    }

    @Test
    public void searchMeasurements() throws Exception {
        assertNotNull(catalog);

        // test the offset/limit
        int expectedTotalCount = 690;
        Result<MeasurementImpl> result = catalog.searchMeasurements("length", 0, -1);
        assertNotNull(result);
        assertEquals(0, result.getOffset());
        assertEquals(expectedTotalCount, result.getCount());
        assertEquals(expectedTotalCount, result.getTotalCount());
        assertEquals(expectedTotalCount, result.getItems().size());

        result = catalog.searchMeasurements("length", 20, 80);
        assertNotNull(result);
        assertEquals(20, result.getOffset());
        assertEquals(80, result.getCount());
        assertEquals(690, result.getTotalCount());
        assertEquals(80, result.getItems().size());
    }

    @Test
    public void fieldSearchForMeasurements() throws Exception {
        assertNotNull(catalog);
        // Field search
        Result<MeasurementImpl> result = catalog.searchMeasurements("Ancestry:length", 0, -1);
        assertEquals(22, result.getCount());

        result = catalog.searchMeasurements("Code:length", 0, -1);
        assertEquals(28, result.getCount());

        // Generic search
        result = catalog.searchMeasurements("length", 0, -1);
        assertEquals(690, result.getCount());

        // BaseMeasurement field search
        result = catalog.searchMeasurements("baseMeasurement:false", 0, -1);
        assertEquals(4677, result.getCount());
        result = catalog.searchMeasurements("baseMeasurement:no", 0, -1);
        assertEquals(4677, result.getCount());

        result = catalog.searchMeasurements("baseMeasurement:true", 0, -1);
        assertEquals(266, result.getCount());
        result = catalog.searchMeasurements("baseMeasurement:yes", 0, -1);
        assertEquals(266, result.getCount());
    }

    @Test
    public void search() throws Exception {
        assertNotNull(catalog);

        QueryResult result = catalog.search("State:identical", 0, -1);

        QueryResult unitResult = catalog.search("State:identical Type:unit", 0, -1);

        QueryResult measurementResult = catalog.search("State:identical Type:measurement", 0, -1);

        QueryResult unitMapItemResult = catalog.search("State:identical Type:unitmapitem", 0, -1);

        QueryResult measurementMapItemResult = catalog.search("State:identical Type:measurementmapitem", 0, -1);

        int sum = unitResult.getCount() + measurementResult.getCount() + unitMapItemResult.getCount() + measurementMapItemResult.getCount();
        assertEquals(result.getCount(), sum);
    }

    /********************************************
     Test unit system related API
     *********************************************/
    @Test
    public void getUnitSystemInfo() {
        UnitSystemInfoResponse unitSystemCollection = catalog.getUnitSystemInfoList(0, -1);
        assertNotNull(unitSystemCollection);
        List<UnitSystemInfo> unitSystemInfoList = unitSystemCollection.getUnitSystemInfoList();
        assertNotNull(unitSystemInfoList);
        assertEquals(14, unitSystemInfoList.size());
    }

    @Test
    public void getAndPostMetricUnitSystem() throws Exception {
        String name = "Metric";
        UnitSystemImpl unitSystem = catalog.getUnitSystem(name);
        assertNotNull(unitSystem);
        UnitSystemEssenceImpl essence = new UnitSystemEssenceImpl("Metric");
        UnitSystemImpl unitSystem2 = catalog.postUnitSystem(essence, 0, -1);
        assertNotNull(unitSystem2);
        assertEquals(unitSystem, unitSystem2);
    }

    @Test
    public void getAndPostCanonicalUnitSystem() throws Exception {
        String name = "Canonical";
        UnitSystemImpl unitSystem = catalog.getUnitSystem(name);
        assertNotNull(unitSystem);
        UnitSystemEssenceImpl essence = new UnitSystemEssenceImpl("Canonical");
        UnitSystemImpl unitSystem2 = catalog.postUnitSystem(essence, 0, -1);
        assertNotNull(unitSystem2);
        assertEquals(unitSystem, unitSystem2);
    }

    @Test
    public void getUnitSystemWithInvalidName() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            catalog.getUnitSystem("abc", 0, -1);
            });
    }

    @Test
    public void postUnitSystemWithInvalidEssence() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            UnitSystemEssenceImpl essence = new UnitSystemEssenceImpl("abc");
            catalog.postUnitSystem(essence, 0, -1);
            });
    }

    @Test
    public void getUnitBySystemAndMeasurement() throws Exception {
        assertNotNull(catalog);

        String m1 = "Length";
        String english = "English";
        Unit unit1 = catalog.getUnitBySystemAndMeasurement(english, m1);
        assertNotNull(unit1);

        String m2 = "Length.Millimeter";
        String metric = "Metric";
        Unit unit2 = catalog.getUnitBySystemAndMeasurement(metric, m2);
        assertNotNull(unit2);
    }

    /**
     * test to ensure that the unit-system unit assignments return only units, which have
     * exactly the same base measurement.
     */
	@Test
	public void postUnitBySystemAndMeasurementCheckConsistency() throws Exception {
		assertNotNull(catalog);
		Result<MeasurementImpl> measurementResult = catalog.getMeasurements(0,-1);
        List<MeasurementImpl> measurements = measurementResult.getItems();
		for (UnitSystemImpl unitSystem : catalog.getUnitSystems()) {
			for (MeasurementImpl measurement : measurements) {
				UnitImpl unit = catalog.postUnitBySystemAndMeasurement(unitSystem.getName(), measurement.getEssence());
				if (!unit.getEssence().getBaseMeasurementEssence().toJsonString().equals(measurement.getBaseMeasurementEssenceJson())){
				    continue;
                }
				assertEquals(unit.getEssence().getBaseMeasurementEssence().toJsonString(), measurement.getBaseMeasurementEssenceJson());
			}
		}
    }

	/**
     * Test getting the unit from "Canonical" if the measurement and
     * its ancestry measurements is not defined from the given unit system, e.g. “Metric”
     */
    @Test
    public void postUnitBySystemAndMeasurementFromDefaultUnitSystem() throws Exception {
        assertNotNull(catalog);

        //Measurement only defined in "Canonical" unit system
        //This unit test is to test getting unit from Canonical unit system
        //which is last resort unit system to get the unit from the given measurement
        //if the measurement can not be found from the given unit system
        String essenceJson = "{\"ancestry\":\"L3/T\",\"type\":\"UM\"}";
        String canonical = "Canonical";
        String english = "English";
        MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        Unit unit1 = catalog.postUnitBySystemAndMeasurement(canonical, essence);
        Unit unit2 = catalog.postUnitBySystemAndMeasurement(english, essence);
        assertEquals(unit1, unit2);
    }

    @Test
    public void getUnitBySystemAndMeasurementWithInvalidUnitSystem() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            String m = "Length";
            String english = "abc";
            catalog.getUnitBySystemAndMeasurement(english, m);
            });
    }

    @Test
    public void getUnitBySystemAndMeasurementWithInvalidMeasurement() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            assertNotNull(catalog);

            String m = "abc";
            String english = "English";
            catalog.getUnitBySystemAndMeasurement(english, m);
            });
    }

    /***********************************************************************
     * Tests related to {@link UnitSystemImpl}s that are instantiated from the catalog
     ***********************************************************************/
    @Test
    public void getNativeUnitAssignments() throws Exception {
        UnitSystemImpl awsDecimetricUnitSystem = catalog.getUnitSystem("AWS_Decimetric");
        UnitSystemImpl metricUnitSystem = catalog.getUnitSystem("Metric");

        assertNotNull(awsDecimetricUnitSystem.getNativeUnitAssignments());
        assertNotNull(metricUnitSystem.getNativeUnitAssignments());

        int expectedUnitAssignmentSize = 3897;
        assertEquals(332, awsDecimetricUnitSystem.getNativeUnitAssignments().size());
        assertEquals(expectedUnitAssignmentSize, metricUnitSystem.getNativeUnitAssignments().size());
        UnitSystemImpl canonicalUnitSystem = catalog.getUnitSystem("Canonical");
        assertNotNull(canonicalUnitSystem.getNativeUnitAssignments());
        assertEquals(266, canonicalUnitSystem.getNativeUnitAssignments().size());
    }

    @Test
    public void getUnitAssignments() throws Exception {
        UnitSystemImpl awsDecimetricUnitSystem = catalog.getUnitSystem("AWS_Decimetric");
        UnitSystemImpl metricUnitSystem = catalog.getUnitSystem("Metric");

        assertNotNull(awsDecimetricUnitSystem.getUnitAssignments());
        assertNotNull(metricUnitSystem.getUnitAssignments());
        //Unit system "AWS_Decimetric" inherits from unit system "Metric", so getUnitAssignments() from
        //unit system "AWS_Decimetric" should contain all the measurements from unit system "Metric"
        //Using size to do a quick test, also see test method getNativeUnitAssignments()
        assertTrue(awsDecimetricUnitSystem.getUnitAssignments().size() >= metricUnitSystem.getUnitAssignments().size());

        int expectedUnitAssignmentSize = 3897;
        assertEquals(expectedUnitAssignmentSize, awsDecimetricUnitSystem.getUnitAssignments().size());
        assertEquals(expectedUnitAssignmentSize, metricUnitSystem.getUnitAssignments().size());
    }

    @Test
    public void getUnitAssignmentsIncrementally() throws Exception {
        String aws_decimetric = "AWS_Decimetric";
        UnitSystemImpl us = catalog.getUnitSystem(aws_decimetric);
        HashMap<String, UnitAssignment> oneGo = new HashMap<>();
        HashMap<String, UnitAssignment> multi = new HashMap<>();
        UnitSystemImpl getAll = catalog.getUnitSystem(aws_decimetric, 0, -1);
        for (UnitAssignment ua: getAll.getUnitAssignments()) {
            oneGo.put(ua.getMeasurement().getEssence().getAncestry(), ua);
        }
        int limit = 100;
        int offset = 0;
        int total = us.getUnitAssignments().size();
        while (offset < total) {
            UnitSystemImpl one = catalog.getUnitSystem(aws_decimetric, offset, limit);
            assertEquals(offset, one.getOffset());
            int expected = min(total, offset+limit) - offset;
            assertEquals(expected, one.getUnitAssignmentCountInResponse());
            for (UnitAssignment ua: one.getUnitAssignments()) {
                multi.put(ua.getMeasurement().getEssence().getAncestry(), ua);
            }
            offset += limit;
        }
        assertEquals(us.getUnitAssignmentCountTotal(), multi.size());
        for (String key: oneGo.keySet()) {
            UnitAssignment ua = multi.get(key);
            assertNotNull(ua);
            assertEquals(key, ua.getMeasurement().getEssence().getAncestry());
        }
    }
        @Test
    public void getUnitAssignment() throws Exception {
        UnitSystemImpl awsDecimetricUnitSystem = catalog.getUnitSystem("AWS_Decimetric");
        UnitSystemImpl metricUnitSystem = catalog.getUnitSystem("Metric");
        // Sample measurements defined by unit system "AWS_Decimetric". They are also defined by unit system "Metric"
        // But their unit assignments are different and they should not be overridden by the assignments defined
        // by unit system "Metric"
        String[] canonicalDefinedMeasurements = new String[] {
                "Velocity.Cable_Speed",
                "Length",
                "Dimensionless.Ratio.Volume_Fraction.Porosity"
        };
        for (String ancestry: canonicalDefinedMeasurements) {
            MeasurementImpl measurement = catalog.getMeasurement(ancestry);
            UnitAssignmentImpl awsAssignment = awsDecimetricUnitSystem.getUnitAssignment(measurement);
            UnitAssignmentImpl metricAssignment = metricUnitSystem.getUnitAssignment(measurement);
            assertEquals(measurement, awsAssignment.getMeasurement());
            assertEquals(measurement, metricAssignment.getMeasurement());
            assertFalse(awsAssignment.equals(metricAssignment));
            assertFalse(awsAssignment.getUnit().equals(metricAssignment.getUnit()));
        }

        // Sample measurements defined by unit system "Metric" and inherited by unit system "AWS_Decimetric"
        // Their unit assignments should be the same
        String[] metricDefinedMeasurements = new String[] {
            "Velocity.Cable_Speed",
            "Length",
            "Dimensionless.Ratio.Volume_Fraction.Porosity"
        };
        for (String ancestry: metricDefinedMeasurements) {
            MeasurementImpl measurement = catalog.getMeasurement(ancestry);
            UnitAssignmentImpl awsAssignment = awsDecimetricUnitSystem.getUnitAssignment(measurement);
            UnitAssignmentImpl metricAssignment = metricUnitSystem.getUnitAssignment(measurement);
            assertEquals(measurement, awsAssignment.getMeasurement());
            assertEquals(measurement, metricAssignment.getMeasurement());
            assertFalse(awsAssignment.equals(metricAssignment));
            assertFalse(awsAssignment.getUnit().equals(metricAssignment.getUnit()));
        }
    }

    /**
     * Test getting the unit from the ancestry measurement (e.g. “Length”),
     * if the measurement (e.g. “Length.Cylinder_Diameter.Cylinder_Diameter_Error” ) is not defined
     * from the given unit system
     */
    @Test
    public void postUnitAssignmentFromParentMeasurement() throws Exception {
        String essenceJson = "{\"ancestry\":\"Length.Cylinder_Diameter\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl measurement = catalog.postMeasurement(essence);
        UnitSystemImpl metricUnitSystem = catalog.getUnitSystem("Metric");
        UnitAssignmentImpl unitAssignment = metricUnitSystem.getUnitAssignment(measurement);
        assertNotNull(unitAssignment);
        assertNotNull(measurement.getParent());
        assertTrue(measurement.equals(unitAssignment.getMeasurement()));
        assertFalse(measurement.getParent().equals(unitAssignment.getMeasurement()));
    }

    @Test
    public void getParentUnitSystem() throws Exception {
        UnitSystemImpl awsDecimetricUnitSystem = catalog.getUnitSystem("AWS_Decimetric");
        assertNotNull(awsDecimetricUnitSystem.getParent());
        UnitSystemImpl metricUnitSystem = catalog.getUnitSystem("Metric");
        assertNull(metricUnitSystem.getParent());
        assertEquals(metricUnitSystem, awsDecimetricUnitSystem.getParent());
    }  

    /********************************************
     Test measurement related API
     *********************************************/
    @Test
    public void getMeasurements() throws Exception {
        Result<MeasurementImpl> result = catalog.getMeasurements(0, -1);
        assertEquals(0, result.getOffset());
        result = catalog.getMeasurements(100, -1);
        assertEquals(100, result.getOffset());
        result = catalog.getMeasurements(100, 10000);
        assertEquals(100, result.getOffset());
        result = catalog.getMeasurements(100, 50);
        assertEquals(100, result.getOffset());
        assertEquals(50, result.getCount());
    }

    @Test
    public void getMeasurementsWithNegativeOffset() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            catalog.getMeasurements(-1, -1);
            });
    }

    @Test
    public void getMeasurementsWithOutOfRangeOffset() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            catalog.getMeasurements(100000, -1);
            });
    }

    @Test
    public void getAndPostMeasurement() throws Exception{
        String ancestry = "Length.Millimeter";
        MeasurementImpl m1 = catalog.getMeasurement(ancestry);
        assertNotNull(m1);
        String essenceJson = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl m2 = catalog.postMeasurement(essence);
        assertNotNull(m2);
        assertEquals(m1, m2);
    }

    @Test
    public void postMeasurementWithDeprecatedJsonString() throws Exception {
        String essenceJson = "{\"ancestry\":\"LengthPerRotation\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl m = catalog.postMeasurement(essence);
        assertNotNull(m);
        MeasurementDeprecationInfoImpl deprecationInfo = m.getDeprecationInfo();
        assertNotNull(deprecationInfo);
        assertEquals(MapStateHelper.getUnresolvedMapState().getState(), deprecationInfo.getState());
    }

    @Test
    public void postMeasurementWithEmptyJsonString()
            throws Exception {
                org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            catalog.postMeasurement(null);
                    });
            }

    @Test
    public void getMeasurementWithInvalidCode() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String ancestry = "abc";
            catalog.getMeasurement(ancestry);
            });
    }

    @Test
    public void getAndPostUnitsByMeasurement() throws Exception {
        String baseMeasurementAncestry = "Length";
        Result<UnitImpl> unitResult1 = catalog.getUnitsByMeasurement(baseMeasurementAncestry);
        assertNotNull(unitResult1);
        assertTrue(unitResult1.getCount() > 0);
        String baseMeasurementEssenceJson = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        MeasurementEssenceImpl baseMeasurementEssence = Utility.fromJsonString(baseMeasurementEssenceJson, MeasurementEssenceImpl.class);
        Result<UnitImpl> unitResult2 = catalog.postUnitsByMeasurement(baseMeasurementEssence);
        assertNotNull(unitResult2);
        assertTrue(unitResult2.getCount() > 0);
        assertEquals(unitResult1.getCount(), unitResult2.getCount());
        String childMeasurementAncestry = "Length.Millimeter";
        Result<UnitImpl> unitResult3 = catalog.getUnitsByMeasurement(childMeasurementAncestry);
        assertNotNull(unitResult3);
        assertTrue(unitResult3.getCount() > 0);
        String childMeasurementEssenceJson = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
        MeasurementEssenceImpl childMeasurementEssence = Utility.fromJsonString(childMeasurementEssenceJson, MeasurementEssenceImpl.class);
        Result<UnitImpl> unitResult4 = catalog.postUnitsByMeasurement(childMeasurementEssence);
        assertNotNull(unitResult4);
        assertTrue(unitResult4.getCount() > 0);
        assertEquals(unitResult3.getCount(), unitResult4.getCount());
        assertEquals(unitResult1.getCount(), unitResult3.getCount());
    }

    @Test
    public void postUnitsByMeasurementWithDeprecatedMeasurementEssenceJson() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            // Length.Millimeter => Length.Millimeter1
            String essenceJson = "{\"ancestry\":\"Length.Millimeter1\",\"type\":\"UM\"}";
            MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
            catalog.postUnitsByMeasurement(essence);
            });
    }

    @Test
    public void postUnitsByMeasurementWithInvalidMeasurementEssenceJson() throws Exception  {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String essenceJson = "{\"ancestry2\":\"Length.Millimeter\",\"type\":\"UM\"}";
            MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
            catalog.postUnitsByMeasurement(essence);
            });
    }

    @Test
    public void getUnitsByMeasurementWithInvalidMeasurementAncestry() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String ancestry = "abc";
            catalog.getUnitsByMeasurement(ancestry);
            });
    }

    @Test
    public void getAndPostPreferredUnitsByMeasurement() throws Exception{
        // Base measurement
        int expectedPreferredUnitSize = 6;
        String ancestry = "Pressure_Per_Length";
        Result<UnitImpl> preferredUnits = catalog.getPreferredUnitsByMeasurement(ancestry);
        assertNotNull(preferredUnits);
        assertEquals(expectedPreferredUnitSize, preferredUnits.getCount());
        String essenceJson = "{\"ancestry\":\"Pressure_Per_Length\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence1 = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        preferredUnits = catalog.postPreferredUnitsByMeasurement(essence1);
        assertNotNull(preferredUnits);
        assertEquals(expectedPreferredUnitSize, preferredUnits.getCount());
        // Child measurement
        ancestry = "Pressure_Per_Length.Membrane_Stiffness";
        preferredUnits = catalog.getPreferredUnitsByMeasurement(ancestry);
        assertNotNull(preferredUnits);
        assertEquals(3, preferredUnits.getCount());
        essenceJson = "{\"ancestry\":\"Pressure_Per_Length.Membrane_Stiffness\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence2 = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        preferredUnits = catalog.postPreferredUnitsByMeasurement(essence2);
        assertNotNull(preferredUnits);
        assertEquals(3, preferredUnits.getCount());
    }

    @Test
    public void postPreferredUnitsByMeasurementWithDeprecatedMeasurementEssenceJson() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            // Length.Millimeter => Length.Millimeter1
            String essenceJson = "{\"ancestry\":\"Length.Millimeter1\",\"type\":\"UM\"}";
            MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
            catalog.postPreferredUnitsByMeasurement(essence);
            });
    }

    @Test
    public void postPreferredUnitsByMeasurementWithInvalidMeasurementEssenceJson() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String essenceJson = "{\"ancestry\":\"Length.Millimeter1\",\"type\":\"UM\"}";
            MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
            catalog.postPreferredUnitsByMeasurement(essence);
            });
    }

    @Test
    public void getPreferredUnitsByMeasurementWithInvalidMeasurementCode() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String ancestry = "abc";
            catalog.getPreferredUnitsByMeasurement(ancestry);
            });
    }

    @Test
    public void getAndPostMeasurementInCatalog() throws Exception {
        String ancestry = "Length";
        MeasurementImpl m1 = catalog.getMeasurementInCatalog(ancestry);
        String essenceJson = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl m2 = catalog.postMeasurementInCatalog(essence);
        assertNotNull(m1);
        assertNotNull(m2);
        assertEquals(m1, m2);
    }

    @Test
    public void getMeasurementInCatalogWithInvalidCode() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            catalog.getMeasurementInCatalog("length");
            });
    }

    @Test
    public void postMeasurementInCatalogWithJsonString() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String essenceJson = "{\"ancestry\":\"length\",\"type\":\"UM\"}";
            MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
            catalog.postMeasurementInCatalog(essence);
            });
    }

    /***********************************************************************
     * Tests related to {@link MeasurementImpl}s that are instantiated from the catalog
     ***********************************************************************/
    @Test
    public void postDeserializationForBaseMeasurement() throws Exception {
        String essenceJson = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl measurement = catalog.postMeasurementInCatalog(essence);
        assertEquals(measurement.getEssence().toJsonString(), essenceJson);
        assertEquals(measurement.getBaseMeasurementEssenceJson(), essenceJson);
        assertNull(measurement.getParentEssenceJson());
        assertNotNull(measurement.getUnitEssenceJsons());
        assertNotNull(measurement.getUnits());
        assertTrue(measurement.getUnitEssenceJsons().size() > 0);
        assertEquals(measurement.getUnitEssenceJsons().size(), measurement.getUnits().size());
        int expectedPreferredUnitSize = 9;
        assertNotNull(measurement.getPreferredUnitEssenceJsons());
        assertNotNull(measurement.getPreferredUnits());
        assertEquals(expectedPreferredUnitSize, measurement.getPreferredUnitEssenceJsons().size());
        assertEquals(expectedPreferredUnitSize, measurement.getPreferredUnits().size());
        assertNotNull(measurement.getChildMeasurementEssenceJsons());
        assertTrue(measurement.getChildMeasurementEssenceJsons().size() > 0);
        assertNull(measurement.getDeprecationInfo());
    }

    @Test
    public void postDeserializationForChildMeasurement() throws Exception {
        String essenceJson = "{\"ancestry\":\"L3.permeability length\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl measurement = catalog.postMeasurementInCatalog(essence);
        assertEquals(measurement.getEssence().toJsonString(), essenceJson);
        assertNotNull(measurement.getBaseMeasurementEssenceJson());
        assertNotNull(measurement.getParentEssenceJson());
        assertNotNull(measurement.getUnitEssenceJsons());
        assertNotNull(measurement.getUnits());
        assertEquals(0, measurement.getUnitEssenceJsons().size());
        assertEquals(0, measurement.getUnits().size());
        assertNotNull(measurement.getPreferredUnitEssenceJsons());
        assertNotNull(measurement.getPreferredUnits());
        assertTrue(measurement.getPreferredUnitEssenceJsons().size() > 0);
        assertEquals(measurement.getPreferredUnitEssenceJsons().size(), measurement.getPreferredUnits().size());
        assertNotNull(measurement.getChildMeasurementEssenceJsons());
        assertTrue(measurement.getChildMeasurementEssenceJsons().size() == 0);
        assertNull(measurement.getDeprecationInfo());
    }

    @Test
    public void postDeserializationForDeprecationInfo() throws Exception {
        String essenceJson = "{\"ancestry\":\"Power_Per_Temperature.PowerPerTemperature\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl measurement = catalog.postMeasurementInCatalog(essence);
        MeasurementDeprecationInfoImpl deprecationInfo = measurement.getDeprecationInfo();
        assertNotNull(deprecationInfo);
        String deprecationState = "identical";
        String deprecationRemarks = "Replicated OSDD Unit_Dimension";
        String supersededByUnitMeasurement = "{\"ancestry\":\"Power_Per_Temperature\",\"type\":\"UM\"}";
        assertEquals(deprecationState, deprecationInfo.getState());
        assertEquals(deprecationRemarks, deprecationInfo.getRemarks());
        assertEquals(supersededByUnitMeasurement, deprecationInfo.getSupersededByUnitMeasurement());
    }

    @Test
    public void postParentMeasurement() throws Exception {
        String lengthJsonString = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        MeasurementEssenceImpl lengthEssence = Utility.fromJsonString(lengthJsonString, MeasurementEssenceImpl.class);
        MeasurementImpl lengthMeasurement = catalog.postMeasurement(lengthEssence);
        assertNull(lengthMeasurement.getParent());
        String millimeterJsonString = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
        MeasurementEssenceImpl millimeterEssence = Utility.fromJsonString(millimeterJsonString, MeasurementEssenceImpl.class);
        MeasurementImpl millimeterMeasurement = catalog.postMeasurement(millimeterEssence);
        assertNotNull(millimeterMeasurement.getParent());
        assertEquals(lengthMeasurement, millimeterMeasurement.getParent());
    }

    /********************************************
     Test unit related API
     *********************************************/
    @Test
    public void getUnits() throws Exception {
        final int unitCount = 3695;

        Result<UnitImpl> result = catalog.getUnits(0, -1);
        assertEquals(0, result.getOffset());
        assertEquals(unitCount, result.getCount());
        assertEquals(unitCount, result.getTotalCount());

        result = catalog.getUnits(100, -1);
        assertEquals(100, result.getOffset());
        assertEquals(unitCount - 100, result.getCount());
        assertEquals(unitCount, result.getTotalCount());

        result = catalog.getUnits(100, 10000);
        assertEquals(100, result.getOffset());
        assertEquals(unitCount - 100, result.getCount());
        assertEquals(unitCount, result.getTotalCount());

        result = catalog.getUnits(100, 50);
        assertEquals(100, result.getOffset());
        assertEquals(50, result.getCount());
        assertEquals(unitCount, result.getTotalCount());
    }

    @Test
    public void getUnitsWithNegativeOffset() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            catalog.getUnits(-1, -1);
            });
    }

    @Test
    public void getUnitsWithOutOfRangeOffset() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            final int unitCount = 3695;
            catalog.getUnits(unitCount, -1);
            });
    }

    @Test
    public void postUnit() throws Exception {
        String  rp66JsonString = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl rp66Essence = Utility.fromJsonString(rp66JsonString, UnitEssenceImpl.class);
        UnitImpl unit1 = catalog.postUnit(rp66Essence);
        assertNotNull(unit1);
        assertEquals("ft", unit1.getEssence().getSymbol());
        assertNotNull(unit1.getEssence().getScaleOffset());
        assertNull(unit1.getEssence().getABCD());
        assertNull(unit1.getDeprecationInfo());

        String energisticsJsonString = "{\"abcd\":{\"a\":0.0,\"b\":0.3048,\"c\":1.0,\"d\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"L\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl energisticsEssence = Utility.fromJsonString(energisticsJsonString, UnitEssenceImpl.class);        
        UnitImpl unit2 = catalog.postUnit(energisticsEssence);
        assertNotNull(unit2);
        assertEquals("ft", unit2.getEssence().getSymbol());
        assertNull(unit2.getEssence().getScaleOffset());
        assertNotNull(unit2.getEssence().getABCD());
        assertNull(unit2.getDeprecationInfo());

        assertNotSame(unit1, unit2);
    }

    @Test
    public void postUnitWithDeprecationInfo() throws Exception {
        //AA0.3048 => A0.30481
        String rp66JsonString = "{\"scaleOffset\":{\"scale\":0.30481,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl rp66Essence = Utility.fromJsonString(rp66JsonString, UnitEssenceImpl.class);
        UnitImpl unit1 = catalog.postUnit(rp66Essence);
        assertNotNull(unit1);
        assertEquals("ft", unit1.getEssence().getSymbol());
        assertNotNull(unit1.getEssence().getScaleOffset());
        assertNull(unit1.getEssence().getABCD());
        UnitDeprecationInfoImpl deprecationInfo = unit1.getDeprecationInfo();
        assertNotNull(deprecationInfo);
        assertEquals(MapStateHelper.getUnresolvedMapState().getState(), deprecationInfo.getState());

        //A0.0 => A0.1
        String energisticsJsonString = "{\"abcd\":{\"a\":0.1,\"b\":0.3048,\"c\":1.0,\"d\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"L\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl energisticsEssence = Utility.fromJsonString(energisticsJsonString, UnitEssenceImpl.class);           
        UnitImpl unit2 = catalog.postUnit(energisticsEssence);
        assertNotNull(unit2);
        assertEquals("ft", unit2.getEssence().getSymbol());
        assertNull(unit2.getEssence().getScaleOffset());
        assertNotNull(unit2.getEssence().getABCD());
        deprecationInfo = unit2.getDeprecationInfo();
        assertNotNull(deprecationInfo);
        assertEquals(MapStateHelper.getUnresolvedMapState().getState(), deprecationInfo.getState());

        assertNotSame(unit1, unit2);
    }

    @Test
    public void postUnitWithNullEssence() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            catalog.postUnit(null);
            });
    }

    @Test
    public void getUnitsBySymbol() throws Exception {
        String symbol = "ft";
        Result<UnitImpl> unitResult = catalog.getUnitsBySymbol(symbol);
        assertNotNull(unitResult);
        assertEquals(2, unitResult.getCount());
        for (Unit unit : unitResult.getItems())
            assertEquals("ft", unit.getEssence().getSymbol());
    }

    @Test
    public void getUnitsByInvalidSymbol() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String symbol = "abc";
            catalog.getUnitsBySymbol(symbol);
            });
    }

    @Test
    public void postConversionScaleOffsetInSameNamespace() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);      
        ConversionResultImpl conversionResult = catalog.postConversionScaleOffset(fromUnitEssence, toUnitEssence);
        ScaleOffset scaleOffset = conversionResult.getScaleOffset();
        assertNotNull(scaleOffset);
        assertEquals(0.3048, scaleOffset.getScale());
        assertEquals(0.0, scaleOffset.getOffset());

        conversionResult = catalog.postConversionScaleOffset(toUnitEssence, fromUnitEssence);
        ScaleOffset inverseScaleOffset = conversionResult.getScaleOffset();
        assertNotNull(inverseScaleOffset);
        assertTrue(Math.abs(scaleOffset.getScale() - 1/inverseScaleOffset.getScale()) < 0.00001);
        assertEquals(0.0, inverseScaleOffset.getOffset());
    }

    @Test
    public void postConversionScaleOffsetWithDeprecation() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.30481,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson   = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);      
        ConversionResultImpl conversionResult = catalog.postConversionScaleOffset(fromUnitEssence, toUnitEssence);
        ScaleOffset scaleOffset = conversionResult.getScaleOffset();
        assertNotNull(scaleOffset);
        assertEquals(0.30481, scaleOffset.getScale());
        assertEquals(0.0, scaleOffset.getOffset());

        conversionResult = catalog.postConversionScaleOffset(toUnitEssence, fromUnitEssence);
        ScaleOffset inverseScaleOffset = conversionResult.getScaleOffset();
        assertNotNull(inverseScaleOffset);
        assertTrue(Math.abs(scaleOffset.getScale() - 1/inverseScaleOffset.getScale()) < 0.00001);
        assertEquals(0.0, inverseScaleOffset.getOffset());
    }

    @Test
    public void postConversionScaleOffsetInDifferentNamespace() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":304.8,\"offset\":0.0},\"symbol\":\"1000 ft/s\",\"baseMeasurement\":{\"ancestry\":\"Velocity\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson   = "{\"abcd\":{\"a\":0.0,\"b\":304.8,\"c\":1.0,\"d\":0.0},\"symbol\":\"1000 ft/s\",\"baseMeasurement\":{\"ancestry\":\"L/T\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);              
        ConversionResultImpl conversionResult = catalog.postConversionScaleOffset(fromUnitEssence, toUnitEssence);
        ScaleOffset scaleOffset = conversionResult.getScaleOffset();
        assertNotNull(scaleOffset);
        assertEquals(1.0, scaleOffset.getScale());
        assertEquals(0.0, scaleOffset.getOffset());

        conversionResult = catalog.postConversionScaleOffset(toUnitEssence, fromUnitEssence);
        ScaleOffset inverseScaleOffset = conversionResult.getScaleOffset();
        assertNotNull(inverseScaleOffset);
        assertEquals(1.0, inverseScaleOffset.getScale());
        assertEquals(0.0, inverseScaleOffset.getOffset());
    }

    @Test
    public void postConversionScaleOffsetWithIdenticalUnitsInDifferentNamespaces() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson   = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"L\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);      
        ConversionResultImpl conversionResult = catalog.postConversionScaleOffset(fromUnitEssence, toUnitEssence);
        ScaleOffset scaleOffset = conversionResult.getScaleOffset();
        assertNotNull(scaleOffset);
        assertEquals(0.3048, scaleOffset.getScale());
        assertEquals(0.0, scaleOffset.getOffset());

        conversionResult = catalog.postConversionScaleOffset(toUnitEssence, fromUnitEssence);
        ScaleOffset inverseScaleOffset = conversionResult.getScaleOffset();
        assertNotNull(inverseScaleOffset);
        assertTrue(Math.abs(3.28084 - inverseScaleOffset.getScale()) < 0.00001);
        assertEquals(0.0, inverseScaleOffset.getOffset());
    }

    @Test
    public void postGetConversionScaleOffsetsWithInverseIdenticalUnitsInDifferentNamespaces() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.158987304,\"offset\":0.0},\"symbol\":\"BBL\",\"baseMeasurement\":{\"ancestry\":\"Volume\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson   = "{\"abcd\":{\"a\":0.0,\"b\":0.158987294928,\"c\":1.0,\"d\":0.0},\"symbol\":\"bbl\",\"baseMeasurement\":{\"ancestry\":\"L3\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);              
        //There is a unit map from LIS to Energistics_UoM
        ConversionResultImpl conversionResult = catalog.postConversionScaleOffset(fromUnitEssence, toUnitEssence);
        ScaleOffset scaleOffset = conversionResult.getScaleOffset();
        assertNotNull(scaleOffset);

        //there is not unit map from Energistics_UoM to LIS
        //We should able to use the unit map from LIS to Energistics_UoM to find a unit map item
        conversionResult = catalog.postConversionScaleOffset(toUnitEssence, fromUnitEssence);
        ScaleOffset inverseScaleOffset = conversionResult.getScaleOffset();
        assertNotNull(inverseScaleOffset);
    }

    @Test
    public void postConversionScaleOffsetWithInvalidEssences() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            catalog.postConversionScaleOffset(null, null);
            });
    }

    @Test
    public void postConversionScaleOffsetWithIncompatibleUnits() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"dB\",\"baseMeasurement\":{\"ancestry\":\"Attenuation\",\"type\":\"UM\"},\"type\":\"USO\"}";
            String toUnitEssenceJson   = "{\"abcd\":{\"a\":0.0,\"b\":1.0E-4,\"c\":1.0,\"d\":0.0},\"symbol\":\"dB/km\",\"baseMeasurement\":{\"ancestry\":\"none\",\"type\":\"UM\"},\"type\":\"UAD\"}";
            UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
            UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);              
            catalog.postConversionScaleOffset(fromUnitEssence, toUnitEssence);
            });
    }

    @Test
    public void postConversionScaleOffsetWithBadFromUnit() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.0,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
            String toUnitEssenceJson   = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
            UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
            UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);      
            catalog.postConversionScaleOffset(fromUnitEssence, toUnitEssence);
            });
    }

    @Test
    public void postConversionScaleOffsetWithBadToUnit() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
            String toUnitEssenceJson   = "{\"scaleOffset\":{\"scale\":0.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
            UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
            UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);      
            catalog.postConversionScaleOffset(fromUnitEssence, toUnitEssence);
            });
    }

    @Test
    public void postConversionABCDInSameNamespace() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson   = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);      
        ConversionResultImpl conversionResult = catalog.postConversionABCD(fromUnitEssence, toUnitEssence);
        ABCD abcd = conversionResult.getABCD();
        assertNotNull(abcd);
        assertEquals(0.0, abcd.getA());
        assertEquals(0.3048, abcd.getB());
        assertEquals(1.0, abcd.getC());
        assertEquals(0.0, abcd.getD());

        conversionResult = catalog.postConversionABCD(toUnitEssence, fromUnitEssence);
        ABCD inverseAbcd = conversionResult.getABCD();
        assertNotNull(inverseAbcd);
        assertEquals(0.0, inverseAbcd.getA());
        assertTrue(Math.abs(3.28084 - inverseAbcd.getB()) < 0.00001);
        assertEquals(1.0, inverseAbcd.getC());
        assertEquals(0.0, inverseAbcd.getD());
    }

    @Test
    public void postConversionABCDWithDeprecation() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.30481,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson   = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);      
        ConversionResultImpl conversionResult = catalog.postConversionABCD(fromUnitEssence, toUnitEssence);
        ABCD abcd = conversionResult.getABCD();
        assertNotNull(abcd);
        assertEquals(0.0, abcd.getA());
        assertEquals(0.30481, abcd.getB());
        assertEquals(1.0, abcd.getC());
        assertEquals(0.0, abcd.getD());

        conversionResult = catalog.postConversionABCD(toUnitEssence, fromUnitEssence);
        ABCD inverseAbcd = conversionResult.getABCD();
        assertNotNull(inverseAbcd);
        assertEquals(0.0, inverseAbcd.getA());
        assertTrue(Math.abs(3.28073 - inverseAbcd.getB()) < 0.00001);
        assertEquals(1.0, inverseAbcd.getC());
        assertEquals(0.0, inverseAbcd.getD());
    }

    @Test
    public void postConversionABCDInDifferentNamespaces() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":304.8,\"offset\":0.0},\"symbol\":\"1000 ft/s\",\"baseMeasurement\":{\"ancestry\":\"Velocity\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson   = "{\"abcd\":{\"a\":0.0,\"b\":304.8,\"c\":1.0,\"d\":0.0},\"symbol\":\"1000 ft/s\",\"baseMeasurement\":{\"ancestry\":\"L/T\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);              
        ConversionResultImpl conversionResult = catalog.postConversionABCD(fromUnitEssence, toUnitEssence);
        ABCD abcd = conversionResult.getABCD();
        assertNotNull(abcd);
        assertEquals(0.0, abcd.getA());
        assertEquals(1.0, abcd.getB());
        assertEquals(1.0, abcd.getC());
        assertEquals(0.0, abcd.getD());

        conversionResult = catalog.postConversionABCD(toUnitEssence, fromUnitEssence);
        ABCD inverseAbcd = conversionResult.getABCD();
        assertNotNull(inverseAbcd);
        assertEquals(0.0, inverseAbcd.getA());
        assertEquals(1.0, inverseAbcd.getB());
        assertEquals(1.0, inverseAbcd.getC());
        assertEquals(0.0, inverseAbcd.getD());
    }

    @Test
    public void postConversionABCDWithIdenticalUnitsInDifferentNamespaces() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson   = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"L\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);      
        ConversionResultImpl conversionResult = catalog.postConversionABCD(fromUnitEssence, toUnitEssence);
        ABCD abcd = conversionResult.getABCD();
        assertNotNull(abcd);
        assertEquals(0.0, abcd.getA());
        assertEquals(0.3048, abcd.getB());
        assertEquals(1.0, abcd.getC());
        assertEquals(0.0, abcd.getD());

        conversionResult = catalog.postConversionABCD(toUnitEssence, fromUnitEssence);
        ABCD inverseAbcd = conversionResult.getABCD();
        assertNotNull(inverseAbcd);
        assertNotNull(inverseAbcd);
        assertEquals(0.0, inverseAbcd.getA());
        assertTrue(Math.abs(3.28084 - inverseAbcd.getB()) < 0.00001);
        assertEquals(1.0, inverseAbcd.getC());
        assertEquals(0.0, inverseAbcd.getD());
    }

    @Test
    public void postConversionABCDWithInverseIdenticalUnitsInDifferentNamespaces() throws Exception {
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.158987304,\"offset\":0.0},\"symbol\":\"BBL\",\"baseMeasurement\":{\"ancestry\":\"Volume\",\"type\":\"UM\"},\"type\":\"USO\"}";
        String toUnitEssenceJson   = "{\"abcd\":{\"a\":0.0,\"b\":0.158987294928,\"c\":1.0,\"d\":0.0},\"symbol\":\"bbl\",\"baseMeasurement\":{\"ancestry\":\"L3\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);              
        //There is a unit map from LIS to Energistics_UoM
        ConversionResultImpl conversionResult = catalog.postConversionABCD(fromUnitEssence, toUnitEssence);
        ABCD abcd = conversionResult.getABCD();
        assertNotNull(abcd);

        //there is not unit map from Energistics_UoM to LIS
        //We should able to use the unit map from LIS to Energistics_UoM to find a unit map item
        conversionResult = catalog.postConversionABCD(toUnitEssence, fromUnitEssence);
        ABCD inverseAbcd = conversionResult.getABCD();
        assertNotNull(inverseAbcd);
    }

    @Test
    public void postConversionABCDWithInvalidEssence() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            catalog.postConversionABCD(null, null);
            });
    }

    @Test
    public void postConversionABCDWithIncompatibleUnits() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {

            String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"dB\",\"baseMeasurement\":{\"ancestry\":\"Attenuation\",\"type\":\"UM\"},\"type\":\"USO\"}";
            String toUnitEssenceJson   = "{\"abcd\":{\"a\":0.0,\"b\":1.0E-4,\"c\":1.0,\"d\":0.0},\"symbol\":\"dB/km\",\"baseMeasurement\":{\"ancestry\":\"none\",\"type\":\"UM\"},\"type\":\"UAD\"}";
            UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
            UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);              
            catalog.postConversionABCD(fromUnitEssence, toUnitEssence);
            });
    }

    @Test
    public void postDeserialization() throws Exception {
        String unitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"FT\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl unitEssence = Utility.fromJsonString(unitEssenceJson, UnitEssenceImpl.class);     
        UnitImpl unit = catalog.postUnit(unitEssence);
        assertNotNull(unit);
        assertNotNull(unit.getEssence());
        assertNotNull(unit.getEssence().getBaseMeasurement());
        assertNotNull(unit.getEssence().getScaleOffset());
        assertNull(unit.getEssence().getABCD());
        assertEquals("FT", unit.getEssence().getSymbol());
        assertEquals(unitEssenceJson, unit.getEssence().toJsonString());

        String baseMeasurementEssenceJson = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        assertEquals(unitEssence.getBaseMeasurementEssence().toJsonString(), baseMeasurementEssenceJson);
        UnitDeprecationInfoImpl deprecationInfo = unit.getDeprecationInfo();
        assertNotNull(deprecationInfo);
        assertNotNull(deprecationInfo.getState());
        assertNotNull(deprecationInfo.getRemarks());
        assertNotNull(deprecationInfo.getSupersededByUnit());
    }
 
}
