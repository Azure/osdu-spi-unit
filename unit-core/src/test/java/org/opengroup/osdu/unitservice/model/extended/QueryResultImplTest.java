package org.opengroup.osdu.unitservice.model.extended;

import org.opengroup.osdu.unitservice.model.MeasurementImpl;
import org.opengroup.osdu.unitservice.model.MeasurementMapItemImpl;
import org.opengroup.osdu.unitservice.model.UnitImpl;
import org.opengroup.osdu.unitservice.model.UnitMapItemImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QueryResultImplTest {

    @Test
    public void emptyConstructorTest() {
        QueryResultImpl queryResult = new QueryResultImpl();
        assertNotNull(queryResult.getUnits());
        assertNotNull(queryResult.getUnitMapItems());
        assertNotNull(queryResult.getMeasurements());
        assertNotNull(queryResult.getMeasurementMapItems());

        assertEquals(0, queryResult.getUnits().size());
        assertEquals(0, queryResult.getUnitMapItems().size());
        assertEquals(0, queryResult.getMeasurements().size());
        assertEquals(0, queryResult.getMeasurementMapItems().size());

        assertEquals(0, queryResult.getOffset());
        assertEquals(0, queryResult.getCount());
        assertEquals(0, queryResult.getTotalCount());
    }

    @Test
    public void buildQueryResultTest() {
        QueryResultImpl queryResult = new QueryResultImpl();

        int count = 0;
        int n = 5;
        for(int i = 0; i < n; i++) {
            queryResult.addUnit(new UnitImpl());
            count++;
        }
        assertEquals(n, queryResult.getUnits().size());

        n = 10;
        for(int i = 0; i < n; i++) {
            queryResult.addMeasurement(new MeasurementImpl());
            count++;
        }
        for(int i = 0; i < n; i++) {
            MeasurementImpl childMeasurement = new MeasurementImpl();
            childMeasurement.setIsBaseMeasurement(false);
            queryResult.addMeasurement(childMeasurement);
            count++;
        }
        assertEquals(n * 2, queryResult.getMeasurements().size());

        UnitMapItemImpl unitMapItem = new UnitMapItemImpl();
        queryResult.addUnitMapItem(unitMapItem);
        assertEquals(1, queryResult.getUnitMapItems().size());
        count++;

        MeasurementMapItemImpl measurementMapItem = new MeasurementMapItemImpl();
        queryResult.addMeasurementMapItem(measurementMapItem);
        assertEquals(1, queryResult.getMeasurementMapItems().size());
        count++;

        int offset = 11;
        int totalCount = count + offset;
        queryResult.setOffset(offset);
        queryResult.setTotalCount(totalCount);
        assertEquals(offset, queryResult.getOffset());
        assertEquals(count, queryResult.getCount());
        assertEquals(totalCount, queryResult.getTotalCount());

        totalCount = count + offset + 100;
        queryResult.setTotalCount(totalCount);
        assertEquals(totalCount, queryResult.getTotalCount());
    }

    @Test
    public void setNegativeOffsetTest() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {

            QueryResultImpl queryResult = new QueryResultImpl();
            queryResult.setOffset(-1);
            });
    }

    @Test
    public void setNegativeTotalCount() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {

            QueryResultImpl queryResult = new QueryResultImpl();
            queryResult.setTotalCount(-1);
            });
    }

    @Test
    public void hasInconsistentCount() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {

            QueryResultImpl queryResult = new QueryResultImpl();

            int count = 0;
            for(int i = 0; i < 5; i++) {
                queryResult.addUnit(new UnitImpl());
                count++;
            }
            // Exception will be thrown when calling getCount() before setting totalCount
            assertEquals(count, queryResult.getCount());
            });
    }
}
