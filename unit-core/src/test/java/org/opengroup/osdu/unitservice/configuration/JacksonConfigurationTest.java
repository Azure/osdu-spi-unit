package org.opengroup.osdu.unitservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.unitservice.model.ABCDImpl;
import org.opengroup.osdu.unitservice.model.UnitEssenceImpl;
import org.opengroup.osdu.unitservice.model.extended.UnitSystemEssenceImpl;
import org.opengroup.osdu.unitservice.request.ConversionABCDRequest;
import org.opengroup.osdu.unitservice.request.MeasurementRequest;
import org.opengroup.osdu.unitservice.request.SearchRequest;
import org.opengroup.osdu.unitservice.request.UnitRequest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JacksonConfigurationTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        JacksonConfiguration.applyStrictCoercion(objectMapper);
    }

    @Test
    public void should_rejectBooleanAndNullQuery_andAcceptStringAndOmittedQuery() throws Exception {
        assertThrows(Exception.class, () -> objectMapper.readValue("{\"query\": false}", SearchRequest.class));
        assertThrows(Exception.class, () -> objectMapper.readValue("{\"query\": null}", SearchRequest.class));

        SearchRequest query = objectMapper.readValue("{\"query\": \"m\"}", SearchRequest.class);
        assertEquals("m", query.getQuery());

        SearchRequest omitted = objectMapper.readValue("{}", SearchRequest.class);
        assertNull(omitted.getQuery());

        assertThrows(Exception.class, () -> objectMapper.readValue("\"AAA\"", SearchRequest.class));
        assertThrows(Exception.class, () -> new ObjectMapper().readValue("\"AAA\"", SearchRequest.class));
    }

    @Test
    public void should_rejectEmptyStringAndNullForUnitNumbers_andAcceptNumbers() throws Exception {
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"a\": \"\", \"b\": \"\", \"c\": \"\", \"d\": \"\"}", ABCDImpl.class));
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"type\": null, \"symbol\": \"\"}", UnitEssenceImpl.class));

        UnitEssenceImpl scaleOffsetOnly = objectMapper.readValue(
                "{\"scaleOffset\": {\"scale\": 1.0, \"offset\": 0.0}, \"symbol\": \"m\", \"type\": \"USO\"}",
                UnitEssenceImpl.class);
        assertNull(scaleOffsetOnly.getABCD());
        assertEquals(1.0d, scaleOffsetOnly.getScaleOffset().getScale());
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"abcd\": null, \"scaleOffset\": {\"scale\": 1.0, \"offset\": 0.0}, \"symbol\": \"m\", \"type\": \"USO\"}",
                UnitEssenceImpl.class));

        ABCDImpl abcd = objectMapper.readValue("{\"a\": 1.5, \"b\": 2, \"c\": 0, \"d\": 4}", ABCDImpl.class);
        assertEquals(1.5d, abcd.getA());
        assertEquals(2d, abcd.getB());
    }

    @Test
    public void should_rejectNullPersistableReference() {
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"essence\": {\"ancestry\": \"\", \"type\": \"\"}, \"persistableReference\": null}",
                MeasurementRequest.class));
    }

    @Test
    public void should_rejectSchemaViolations_onPlainObjectMapperUsedByMvc() throws Exception {
        ObjectMapper plain = new ObjectMapper();

        assertThrows(Exception.class, () -> plain.readValue("{\"query\": false}", SearchRequest.class));
        assertThrows(Exception.class, () -> plain.readValue("{\"query\": null}", SearchRequest.class));
        assertThrows(Exception.class, () -> plain.readValue(
                "{\"essence\": {\"ancestry\": \"\", \"type\": \"\"}, \"persistableReference\": null}",
                MeasurementRequest.class));
        assertThrows(Exception.class, () -> plain.readValue(
                "{\"persistableReference\": \"\", \"essence\": {\"abcd\": {\"a\": 0, \"b\": 0, \"c\": 0, \"d\": 0},"
                        + " \"scaleOffset\": {\"scale\": 0, \"offset\": 0}, \"symbol\": \"\","
                        + " \"baseMeasurement\": {\"type\": \"\", \"ancestry\": \"\"}, \"type\": null}}",
                UnitRequest.class));
        assertThrows(Exception.class, () -> plain.readValue(
                "{\"a\": \"\", \"b\": 0, \"c\": 0, \"d\": 0}", ABCDImpl.class));

        SearchRequest query = plain.readValue("{\"query\": \"m\"}", SearchRequest.class);
        assertEquals("m", query.getQuery());
    }

    @Test
    public void mvcConverterUsesACopyOfTheSharedMapper() throws Exception {
        ObjectMapper shared = new ObjectMapper();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(shared);

        new SpringWebMvcConfiguration().extendMessageConverters(List.of(converter));

        assertNotSame(shared, converter.getObjectMapper());
        String abcdNull = "{\"abcd\": null, \"scaleOffset\": {\"scale\": 1.0, \"offset\": 0.0}, \"symbol\": \"m\", \"type\": \"USO\"}";
        assertThrows(Exception.class, () -> shared.readValue(abcdNull, UnitEssenceImpl.class));
        assertThrows(Exception.class, () -> converter.getObjectMapper().readValue(abcdNull, UnitEssenceImpl.class));
        assertThrows(Exception.class, () -> converter.getObjectMapper().readValue("{\"query\": false}", SearchRequest.class));
    }

    @Test
    public void should_rejectNullBaseMeasurementCodeAndBaseMeasurement_andAllowNullAbcd() throws Exception {
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"essence\": {\"baseMeasurementCode\": null, \"ancestry\": \"\", \"type\": \"\"}, \"persistableReference\": \"\"}",
                MeasurementRequest.class));
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"persistableReference\": \"\", \"essence\": {\"abcd\": {\"a\": 0, \"b\": 0, \"c\": 0, \"d\": 0},"
                        + " \"scaleOffset\": {\"scale\": 0, \"offset\": 0}, \"symbol\": \"\","
                        + " \"baseMeasurement\": null, \"type\": \"\"}}",
                UnitRequest.class));
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"persistableReference\": \"\", \"essence\": {\"abcd\": {\"a\": 0, \"b\": 0, \"c\": 0, \"d\": 0},"
                        + " \"scaleOffset\": null, \"symbol\": \"\","
                        + " \"baseMeasurement\": {\"type\": \"\", \"ancestry\": \"\"}, \"type\": \"\"}}",
                UnitRequest.class));
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"persistableReference\": \"\", \"essence\": {\"abcd\": null,"
                        + " \"scaleOffset\": {\"scale\": 0, \"offset\": 0}, \"symbol\": \"\","
                        + " \"baseMeasurement\": {\"type\": \"\", \"ancestry\": \"\"}, \"type\": \"\"}}",
                UnitRequest.class));
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"persistableReference\": \"\", \"essence\": null}", UnitRequest.class));
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"fromUnit\": null, \"toUnitPersistableReference\": \"\"}", ConversionABCDRequest.class));
        assertThrows(Exception.class, () -> objectMapper.readValue(
                "{\"ancestry\": null}", UnitSystemEssenceImpl.class));

        UnitRequest omittedAbcd = objectMapper.readValue(
                "{\"persistableReference\": \"\", \"essence\": {\"scaleOffset\": {\"scale\": 0, \"offset\": 0},"
                        + " \"symbol\": \"\", \"baseMeasurement\": {\"type\": \"\", \"ancestry\": \"\"}, \"type\": \"\"}}",
                UnitRequest.class);
        assertNull(omittedAbcd.getUnitEssence().getABCD());
    }
}
