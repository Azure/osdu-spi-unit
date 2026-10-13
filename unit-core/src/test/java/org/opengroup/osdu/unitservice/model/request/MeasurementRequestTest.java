package org.opengroup.osdu.unitservice.model.request;

import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.model.MeasurementEssenceImpl;
import org.opengroup.osdu.unitservice.request.MeasurementRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasurementRequestTest {

    @Test
    public void emptyTest(){
        MeasurementRequest request = new MeasurementRequest();
        assertNotNull(request);
        assertNull(request.getMeasurementEssence());
        assertNull(request.getMeasurementEssenceJson());
    }

    // @Test
    // public void v1RequestTest(){
    //     String v1_pr = "%7B%22Ancestry%22%3A%22Length.Standard_Depth_Index%22%7D";
    //     MeasurementRequest request = new MeasurementRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getMeasurementEssenceJson());
    //     assertNotNull(request.getMeasurementEssence());
    //     v1_pr = v1_pr.replace("%7D", "Corrupted");
    //     request = new MeasurementRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getMeasurementEssenceJson());
    //     assertNull(request.getMeasurementEssence());
    //     v1_pr = v1_pr.replace("Corrupted", "%7D");
    //     v1_pr = v1_pr.replace("Ancestry", "test");
    //     request = new MeasurementRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getMeasurementEssenceJson());
    //     assertNull(request.getMeasurementEssence());
    // }

    @Test
    public void v2RequestTest(){
        String v2_pr = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
        MeasurementRequest request = new MeasurementRequest(null, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getMeasurementEssenceJson());
        assertNotNull(request.getMeasurementEssence());
        v2_pr = v2_pr.replace("UM", "Corrupted");
        request = new MeasurementRequest(null, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getMeasurementEssenceJson());
        assertNull(request.getMeasurementEssence());
        v2_pr = v2_pr.replace("Corrupted", "UM");
        v2_pr = v2_pr.replace("ancestry", "test");
        request = new MeasurementRequest(null, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getMeasurementEssenceJson());
        assertNull(request.getMeasurementEssence());
    }

    @Test
    public void essenceRequestTest(){
        String v2_pr = "{\"ancestry\":\"Length.Millimeter\",\"type\":\"UM\"}";
        MeasurementEssenceImpl essence = Utility.fromJsonString(v2_pr, MeasurementEssenceImpl.class);
        MeasurementRequest request = new MeasurementRequest(essence, null);
        assertNotNull(request);
        assertNull(request.getMeasurementEssenceJson());
        assertNotNull(request.getMeasurementEssence());
        v2_pr = "{\"ancestry\":\"Length.Standard_Depth_Index\",\"type\":\"UM\"}";
        request = new MeasurementRequest(essence, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getMeasurementEssenceJson());
        assertNotNull(request.getMeasurementEssence());
        assertEquals("Length.Millimeter", request.getMeasurementEssence().getAncestry());
    }
}
