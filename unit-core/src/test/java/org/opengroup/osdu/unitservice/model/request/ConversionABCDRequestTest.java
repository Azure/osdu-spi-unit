package org.opengroup.osdu.unitservice.model.request;

import org.opengroup.osdu.unitservice.model.MeasurementEssenceImpl;
import org.opengroup.osdu.unitservice.model.ScaleOffsetImpl;
import org.opengroup.osdu.unitservice.model.UnitEssenceImpl;
import org.opengroup.osdu.unitservice.request.ConversionABCDRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConversionABCDRequestTest {
    private UnitEssenceImpl getUnit(){
        UnitEssenceImpl essence = new UnitEssenceImpl();
        essence.setScaleOffset(new ScaleOffsetImpl(0.0, 0.3048));
        essence.setSymbol("ft");
        essence.setType("USO");
        MeasurementEssenceImpl measurementEssence = new MeasurementEssenceImpl();
        measurementEssence.setAncestry("Length");
        essence.setBaseMeasurementEssence(measurementEssence);
        return essence;
    }
    @Test
    public void emptyConstructor() {
        ConversionABCDRequest request = new ConversionABCDRequest();
        assertNotNull(request);
        assertNull(request.getFromUnitEssence());
        assertNull(request.getToUnitEssence());
        assertNull(request.getFromUnitPersistableReference());
        assertNull(request.getToUnitPersistableReference());
    }

    @Test
    public void testEssenceConstructor() {
        ConversionABCDRequest request = new ConversionABCDRequest(getUnit(), getUnit(), null, null);
        assertNotNull(request);
        assertNotNull(request.getFromUnitEssence());
        assertNotNull(request.getToUnitEssence());
        assertNull(request.getFromUnitPersistableReference());
        assertNull(request.getToUnitPersistableReference());
    }

    @Test
    public void testPersistableReferenceConstructor() {
        String pr = getUnit().toJsonString();
        ConversionABCDRequest request = new ConversionABCDRequest(null, null, pr, pr);
        assertNotNull(request);
        assertNotNull(request.getFromUnitEssence());
        assertNotNull(request.getToUnitEssence());
        assertNotNull(request.getFromUnitPersistableReference());
        assertNotNull(request.getToUnitPersistableReference());
    }
}
