package org.opengroup.osdu.unitservice.model.extended;

import org.opengroup.osdu.unitservice.model.UnitImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResultTest {
    List<UnitImpl> units;

    @BeforeEach
    public void setup() {
        int n = 5;
        units = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            units.add(new UnitImpl());
        }
    }

    @Test
    public void emptyConstructorTest() {
        Result<UnitImpl> result = new Result<UnitImpl>();
        assertNotNull(result.getItems());
        assertEquals(0, result.getItems().size());
        assertEquals(0, result.getOffset());
        assertEquals(0, result.getCount());
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void constructorWithItemListOnlyTest() {
        Result<UnitImpl> result = new Result<UnitImpl>(null);
        assertNotNull(result.getItems());
        assertEquals(0, result.getItems().size());
        assertEquals(0, result.getOffset());
        assertEquals(0, result.getCount());
        assertEquals(0, result.getTotalCount());

        int count = units.size();
        result = new Result<UnitImpl>(units);
        assertNotNull(result.getItems());
        assertEquals(count, result.getItems().size());
        assertEquals(0, result.getOffset());
        assertEquals(count, result.getCount());
        assertEquals(count, result.getTotalCount());
    }

    @Test
    public void constructorWithRangeTest() {
        Result<UnitImpl> result = new Result<UnitImpl>(null, 0, 0);
        assertNotNull(result.getItems());
        assertEquals(0, result.getItems().size());
        assertEquals(0, result.getOffset());
        assertEquals(0, result.getCount());
        assertEquals(0, result.getTotalCount());

        int offset = 10;
        int total = 100;
        int count = units.size();
        result = new Result<UnitImpl>(units, offset, total);
        assertNotNull(result.getItems());
        assertEquals(count, result.getItems().size());
        assertEquals(offset, result.getOffset());
        assertEquals(count, result.getCount());
        assertEquals(total, result.getTotalCount());
    }

    @Test
    public void setNegativeOffsetTest() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {

            int offset = -1;
            int total = 100;
            new Result<UnitImpl>(units, offset, total);
            });
    }

    @Test
    public void setNegativeTotalCount() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {

            int offset = 0;
            int total = -1;
            new Result<UnitImpl>(units, offset, total);
            });
    }

    @Test
    public void hasInconsistentCount() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {

            int offset = 10;
            int total = units.size();
            new Result<UnitImpl>(units, offset, total);
            });
    }
}
