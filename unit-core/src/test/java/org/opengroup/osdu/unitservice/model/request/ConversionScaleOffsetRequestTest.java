package org.opengroup.osdu.unitservice.model.request;
import org.opengroup.osdu.unitservice.model.MeasurementEssenceImpl;
import org.opengroup.osdu.unitservice.model.ScaleOffsetImpl;
import org.opengroup.osdu.unitservice.model.UnitEssenceImpl;
import org.opengroup.osdu.unitservice.request.ConversionScaleOffsetRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConversionScaleOffsetRequestTest {
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
        ConversionScaleOffsetRequest request = new ConversionScaleOffsetRequest();
        assertNotNull(request);
        assertNull(request.getFromUnitEssence());
        assertNull(request.getToUnitEssence());
        assertNull(request.getFromUnitPersistableReference());
        assertNull(request.getToUnitPersistableReference());
    }

    @Test
    public void testEssenceConstructor() {
        ConversionScaleOffsetRequest request = new ConversionScaleOffsetRequest(getUnit(), getUnit(), null, null);
        assertNotNull(request);
        assertNotNull(request.getFromUnitEssence());
        assertNotNull(request.getToUnitEssence());
        assertNull(request.getFromUnitPersistableReference());
        assertNull(request.getToUnitPersistableReference());
    }

    @Test
    public void testPersistableReferenceConstructor() {
        String pr = getUnit().toJsonString();
        ConversionScaleOffsetRequest request = new ConversionScaleOffsetRequest(null, null, pr, pr);
        assertNotNull(request);
        assertNotNull(request.getFromUnitEssence());
        assertNotNull(request.getToUnitEssence());
        assertNotNull(request.getFromUnitPersistableReference());
        assertNotNull(request.getToUnitPersistableReference());
    }

}
