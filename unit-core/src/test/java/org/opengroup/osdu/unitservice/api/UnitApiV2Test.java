package org.opengroup.osdu.unitservice.api;

import java.util.ArrayList;
import java.util.List;

import org.opengroup.osdu.unitservice.interfaces.*;
import org.opengroup.osdu.unitservice.model.*;
import org.opengroup.osdu.unitservice.model.extended.*;
import org.opengroup.osdu.unitservice.request.*;
import org.opengroup.osdu.unitservice.util.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.opengroup.osdu.unitservice.helper.Utility;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UnitApiV2Test {
 
    final static double delta = 0.0000001;

    @Mock
	private CatalogImpl	catalogMock;
    private UnitApiV2 unitApi;

    @BeforeEach
    public void setUp() {
        unitApi = new UnitApiV2(catalogMock);
    }

    /********************************************
     Test Catalog related API
     *********************************************/
    
    @Test
    public void getCatalog() {
        assertNotNull(unitApi);
        Catalog catalog = unitApi.getCatalog();
        assertNull(catalog);
    }

    @Test
    public void getLastModified() {
        assertNotNull(unitApi);
        String lastModified = "2018-08-22T06:37:14.7654645Z";
        when(catalogMock.getLastModified()).thenReturn(lastModified);
        CatalogLastModified result = unitApi.getLastModified();
        assertEquals(lastModified, result.getLastModified());
    }

    /********************************************
     Test Measurement related API
     *********************************************/
    @Test
    public void getMeasurements() throws Exception {
        assertNotNull(unitApi);
        List<MeasurementImpl> measurementList = new ArrayList<>();
        measurementList.add(new MeasurementImpl());
        Result<MeasurementImpl> measurementImplResult = new Result<>(measurementList, 0, 1);
        when(catalogMock.getMeasurements(0, -1)).thenReturn(measurementImplResult);
        QueryResult result = unitApi.getMeasurements(0, -1);
        assertNotNull(result);
        assertEquals(1, result.getMeasurements().size());
        assertEquals(measurementList.get(0), result.getMeasurements().get(0));
        assertEquals(0, result.getOffset());
        assertEquals(1, result.getTotalCount());
    }

    @Test
    public void getMeasurementsWithInvalidOffset() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.getMeasurements(-1, 10)).thenThrow(AppException.class);
            unitApi.getMeasurements(-1, 10);
            });
    }

    @Test
    public void getMeasurementsWithInvalidLimit() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.getMeasurements(0, -2)).thenThrow(AppException.class);
            unitApi.getMeasurements(0, -2);
            });
    }

    @Test
    public void getMeasurementsWithOutOfRangeOffset() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.getMeasurements(anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.getMeasurements(5, 10);
            });
    }

    @Test
    public void getMeasurement() throws Exception  {
        assertNotNull(unitApi);
        String ancestry = "Length.Millimeter";
        MeasurementImpl measurement = new MeasurementImpl();
        when(catalogMock.getMeasurement(ancestry)).thenReturn(measurement);
        Measurement result = unitApi.getMeasurement(ancestry);
        assertNotNull(result);
        assertEquals(measurement, result);
    }

    @Test
    public void postMeasurement() throws Exception  {
        assertNotNull(unitApi);
        String essenceJson = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        MeasurementRequest request = new MeasurementRequest(essence, null);
        MeasurementImpl measurement = new MeasurementImpl();
        when(catalogMock.postMeasurement(essence)).thenReturn(measurement);
        Measurement result = unitApi.postMeasurement(request);
        assertNotNull(result);
        assertEquals(measurement, result);
    }

    @Test
    public void postMeasurementWithInvalidEssence() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            String essenceJson = "{\"test\":\"Length.Millimeter\",\"type\":\"UM\"}";
            MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
            MeasurementRequest  request = new MeasurementRequest(essence, null);
            when(catalogMock.postMeasurement(essence)).thenThrow(AppException.class);
            unitApi.postMeasurement(request);
            });
    }

    @Test
    public void getMeasurementWithInvalidCode() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String ancestry = "abc";
            when(catalogMock.getMeasurement(ancestry)).thenThrow(AppException.class);
            unitApi.getMeasurement(ancestry);
            });
    }

    /********************************************
     Test Unit related API
     *********************************************/
    @Test
    public void getUnits() throws Exception  {
        assertNotNull(unitApi);
        List<UnitImpl> unitList = new ArrayList<>();
        unitList.add(new UnitImpl());
        Result<UnitImpl> unitResult = new Result<>(unitList, 0, 1);
        when(catalogMock.getUnits(0, -1)).thenReturn(unitResult);
        QueryResult result = unitApi.getUnits(0, -1);
        assertNotNull(result);
        assertEquals(1, result.getUnits().size());
        assertEquals(unitList.get(0), result.getUnits().get(0));
        assertEquals(0, result.getOffset());
        assertEquals(1, result.getTotalCount());
        assertEquals(result.getCount(), result.getUnits().size());
        assertEquals(0, result.getMeasurements().size());
        assertEquals(0, result.getUnitMapItems().size());
        assertEquals(0, result.getMeasurementMapItems().size());
    }

    @Test
    public void getUnitsWithInvalidOffset() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.getUnits(anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.getUnits(-1, 10);
            });
    }

    @Test
    public void getUnitsWithInvalidLimit() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.getUnits(anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.getUnits(0, -2);
            });
    }

    @Test
    public void getUnitsWithOutOfRangeOffset() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.getUnits(anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.getUnits(12, 10);
            });
    }

    @Test
    public void postRP66Unit() throws Exception {
        assertNotNull(unitApi);

        String essenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl essence = Utility.fromJsonString(essenceJson, UnitEssenceImpl.class);
        UnitRequest request = new UnitRequest(essence, null);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(essence);
        when(catalogMock.postUnit(essence)).thenReturn(unit);
        Unit result = unitApi.postUnit(request);
        assertNotNull(result);
        assertEquals(essence, result.getEssence());
        assertNull(result.getDeprecationInfo());
        assertEquals(unit.getDeprecationInfo(), result.getDeprecationInfo());
    }

    @Test
    public void postEnergisticsUnit() throws Exception {
        assertNotNull(unitApi);
        String essenceJson = "{\"abcd\":{\"a\":0.0,\"b\":0.3048,\"c\":1.0,\"d\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"L\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl essence = Utility.fromJsonString(essenceJson, UnitEssenceImpl.class);
        UnitRequest  request = new UnitRequest(essence, null);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(essence);
        when(catalogMock.postUnit(essence)).thenReturn(unit);
        Unit result = unitApi.postUnit(request);
        assertNotNull(result);
        assertEquals(essence, result.getEssence());
        assertNull(result.getDeprecationInfo());
        assertEquals(unit.getDeprecationInfo(), result.getDeprecationInfo());
    }

    @Test
    public void postUnitWithInvalidEssence() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String essenceJson = "{7B\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
            UnitEssenceImpl essence = Utility.fromJsonString(essenceJson, UnitEssenceImpl.class);
            UnitRequest  request = new UnitRequest(essence, null);
            when(catalogMock.postUnit(essence)).thenThrow(AppException.class);
            unitApi.postUnit(request);
            });
    }

    @Test
    public void getUnitsBySymbol() throws Exception {
        assertNotNull(unitApi);
        String essenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl essence = Utility.fromJsonString(essenceJson, UnitEssenceImpl.class);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(essence);
        List<UnitImpl> units = new ArrayList<>();
        Result<UnitImpl> unitResult = new Result<>(units, 0, 1);
        units.add(unit);
        String symbol = "ft";
        when(catalogMock.getUnitsBySymbol(symbol)).thenReturn(unitResult);
        QueryResult result = unitApi.getUnitsBySymbol(symbol);
        assertNotNull(result);
        assertEquals(1, result.getCount());
        assertEquals("ft", result.getUnits().get(0).getEssence().getSymbol());
    }

    @Test
    public void getUnitsByInvalidSymbol() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String symbol = "abc";
            when(catalogMock.getUnitsBySymbol(symbol)).thenThrow(AppException.class);
            unitApi.getUnitsBySymbol(symbol);
            });
    }

    @Test
    public void getUnitBySymbol() throws Exception {
        assertNotNull(unitApi);
        String essenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl essence = Utility.fromJsonString(essenceJson, UnitEssenceImpl.class);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(essence);
        String namespaces = "LIS,RP66";
        String symbol = "ft";
        when(catalogMock.getUnitBySymbol(namespaces, symbol)).thenReturn(unit);
        Unit result = unitApi.getUnitBySymbol(namespaces, symbol);
        assertNotNull(result);
        assertEquals(essence, result.getEssence());
    }

    @Test
    public void getUnitBySymbolWithUnmatchedNamespaces() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String namespaces = "LIS";
            String symbol = "ft";
            when(catalogMock.getUnitBySymbol(namespaces, symbol)).thenThrow(AppException.class);
            unitApi.getUnitBySymbol(namespaces, symbol);
            });
    }

    @Test
    public void getUnitBySymbolWithInvalidSymbol() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String namespaces = "LIS,RP66";
            String symbol = "abc";
            when(catalogMock.getUnitBySymbol(namespaces, symbol)).thenThrow(AppException.class);
            unitApi.getUnitBySymbol(namespaces, symbol);
            });
    }

    @Test
    public void getUnitsByMeasurement() throws Exception {
        assertNotNull(unitApi);
        String rp66EssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl unitEssence = Utility.fromJsonString(rp66EssenceJson, UnitEssenceImpl.class);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(unitEssence);
        List<UnitImpl> units = new ArrayList<>();
        Result<UnitImpl> unitResult = new Result<>(units, 0, 1);
        units.add(unit);

        String bmAncestry = "Length";
        when(catalogMock.getUnitsByMeasurement(bmAncestry)).thenReturn(unitResult);
        QueryResult unitResult1 = unitApi.getUnitsByMeasurement(bmAncestry);
        assertNotNull(unitResult1);
        assertEquals(1, unitResult1.getCount());
        assertEquals(unitEssence, unitResult1.getUnits().get(0).getEssence());

        String bmEssenceJson = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        MeasurementEssenceImpl bmEssence = Utility.fromJsonString(bmEssenceJson, MeasurementEssenceImpl.class);
        MeasurementRequest bmRequest = new MeasurementRequest(bmEssence, null);
        when(catalogMock.postUnitsByMeasurement(bmEssence)).thenReturn(unitResult);
        QueryResult unitResult2 = unitApi.postUnitsByMeasurement(bmRequest);
        assertNotNull(unitResult2);
        assertTrue(unitResult2.getCount() > 0);
        assertEquals(unitResult1.getCount(), unitResult2.getCount());
        assertEquals(1, unitResult2.getCount());
        assertEquals(unitEssence, unitResult2.getUnits().get(0).getEssence());

        String cmAncestry = "Length.Millimeter";
        when(catalogMock.getUnitsByMeasurement(cmAncestry)).thenReturn(unitResult);
        QueryResult unitResult3 = unitApi.getUnitsByMeasurement(cmAncestry);
        assertNotNull(unitResult3);
        assertTrue(unitResult3.getCount() > 0);

        String cmEssenceJson = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
        MeasurementEssenceImpl cmEssence = Utility.fromJsonString(cmEssenceJson, MeasurementEssenceImpl.class);
        MeasurementRequest cmRequest = new MeasurementRequest(cmEssence, null);
        when(catalogMock.postUnitsByMeasurement(cmEssence)).thenReturn(unitResult);
        QueryResult unitResult4 = unitApi.postUnitsByMeasurement(cmRequest);
        assertNotNull(unitResult4);
        assertTrue(unitResult4.getCount() > 0);
        assertEquals(unitResult3.getCount(), unitResult4.getCount());

        assertEquals(unitResult1.getCount(), unitResult3.getCount());
    }

    @Test
    public void getUnitsByMeasurementWithInvalidMeasurementEssence() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            String essenceJson = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
            MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
            MeasurementRequest request = new MeasurementRequest(essence, null);
            when(catalogMock.postUnitsByMeasurement(essence)).thenThrow(AppException.class);
            unitApi.postUnitsByMeasurement(request);
            });
    }

    @Test
    public void getUnitsByMeasurementWithInvalidMeasurementCode() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            String ancestry = "abc";
            when(catalogMock.getUnitsByMeasurement(ancestry)).thenThrow(AppException.class);
            unitApi.getUnitsByMeasurement(ancestry);
            });
    }

    @Test
    public void getPreferredUnitsByMeasurement() throws Exception {
        assertNotNull(unitApi);
        String rp66EssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl unitEssence = Utility.fromJsonString(rp66EssenceJson, UnitEssenceImpl.class);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(unitEssence);
        List<UnitImpl> units = new ArrayList<>();
        Result<UnitImpl> unitResult = new Result<>(units, 0, 1);
        units.add(unit);

        String bmAncestry = "Pressure_Per_Length";
        when(catalogMock.getPreferredUnitsByMeasurement(bmAncestry)).thenReturn(unitResult);
        QueryResult unitResult1 = unitApi.getPreferredUnitsByMeasurement(bmAncestry);
        assertNotNull(unitResult1);
        assertEquals(1, unitResult1.getCount());
        assertEquals(unitEssence, unitResult1.getUnits().get(0).getEssence());

        String bmEssenceJson = "{\"ancestry\":\"Pressure_Per_Length\",\"type\":\"UM\"}";
        MeasurementEssenceImpl bmEssence = Utility.fromJsonString(bmEssenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl bm = new MeasurementImpl();
        bm.setEssence(bmEssence);
        when(catalogMock.postPreferredUnitsByMeasurement(bmEssence)).thenReturn(unitResult);
        MeasurementRequest bmRequest = new MeasurementRequest(bmEssence, null);
        QueryResult unitResult2 = unitApi.postPreferredUnitsByMeasurement(bmRequest);
        assertNotNull(unitResult2);
        assertTrue(unitResult2.getCount() > 0);
        assertEquals(unitResult1.getCount(), unitResult2.getCount());
        assertEquals(1, unitResult2.getCount());
        assertEquals(unitEssence, unitResult2.getUnits().get(0).getEssence());

        String cmAncestry = "Pressure_Per_Length.Membrane_Stiffness";
        when(catalogMock.getPreferredUnitsByMeasurement(cmAncestry)).thenReturn(unitResult);
        QueryResult unitResult3 = unitApi.getPreferredUnitsByMeasurement(cmAncestry);
        assertNotNull(unitResult3);
        assertTrue(unitResult3.getCount() > 0);

        String cmEssenceJson = "{\"ancestry\":\"Pressure_Per_Length.Membrane_Stiffness\",\"type\":\"UM\"}";
        MeasurementEssenceImpl cmEssence = Utility.fromJsonString(cmEssenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl cm = new MeasurementImpl();
        cm.setEssence(cmEssence);
        when(catalogMock.postPreferredUnitsByMeasurement(cmEssence)).thenReturn(unitResult);
        MeasurementRequest cmRequest = new MeasurementRequest(cmEssence, null);
        QueryResult unitResult4 = unitApi.postPreferredUnitsByMeasurement(cmRequest);
        assertNotNull(unitResult4);
        assertTrue(unitResult4.getCount() > 0);
        assertEquals(unitResult3.getCount(), unitResult4.getCount());

        assertEquals(unitResult1.getCount(), unitResult3.getCount());
    }

    @Test
    public void postPreferredUnitsByMeasurementWithInvalidMeasurementEssence() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String essenceJson = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
            MeasurementEssenceImpl essence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
            when(catalogMock.postPreferredUnitsByMeasurement(essence)).thenThrow(AppException.class);
            MeasurementRequest request = new MeasurementRequest(essence, null);
            unitApi.postPreferredUnitsByMeasurement(request);
            });
    }

    @Test
    public void getPreferredUnitsByMeasurementWithInvalidMeasurementCode() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String ancestry = "abc";
            when(catalogMock.getPreferredUnitsByMeasurement(ancestry)).thenThrow(AppException.class);
            unitApi.getPreferredUnitsByMeasurement(ancestry);
            });
    }

    @Test
    public void getUnitBySystemAndMeasurement() throws Exception {
        assertNotNull(unitApi);
        String rp66EssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl unitEssence = Utility.fromJsonString(rp66EssenceJson, UnitEssenceImpl.class);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(unitEssence);

        String m1 = "Length";
        String english = "English";
        when(catalogMock.getUnitBySystemAndMeasurement(english, m1)).thenReturn(unit);
        Unit result1 = unitApi.getUnitBySystemAndMeasurement(english, m1);
        assertNotNull(result1);
        assertEquals(unit, result1);

        String m2 = "Length.Millimeter";
        String metric = "Metric";
        when(catalogMock.getUnitBySystemAndMeasurement(metric, m2)).thenReturn(unit);
        Unit result2 = unitApi.getUnitBySystemAndMeasurement(metric, m2);
        assertNotNull(result2);
        assertEquals(unit, result2);
    }

	/**
     * Test getting the unit from "Canonical" if the measurement and
     * its ancestry measurements is not defined from the given unit system, e.g. “Metric”
     */
    @Test
    public void postUnitBySystemAndMeasurementFromDefaultUnitSystem() throws Exception {
        assertNotNull(unitApi);
        String rp66EssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl unitEssence = Utility.fromJsonString(rp66EssenceJson, UnitEssenceImpl.class);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(unitEssence);
        //Measurement only defined in "Canonical" unit system
        //This unit test is to test getting unit from Canonical unit system
        //which is last resort unit system to get the unit from the given measurement
        //if the measurement can not be found from the given unit system
        String measurementEssenceJson = "{\"ancestry\":\"L3/T\",\"type\":\"UM\"}";
        String canonical = "Canonical";
        String english = "English";
        MeasurementEssenceImpl essence = Utility.fromJsonString(measurementEssenceJson, MeasurementEssenceImpl.class);
        when(catalogMock.postUnitBySystemAndMeasurement(canonical, essence)).thenReturn(unit);
        when(catalogMock.postUnitBySystemAndMeasurement(english, essence)).thenReturn(unit);
        MeasurementRequest request = new MeasurementRequest(essence, null);
        Unit unit1 = unitApi.postUnitBySystemAndMeasurement(canonical, request);
        Unit unit2 = unitApi.postUnitBySystemAndMeasurement(english, request);
        assertEquals(unit1, unit2);
    }

    @Test
    public void postUnitBySystemAndMeasurementWithInvalidUnitSystem() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            String measurementEssenceJson = "{\"ancestry\":\"L3/T\",\"type\":\"UM\"}";
            String canonical = "Canonical";
            String english = "abc";
            MeasurementEssenceImpl essence = Utility.fromJsonString(measurementEssenceJson, MeasurementEssenceImpl.class);
            MeasurementRequest request = new MeasurementRequest(essence, null);
            when(catalogMock.postUnitBySystemAndMeasurement(english, essence)).thenThrow(AppException.class);
            unitApi.postUnitBySystemAndMeasurement(english, request);
            });
    }

    @Test
    public void getUnitBySystemAndMeasurementWithInvalidUnitSystem() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String m = "Length";
            String english = "abc";
            when(catalogMock.getUnitBySystemAndMeasurement(english, m)).thenThrow(AppException.class);
            unitApi.getUnitBySystemAndMeasurement(english, m);
            });
    }

    @Test
    public void getUnitBySystemAndMeasurementWithInvalidMeasurement() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String m = "abc";
            String english = "English";
            when(catalogMock.getUnitBySystemAndMeasurement(english, m)).thenThrow(AppException.class);
            unitApi.getUnitBySystemAndMeasurement(english, m);
            });
    }

    @Test
    public void postConversionScaleOffset() throws Exception {
        assertNotNull(unitApi);
        double  scale = 0.3048;
        double  offset = 0.0;
        ScaleOffsetImpl scaleOffset = new ScaleOffsetImpl(scale, offset);
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitImpl fromUnit = new UnitImpl();
        fromUnit.setEssence(fromUnitEssence);
        String toUnitEssenceJson = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);
        UnitImpl toUnit = new UnitImpl();
        toUnit.setEssence(toUnitEssence);
        ConversionResultImpl conversionResult1 = new ConversionResultImpl((ScaleOffset)scaleOffset, (Unit)fromUnit, (Unit)toUnit);
        when(catalogMock.postConversionScaleOffset(fromUnitEssence, toUnitEssence)).thenReturn(conversionResult1);
        ConversionScaleOffsetRequest request1 = new ConversionScaleOffsetRequest(fromUnitEssence, toUnitEssence, null, null);
        ConversionResult result1 = unitApi.postConversionScaleOffset(request1);
        ScaleOffset scaleOffset1 = result1.getScaleOffset();
        assertNotNull(scaleOffset1);
        assertEquals(scale, scaleOffset1.getScale(), delta);
        assertEquals(offset, scaleOffset1.getOffset(), delta);

        double  scale2 = 1.0;
        double  offset2 = 0.0;
        ScaleOffsetImpl scaleOffset2 = new ScaleOffsetImpl(scale2, offset2);
        ConversionResultImpl conversionResult2 = new ConversionResultImpl((ScaleOffset)scaleOffset2, (Unit)toUnit, (Unit)fromUnit);
        when(catalogMock.postConversionScaleOffset(toUnitEssence, fromUnitEssence)).thenReturn(conversionResult2);
        ConversionScaleOffsetRequest request2 = new ConversionScaleOffsetRequest(toUnitEssence, fromUnitEssence, null, null);
        ConversionResult    result2 = unitApi.postConversionScaleOffset(request2);
        ScaleOffset inverseScaleOffset = result2.getScaleOffset();
        assertNotNull(inverseScaleOffset);
        assertTrue(Math.abs(scaleOffset2.getScale() - 1/inverseScaleOffset.getScale()) < delta);
        assertEquals(0.0, inverseScaleOffset.getOffset(), delta);
    }

    @Test
    public void postConversionScaleOffsetByEssencesWithInvalidEssence() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            when(catalogMock.postConversionScaleOffset(any(), any())).thenThrow(AppException.class);
            ConversionScaleOffsetRequest request = new ConversionScaleOffsetRequest(null, null, null, null);
            unitApi.postConversionScaleOffset(request);
            });
    }

    @Test
    public void postConversionScaleOffsetWithIncompatibleUnits() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"dB\",\"baseMeasurement\":{\"ancestry\":\"Attenuation\",\"type\":\"UM\"},\"type\":\"USO\"}";
            String toUnitEssenceJson   = "{\"abcd\":{\"a\":0.0,\"b\":1.0E-4,\"c\":1.0,\"d\":0.0},\"symbol\":\"dB/km\",\"baseMeasurement\":{\"ancestry\":\"none\",\"type\":\"UM\"},\"type\":\"UAD\"}";
            UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
            UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);       
            when(catalogMock.postConversionScaleOffset(fromUnitEssence, toUnitEssence)).thenThrow(AppException.class);
            ConversionScaleOffsetRequest request = new ConversionScaleOffsetRequest(fromUnitEssence, toUnitEssence, null, null);
            unitApi.postConversionScaleOffset(request);
            });
    }

    @Test
    public void postConversionABCD() throws Exception {
        assertNotNull(unitApi);
        double  a = 0.0;
        double  b = 1.0E-4;
        double  c = 1.0;
        double  d = 0.0;
        ABCDImpl abcd = new ABCDImpl(a, b, c, d);
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"dB\",\"baseMeasurement\":{\"ancestry\":\"Attenuation\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitImpl fromUnit = new UnitImpl();
        fromUnit.setEssence(fromUnitEssence);
        String toUnitEssenceJson = "{\"abcd\":{\"a\":0.0,\"b\":1.0E-4,\"c\":1.0,\"d\":0.0},\"symbol\":\"dB/km\",\"baseMeasurement\":{\"ancestry\":\"none\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);
        UnitImpl toUnit = new UnitImpl();
        toUnit.setEssence(toUnitEssence);
        ConversionResultImpl conversionResult1 = new ConversionResultImpl((ABCD)abcd, (Unit)fromUnit, (Unit)toUnit);
        when(catalogMock.postConversionABCD(fromUnitEssence, toUnitEssence)).thenReturn(conversionResult1);
        ConversionABCDRequest request1 = new ConversionABCDRequest(fromUnitEssence, toUnitEssence, null, null);
        ConversionResult result1 = unitApi.postConversionABCD(request1);
        ABCD abcd1 = result1.getABCD();
        assertNotNull(abcd1);
        assertEquals(0.0, abcd1.getA(), delta);
        assertEquals(1.0E-4, abcd1.getB(), delta);
        assertEquals(1.0, abcd1.getC(), delta);
        assertEquals(0.0, abcd1.getD(), delta);

        ConversionResultImpl conversionResult2 = new ConversionResultImpl((ABCD)abcd, (Unit)toUnit, (Unit)fromUnit);
        when(catalogMock.postConversionABCD(toUnitEssence, fromUnitEssence)).thenReturn(conversionResult2);
        ConversionABCDRequest request2 = new ConversionABCDRequest(toUnitEssence, fromUnitEssence, null, null);
        ConversionResult    result2 = unitApi.postConversionABCD(request2);
        ABCD inverseAbcd = result2.getABCD();
        assertNotNull(inverseAbcd);
        assertEquals(0.0, inverseAbcd.getA(), delta);
        assertTrue(Math.abs(1.0E-4 - inverseAbcd.getB()) < 0.00001);
        assertEquals(1.0, inverseAbcd.getC(), delta);
        assertEquals(0.0, inverseAbcd.getD(), delta);
    }

    @Test
    public void postConversionABCDWithInvalidEssence() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.postConversionABCD(any(), any())).thenThrow(AppException.class);
            ConversionABCDRequest request = new ConversionABCDRequest(null, null, null, null);
            unitApi.postConversionABCD(request);
            });
    }

    @Test
    public void postConversionABCDWithIncompatibleUnits() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);

            String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"dB\",\"baseMeasurement\":{\"ancestry\":\"Attenuation\",\"type\":\"UM\"},\"type\":\"USO\"}";
            String toUnitEssenceJson   = "{\"abcd\":{\"a\":0.0,\"b\":1.0E-4,\"c\":1.0,\"d\":0.0},\"symbol\":\"dB/km\",\"baseMeasurement\":{\"ancestry\":\"none\",\"type\":\"UM\"},\"type\":\"UAD\"}";
            UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
            UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);              
            when(catalogMock.postConversionABCD(fromUnitEssence, toUnitEssence)).thenThrow(AppException.class);
            ConversionABCDRequest request = new ConversionABCDRequest(fromUnitEssence, toUnitEssence, null, null);
            unitApi.postConversionABCD(request);
            });
    }

    @Test
    public void getConversionScaleOffsetBySymbols() throws Exception {
        assertNotNull(unitApi);
        double  scale = 0.3048;
        double  offset = 0.0;
        ScaleOffsetImpl scaleOffset = new ScaleOffsetImpl(scale, offset);
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitImpl fromUnit = new UnitImpl();
        fromUnit.setEssence(fromUnitEssence);
        String toUnitEssenceJson = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"m\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);
        UnitImpl toUnit = new UnitImpl();
        toUnit.setEssence(toUnitEssence);
        ConversionResultImpl conversionResult = new ConversionResultImpl((ScaleOffset)scaleOffset, (Unit)fromUnit, (Unit)toUnit);
        String namespaces = "LIS,ECL,RP66";
        String fromSymbol = "F";
        String toSymbol = "m";
        when(catalogMock.getConversionScaleOffsetBySymbols(namespaces, fromSymbol, toSymbol)).thenReturn(conversionResult);
        ConversionResult result = unitApi.getConversionScaleOffsetBySymbols(namespaces, fromSymbol, toSymbol);
        ScaleOffset scaleOffset2 = result.getScaleOffset();
        assertEquals(scale, scaleOffset2.getScale(), delta);
        assertEquals(offset, scaleOffset2.getOffset(), delta);
    }

    @Test
    public void getConversionScaleOffsetBySymbolsWithIncompleteNamespaces() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            String namespaces = "ECL,RP66";
            String fromSymbol = "F";
            String toSymbol = "m";
            when(catalogMock.getConversionScaleOffsetBySymbols(namespaces, fromSymbol, toSymbol)).thenThrow(AppException.class);
            unitApi.getConversionScaleOffsetBySymbols(namespaces, fromSymbol, toSymbol);
            });
    }

    @Test
    public void getConversionABCDBySymbols() throws Exception {
        assertNotNull(unitApi);
        double  a = 0.0;
        double  b = 1.0E-4;
        double  c = 1.0;
        double  d = 0.0;
        ABCDImpl abcd = new ABCDImpl(a, b, c, d);
        String fromUnitEssenceJson = "{\"scaleOffset\":{\"scale\":1.0,\"offset\":0.0},\"symbol\":\"dB\",\"baseMeasurement\":{\"ancestry\":\"Attenuation\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl fromUnitEssence = Utility.fromJsonString(fromUnitEssenceJson, UnitEssenceImpl.class);
        UnitImpl fromUnit = new UnitImpl();
        fromUnit.setEssence(fromUnitEssence);
        String toUnitEssenceJson = "{\"abcd\":{\"a\":0.0,\"b\":1.0E-4,\"c\":1.0,\"d\":0.0},\"symbol\":\"dB/km\",\"baseMeasurement\":{\"ancestry\":\"none\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl toUnitEssence = Utility.fromJsonString(toUnitEssenceJson, UnitEssenceImpl.class);
        UnitImpl toUnit = new UnitImpl();
        toUnit.setEssence(toUnitEssence);
        ConversionResultImpl conversionResult1 = new ConversionResultImpl((ABCD)abcd, (Unit)fromUnit, (Unit)toUnit);
        String namespaces = "LIS,ECL,RP66";
        String fromSymbol = "F";
        String toSymbol = "m";
        when(catalogMock.getConversionABCDBySymbols(namespaces, fromSymbol, toSymbol)).thenReturn(conversionResult1);
        ConversionResult result = unitApi.getConversionABCDBySymbols(namespaces, fromSymbol, toSymbol);
        ABCD abcd2 = result.getABCD();
        assertEquals(0.0, abcd2.getA(), delta);
        assertEquals(1.0E-4, abcd2.getB(), delta);
        assertEquals(1.0, abcd2.getC(), delta);
        assertEquals(0.0, abcd2.getD(), delta);
    }

    @Test
    public void getConversionABCDBySymbolsWithIncompleteNamespaces() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            String namespaces = "ECL,RP66";
            String fromSymbol = "F";
            String toSymbol = "m";
            when(catalogMock.getConversionABCDBySymbols(namespaces, fromSymbol, toSymbol)).thenThrow(AppException.class);
            unitApi.getConversionABCDBySymbols(namespaces, fromSymbol, toSymbol);
            });
    }

    /********************************************
     Test UnitSystem related API
     *********************************************/
    @Test
    public void getUnitSystemNames() {
        assertNotNull(unitApi);
        List<UnitSystemInfo> unitSystemNames = new ArrayList<>();
        unitSystemNames.add(new UnitSystemInfo("English", "d1", "Metric.English", "pr1"));
        unitSystemNames.add(new UnitSystemInfo("Metric", "d2", "Metric", "pr2"));
        unitSystemNames.add(new UnitSystemInfo("Canonical", "d3","Canonical", "pr3"));
        UnitSystemInfoResponse unitSystemCollection = new UnitSystemInfoResponse(unitSystemNames, 3, 0);
        when(catalogMock.getUnitSystemInfoList(anyInt(), anyInt())).thenReturn(unitSystemCollection);
        UnitSystemInfoResponse result = unitApi.getUnitSystemInfoList(0, -1);
        assertNotNull(result);
        assertNotNull(result.getUnitSystemInfoList());
        assertEquals(3, result.getUnitSystemInfoList().size());
        assertEquals(3, result.getCount());
        assertEquals(0, result.getOffset());
        assertEquals(3, result.getTotalCount());
        assertTrue(result.getUnitSystemInfoList().containsAll(unitSystemNames));
        // test the response container
        UnitSystemInfoResponse r = new UnitSystemInfoResponse();
        assertNotNull(r);
        assertNotNull(r.getUnitSystemInfoList());
        assertEquals(0, r.getCount());
        assertEquals(0, r.getOffset());
        assertEquals(0, r.getTotalCount());
    }

    @Test
    public void getUnitSystem() throws Exception {
        assertNotNull(unitApi);
        UnitSystemImpl unitSystem = new UnitSystemImpl();
        String name = "Metric";
        when(catalogMock.getUnitSystem(name, 0, -1)).thenReturn(unitSystem);
        UnitSystem result = unitApi.getUnitSystem(name, 0, -1);
        assertNotNull(result);
        assertEquals(unitSystem, result);
    }

    @Test
    public void postUnitSystem() throws Exception {
        assertNotNull(unitApi);
        UnitSystemImpl unitSystem = new UnitSystemImpl();
        UnitSystemEssenceImpl essence = new UnitSystemEssenceImpl("Metric");
        when(catalogMock.postUnitSystem(essence, 0, -1)).thenReturn(unitSystem);
        UnitSystemRequest request = new UnitSystemRequest(essence);
        UnitSystem result = unitApi.postUnitSystem(request, 0, -1);
        assertNotNull(result);
        assertEquals(unitSystem, result);
    }

    @Test
    public void getUnitSystemWithInvalidName() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.getUnitSystem(anyString(), anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.getUnitSystem("abc", 0, -1);
            });
    }

    @Test
    public void postUnitSystemWithInvalidEssence() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.postUnitSystem(any(), anyInt(), anyInt())).thenThrow(AppException.class);
            UnitSystemEssenceImpl essence = new UnitSystemEssenceImpl("abc");
            UnitSystemRequest request = new UnitSystemRequest(essence);
            unitApi.postUnitSystem(request, 0, -1);
            });
    }

    /********************************************
     Test Search related API
     *********************************************/
    @Test
    public void searchUnits() throws Exception {
        assertNotNull(unitApi);
        String unitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl unitEssence = Utility.fromJsonString(unitEssenceJson, UnitEssenceImpl.class);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(unitEssence);
        List<UnitImpl> unitList = new ArrayList<>();
        unitList.add(unit);
        Result<UnitImpl> unitResults = new Result<>(unitList, 0, 1);
        when(catalogMock.searchUnits(anyString(), anyInt(), anyInt())).thenReturn(unitResults);
        // test symbol with special char '/'
        SearchRequest request = new SearchRequest("mW/degC");
        QueryResult result = unitApi.postSearchUnits(request, 0, -1);
        assertNotNull(result);
        assertEquals(0, result.getOffset());
        assertEquals(1, result.getCount());
        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getUnits().size());
    }
    private MeasurementImpl getMeasurementWithNamespace(String namespace) {
        String unitEssenceJson = "{\"ancestry\":\"Length\",\"type\":\"UM\"}";
        MeasurementEssenceImpl measurementEssence = Utility.fromJsonString(unitEssenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl measurement = new MeasurementImpl();
        measurement.setEssence(measurementEssence);
        measurement.setName(measurementEssence.getAncestry());
        measurement.setNamespace(namespace);
        return measurement;
    }

    private UnitImpl getUnitWithNamespace(String namespace) {
        String unitEssenceJson = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitEssenceImpl unitEssence = Utility.fromJsonString(unitEssenceJson, UnitEssenceImpl.class);
        UnitImpl unit = new UnitImpl();
        unit.setEssence(unitEssence);
        unit.setName(unitEssence.getSymbol());
        unit.setNamespace(namespace);
        return unit;
    }

    @Test
    public void geUnitMaps() throws Exception {
        assertNotNull(unitApi);
        List<UnitMapImpl> unitMapList = new ArrayList<>();
        UnitMapImpl impl = new UnitMapImpl("fromNamespace", "toNamespace");
        for (int i=0; i<10; i++){
            UnitImpl m1 = getUnitWithNamespace("fromNamespace");
            UnitImpl m2 = getUnitWithNamespace("toNamespace");
            impl.addUnitMapItem(new UnitMapItemImpl(m1, m2, "state", "-"));
        }
        unitMapList.add(impl);
        when(catalogMock.getUnitMaps()).thenReturn(unitMapList);
        QueryResult result = unitApi.getUnitMaps(0, 5);
        assertNotNull(result.getUnitMapItems());
        assertEquals(5, result.getCount());
        assertEquals(10, result.getTotalCount());
    }

    @Test
    public void geUnitMapsWithInvalidRange() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            List<UnitMapImpl> unitMapList = new ArrayList<>();
            UnitMapImpl impl = new UnitMapImpl("fromNamespace", "toNamespace");
            for (int i=0; i<10; i++){
                UnitImpl m1 = getUnitWithNamespace("fromNamespace");
                UnitImpl m2 = getUnitWithNamespace("toNamespace");
                impl.addUnitMapItem(new UnitMapItemImpl(m1, m2, "state", "-"));
            }
            unitMapList.add(impl);
            when(catalogMock.getUnitMaps()).thenReturn(unitMapList);
            QueryResult result = unitApi.getUnitMaps(100, 5);
            });
    }

    @Test
    public void geMeasurementMaps() throws Exception {
        assertNotNull(unitApi);
        List<MeasurementMapImpl> measurementMapsList = new ArrayList<>();
        MeasurementMapImpl impl = new MeasurementMapImpl("fromNamespace", "toNamespace");
        for (int i=0; i<10; i++){
            MeasurementImpl m1 = getMeasurementWithNamespace("fromNamespace");
            MeasurementImpl m2 = getMeasurementWithNamespace("toNamespace");
            impl.addMeasurementMapItem(new MeasurementMapItemImpl(m1, m2, "state", "-"));
        }
        measurementMapsList.add(impl);
        when(catalogMock.getMeasurementMaps()).thenReturn(measurementMapsList);
        QueryResult result = unitApi.getMeasurementMaps(0, 5);
        assertNotNull(result.getMeasurementMapItems());
        assertEquals(5, result.getCount());
        assertEquals(10, result.getTotalCount());
    }

    @Test
    public void geMeasurementMapsWithInvalidRange() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            List<MeasurementMapImpl> measurementMapsList = new ArrayList<>();
            MeasurementMapImpl impl = new MeasurementMapImpl("fromNamespace", "toNamespace");
            for (int i=0; i<10; i++){
                MeasurementImpl m1 = getMeasurementWithNamespace("fromNamespace");
                MeasurementImpl m2 = getMeasurementWithNamespace("toNamespace");
                impl.addMeasurementMapItem(new MeasurementMapItemImpl(m1, m2, "state", "-"));
            }
            measurementMapsList.add(impl);
            when(catalogMock.getMeasurementMaps()).thenReturn(measurementMapsList);
            QueryResult result = unitApi.getMeasurementMaps(100, 5);
            });
    }

    @Test
    public void getMapStates() throws Exception {
        assertNotNull(unitApi);
        List<MapStateImpl> mapStates = new ArrayList<>();
        for (int i=0; i<5; i++) mapStates.add(new MapStateImpl("state", "description", "source"));
        when(catalogMock.getWellknownMapStates()).thenReturn(mapStates);
        QueryResult result = unitApi.getMapStates(0, 2);
        assertNotNull(result.getMapStates());
        assertEquals(2, result.getCount());
        assertEquals(5, result.getTotalCount());
    }

    @Test
    public void getMapStatesInvalidOffset() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            List<MapStateImpl> mapStates = new ArrayList<>();
            for (int i=0; i<5; i++) mapStates.add(new MapStateImpl("state", "description", "source"));
            when(catalogMock.getWellknownMapStates()).thenReturn(mapStates);
            QueryResult result = unitApi.getMapStates(100, 2);
            });
    }

    @Test
    public void searchUnitsWithBadRequest() throws Exception {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.searchUnits(anyString(), anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.postSearchUnits(new SearchRequest("Invalid"), 0, 200);
            });
    }

    @Test
    public void searchUnitsWithInvalidOffset() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.searchUnits(anyString(), anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.postSearchUnits(new SearchRequest(), -1, -1);
            });
    }

    @Test
    public void searchUnitsWithInvalidLimit() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.searchUnits(anyString(), anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.postSearchUnits(new SearchRequest("Namespace:rp66"), 0, -2);
            });
    }

    @Test
    public void searchMeasurements() throws Exception {
        assertNotNull(unitApi);
        String essenceJson = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
        MeasurementEssenceImpl measurementEssence = Utility.fromJsonString(essenceJson, MeasurementEssenceImpl.class);
        MeasurementImpl measurement = new MeasurementImpl();
        measurement.setEssence(measurementEssence);
        List<MeasurementImpl> measurementList = new ArrayList<>();
        measurementList.add(measurement);
        Result<MeasurementImpl> measurementResults = new Result<>(measurementList, 0 , 1);
        when(catalogMock.searchMeasurements(anyString(), anyInt(), anyInt())).thenReturn(measurementResults);
        QueryResult result = unitApi.postSearchMeasurements(new SearchRequest("length"), 0, -1);
        assertNotNull(result);
        assertEquals(0, result.getOffset());
        assertEquals(1, result.getCount());
        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getMeasurements().size());
    }

    @Test
    public void searchMeasurementsWithInvalidOffset() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.searchMeasurements(anyString(), anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.postSearchMeasurements(new SearchRequest("length"), -1, -1);
            });
    }

    @Test
    public void searchMeasurementsWithInvalidLimit() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.searchMeasurements(anyString(), anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.postSearchMeasurements(new SearchRequest("length"), 0, -2);
            });
    }

    @Test
    public void searchWithInvalidOffset() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.search(anyString(), anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.postSearch(new SearchRequest("State:identical"), -1, -1);
            });
    }

    @Test
    public void searchWithInvalidLimit() throws Exception  {
        assertThrows(AppException.class, () -> {

            assertNotNull(unitApi);
            when(catalogMock.searchMeasurements(anyString(), anyInt(), anyInt())).thenThrow(AppException.class);
            unitApi.postSearch(new SearchRequest("State:identical"), 0, -2);
            });
    }

}
