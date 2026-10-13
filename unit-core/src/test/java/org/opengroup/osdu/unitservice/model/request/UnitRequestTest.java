package org.opengroup.osdu.unitservice.model.request;

import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.model.UnitEssenceImpl;
import org.opengroup.osdu.unitservice.request.UnitRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitRequestTest {

    @Test
    public void emptyTest(){
        UnitRequest request = new UnitRequest();
        assertNotNull(request);
        assertNull(request.getUnitEssence());
        assertNull(request.getPersistableReference());
    }

    // @Test
    // public void v1RequestTest() {
    //     String v1_pr = "%7B%22ScaleOffset%22%3A%7B%22Scale%22%3A0.3048006096012192%2C%22Offset%22%3A0.0%7D%2C%22Symbol%22%3A%22ftUS%22%2C%22BaseMeasurement%22%3A%22%257B%2522Ancestry%2522%253A%2522Length%2522%257D%22%7D";
    //     UnitRequest request = new UnitRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getUnitEssence());
    //     assertNotNull(request.getPersistableReference());
    //     v1_pr = v1_pr.replace("%7D", "Corrupted");
    //     request = new UnitRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getPersistableReference());
    //     assertNull(request.getUnitEssence());
    //     v1_pr = v1_pr.replace("Corrupted", "%7D");
    //     v1_pr = v1_pr.replace("0.0", "NaN");
    //     request = new UnitRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getPersistableReference());
    //     assertNull(request.getUnitEssence());
    //     v1_pr = v1_pr.replace("NaN", "0.0");
    //     v1_pr = v1_pr.replace("Ancestry", "test");
    //     request = new UnitRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getPersistableReference());
    //     assertNull(request.getUnitEssence());
    //     v1_pr =  "%7B%22ABCD%22%3A%7B%22A%22%3A2298.35%2C%22B%22%3A5.0%2C%22C%22%3A9.0%2C%22D%22%3A0.0%7D%2C%22Symbol%22%3A%22degF%22%2C%22BaseMeasurement%22%3A%22%257B%2522Ancestry%2522%253A%2522K%2522%257D%22%7D";
    //     request = new UnitRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getUnitEssence());
    //     assertNotNull(request.getPersistableReference());
    //     v1_pr = v1_pr.replace("%7D", "Corrupted");
    //     request = new UnitRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getPersistableReference());
    //     assertNull(request.getUnitEssence());
    //     v1_pr = v1_pr.replace("Corrupted", "%7D");
    //     v1_pr = v1_pr.replace("0.0", "NaN");
    //     request = new UnitRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getPersistableReference());
    //     assertNull(request.getUnitEssence());
    //     v1_pr = v1_pr.replace("NaN", "0.0");
    //     v1_pr = v1_pr.replace("Ancestry", "test");
    //     request = new UnitRequest(null, v1_pr);
    //     assertNotNull(request);
    //     assertNotNull(request.getPersistableReference());
    //     assertNull(request.getUnitEssence());
    // }

    @Test
    public void v2RequestTest() {
        String v2_pr = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        UnitRequest request = new UnitRequest(null, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getUnitEssence());
        assertNotNull(request.getPersistableReference());
        v2_pr = v2_pr.replace("symbol", "Corrupted");
        request = new UnitRequest(null, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getPersistableReference());
        assertNull(request.getUnitEssence());
        v2_pr = v2_pr.replace("Corrupted", "symbol");
        v2_pr = v2_pr.replace("0.0", "NaN");
        request = new UnitRequest(null, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getPersistableReference());
        assertNull(request.getUnitEssence());
        v2_pr = v2_pr.replace("NaN", "0.0");
        v2_pr = v2_pr.replace("ancestry", "Corrupted");
        request = new UnitRequest(null, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getPersistableReference());
        assertNull(request.getUnitEssence());
        v2_pr = "{\"abcd\":{\"a\":2298.35,\"b\":5.0,\"c\":9.0,\"d\":0.0},\"symbol\":\"degF\",\"baseMeasurement\":{\"ancestry\":\"K\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        request = new UnitRequest(null, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getUnitEssence());
        assertNotNull(request.getPersistableReference());
        v2_pr = v2_pr.replace("0.0", "NaN");
        request = new UnitRequest(null, v2_pr);
        assertNotNull(request);
        assertNull(request.getUnitEssence());
        assertNotNull(request.getPersistableReference());
        v2_pr = v2_pr.replace("NaN", "\"NaN\"");
        request = new UnitRequest(null, v2_pr);
        assertNotNull(request);
        assertNull(request.getUnitEssence());
        assertNotNull(request.getPersistableReference());
    }

    @Test
    public void essenceRequestTest(){
        String v2_pr = "{\"abcd\":{\"a\":2298.35,\"b\":5.0,\"c\":9.0,\"d\":0.0},\"symbol\":\"degF\",\"baseMeasurement\":{\"ancestry\":\"K\",\"type\":\"UM\"},\"type\":\"UAD\"}";
        UnitEssenceImpl ess = Utility.fromJsonString(v2_pr, UnitEssenceImpl.class);
        assertNotNull(ess);
        UnitRequest request = new UnitRequest(ess, null);
        assertNotNull(request);
        assertNotNull(request.getUnitEssence());
        assertNull(request.getPersistableReference());
        assertEquals("UAD", request.getUnitEssence().getType());
        String v2_pr2 = "{\"scaleOffset\":{\"scale\":0.3048,\"offset\":0.0},\"symbol\":\"ft\",\"baseMeasurement\":{\"ancestry\":\"Length\",\"type\":\"UM\"},\"type\":\"USO\"}";
        ess = Utility.fromJsonString(v2_pr2, UnitEssenceImpl.class);
        assertNotNull(ess);
        request = new UnitRequest(ess, null);
        assertNotNull(request);
        assertNotNull(request.getUnitEssence());
        assertNull(request.getPersistableReference());
        assertEquals("USO", request.getUnitEssence().getType());
        request = new UnitRequest(ess, v2_pr);
        assertNotNull(request);
        assertNotNull(request.getUnitEssence());
        assertNotNull(request.getPersistableReference());
        assertEquals("USO", request.getUnitEssence().getType()); // essence wins over persistableReference
    }
}
