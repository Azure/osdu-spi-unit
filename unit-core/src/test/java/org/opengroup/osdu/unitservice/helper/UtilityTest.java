package org.opengroup.osdu.unitservice.helper;

import org.opengroup.osdu.unitservice.model.ABCDImpl;
import org.opengroup.osdu.unitservice.model.MeasurementImpl;
import org.opengroup.osdu.unitservice.model.ScaleOffsetImpl;
import org.opengroup.osdu.unitservice.model.UnitImpl;
import org.opengroup.osdu.unitservice.model.extended.QueryResultImpl;
import org.opengroup.osdu.unitservice.model.extended.Result;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by ZMai on 6/17/2016.
 */
public class UtilityTest {

    @Test
    public void isEqual() {
        assertTrue(Utility.isEqual(1.0, 1.0));
        assertFalse(Utility.isEqual(1.0, 1.0001));

        assertFalse(Utility.isEqual(1.0, Double.NaN));
        assertFalse(Utility.isEqual(Double.NaN, 1.0));
        assertFalse(Utility.isEqual(Double.NaN, Double.NaN));
    }

    @Test
    public void isZero() {
        assertTrue(Utility.isZeroValue(0));
        assertTrue(Utility.isZeroValue(-0));

        assertFalse(Utility.isZeroValue(0.0001));
        assertFalse(Utility.isZeroValue(-0.0001));
    }

    @Test
    public void isNullOrEmpty() {
        assertTrue(Utility.isNullOrEmpty(null));
        assertTrue(Utility.isNullOrEmpty(""));
        assertFalse(Utility.isNullOrEmpty("   "));
        assertFalse(Utility.isNullOrEmpty("abc"));
    }

    @Test
    public void isNullOrWhiteSpace() {
        assertTrue(Utility.isNullOrWhiteSpace(null));
        assertTrue(Utility.isNullOrWhiteSpace(""));
        assertTrue(Utility.isNullOrWhiteSpace("   "));
        assertFalse(Utility.isNullOrWhiteSpace("abc"));
    }

    @Test
    public void trimSpace() {
        assertEquals(null, Utility.trim(null));
        assertEquals("", Utility.trim(""));
        assertEquals("   ", Utility.trim("   "));
        assertEquals("abc", Utility.trim(" abc  "));
    }

    @Test
    public void getRange() throws Exception {
        // Test null collection
        List<UnitImpl> units = null;
        List<UnitImpl> result =  Utility.getRange(units, 0, 0);
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test empty collection
        units = new ArrayList<>();
        result=  Utility.getRange(units, 0, 0);
        assertEquals(0, result.size());

        // Test no-empty collection
        int n = 10;
        int offset = 2;
        units = new ArrayList<>();
        for(int i = 0; i < n; i++)
            units.add(new UnitImpl());
        result=  Utility.getRange(units, offset, 0);
        assertEquals(0, result.size());
        result=  Utility.getRange(units, offset, n - 5);
        assertEquals(Math.min(n-5, n - offset), result.size());
        result=  Utility.getRange(units, offset, n);
        assertEquals(n - offset, result.size());
        result=  Utility.getRange(units, offset, -1); // -1 means unlimited
        assertEquals(n - offset, result.size());
    }

    @Test
    public void getRangeWithNegativeOffset() throws Exception{
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {

            List<UnitImpl> units = new ArrayList<>();
            units.add(new UnitImpl());
            List<UnitImpl> result = Utility.getRange(units, -1, 1);
            });
    }

    @Test
    public void getRangeWithTooLargeOffset() throws Exception{
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {

            List<UnitImpl> units = new ArrayList<>();
            units.add(new UnitImpl());
            List<UnitImpl> result = Utility.getRange(units, 10, 1);
            });
    }

    @Test
    public void createQueryResultForUnits() {
        QueryResultImpl queryResult = Utility.createQueryResultForUnits(null);
        assertNotNull(queryResult);
        assertEquals(0, queryResult.getOffset());
        assertEquals(0, queryResult.getCount());
        assertEquals(0, queryResult.getTotalCount());

        int n = 5;
        List<UnitImpl> units = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            units.add(new UnitImpl());
        }
        Result<UnitImpl> result = new Result<>(units);
        queryResult = Utility.createQueryResultForUnits(result);
        assertNotNull(queryResult);
        assertEquals(0, queryResult.getOffset());
        assertEquals(n, queryResult.getCount());
        assertEquals(n, queryResult.getTotalCount());
        assertEquals(n, queryResult.getUnits().size());
        assertEquals(0, queryResult.getMeasurements().size());
        assertEquals(0, queryResult.getUnitMapItems().size());
        assertEquals(0, queryResult.getMeasurementMapItems().size());
    }

    @Test
    public void createQueryResultForMeasurements() {
        QueryResultImpl queryResult = Utility.createQueryResultForMeasurements(null);
        assertNotNull(queryResult);
        assertEquals(0, queryResult.getOffset());
        assertEquals(0, queryResult.getCount());
        assertEquals(0, queryResult.getTotalCount());

        int n = 5;
        List<MeasurementImpl> measurements = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            measurements.add(new MeasurementImpl());
        }
        Result<MeasurementImpl> result = new Result<>(measurements);
        queryResult = Utility.createQueryResultForMeasurements(result);
        assertNotNull(queryResult);
        assertEquals(0, queryResult.getOffset());
        assertEquals(n, queryResult.getCount());
        assertEquals(n, queryResult.getTotalCount());
        assertEquals(n, queryResult.getMeasurements().size());
        assertEquals(0, queryResult.getUnits().size());
        assertEquals(0, queryResult.getUnitMapItems().size());
        assertEquals(0, queryResult.getMeasurementMapItems().size());
    }

    @Test
    public void serialization() throws Exception {
        ABCDImpl abcd = new ABCDImpl(0, 3.3, 1, 0);
        String abcdString = Utility.toJsonString(abcd);
        ABCDImpl abcdClone = Utility.fromJsonString(abcdString, ABCDImpl.class);
        assertNotNull(abcdClone);
        assertEquals(abcd.getA(), abcdClone.getA());
        assertEquals(abcd.getB(), abcdClone.getB());
        assertEquals(abcd.getC(), abcdClone.getC());
        assertEquals(abcd.getD(), abcdClone.getD());

        ScaleOffsetImpl scaleOffset = new ScaleOffsetImpl(3.3, 100);
        String scaleOffsetString = Utility.toJsonString(scaleOffset);
        ScaleOffsetImpl scaleOffsetCone = Utility.fromJsonString(scaleOffsetString, ScaleOffsetImpl.class);
        assertNotNull(scaleOffsetCone);
        assertEquals(scaleOffset.getOffset(), scaleOffsetCone.getOffset());
        assertEquals(scaleOffset.getScale(), scaleOffsetCone.getScale());
    }

}
