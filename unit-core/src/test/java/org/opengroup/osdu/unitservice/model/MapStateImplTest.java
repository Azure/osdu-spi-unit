package org.opengroup.osdu.unitservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapStateImplTest {

    @Test
    public void emptyConstructor() {
        MapStateImpl mapState = new MapStateImpl();
        assertNull(mapState.getState());
        assertNull(mapState.getSource());
        assertNull(mapState.getDescription());
    }

    @Test
    public void constructor() {
        String state = "identical";
        String source = "SLB";
        String description = "haha";

        MapStateImpl mapState = new MapStateImpl(state, description, source);
        assertEquals(state, mapState.getState());
        assertEquals(source, mapState.getSource());
        assertEquals(description, mapState.getDescription());
    }
}
