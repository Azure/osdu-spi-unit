package org.opengroup.osdu.unitservice.model;

import org.opengroup.osdu.unitservice.interfaces.CatalogLastModified;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class catalogLastModifiedTest {
    @Test
    public void getLastModifiedTest(){
        CatalogLastModified lastModified = new CatalogLastModifiedImpl();
        assertNotNull(lastModified);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ssssssXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeStamp = dateFormat.format(Calendar.getInstance().getTime());
        lastModified = new CatalogLastModifiedImpl(timeStamp);
        assertNotNull(lastModified);
        assertEquals(timeStamp, lastModified.getLastModified());
    }
}
