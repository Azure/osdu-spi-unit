package org.opengroup.osdu.unitservice.configuration;

import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.unitservice.model.ABCDImpl;
import org.opengroup.osdu.unitservice.model.UnitEssenceImpl;
import org.opengroup.osdu.unitservice.model.extended.UnitSystemEssenceImpl;
import org.opengroup.osdu.unitservice.request.ConversionABCDRequest;
import org.opengroup.osdu.unitservice.request.MeasurementRequest;
import org.opengroup.osdu.unitservice.request.SearchRequest;
import org.opengroup.osdu.unitservice.request.UnitRequest;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JacksonConfigurationTest {

    private JsonMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = JacksonConfiguration.strictCopyOf(new JsonMapper());
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
        assertThrows(Exception.class, () -> new JsonMapper().readValue("\"AAA\"", SearchRequest.class));
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
        JsonMapper plain = new JsonMapper();

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
    public void mvcConverterUsesAStrictCopyOfTheSharedMapper() throws Exception {
        JsonMapper shared = new JsonMapper();
        SpringWebMvcConfiguration.StrictJsonConverterCustomizer customizer =
                new SpringWebMvcConfiguration().strictJsonConverterCustomizer(shared);

        JsonMapper strict = customizer.getJsonMapper();
        assertNotSame(shared, strict);
        String abcdNull = "{\"abcd\": null, \"scaleOffset\": {\"scale\": 1.0, \"offset\": 0.0}, \"symbol\": \"m\", \"type\": \"USO\"}";
        assertThrows(Exception.class, () -> shared.readValue(abcdNull, UnitEssenceImpl.class));
        assertThrows(Exception.class, () -> strict.readValue(abcdNull, UnitEssenceImpl.class));
        assertThrows(Exception.class, () -> strict.readValue("{\"query\": false}", SearchRequest.class));
        assertThrows(Exception.class, () -> strict.readValue("{\"a\": 1.5, \"b\": \"2\", \"c\": 0, \"d\": 4}", ABCDImpl.class));
        // Unknown properties stay ignored, as with the Jackson 2 mapper.
        assertEquals("m", strict.readValue("{\"query\": \"m\", \"unknown\": 1}", SearchRequest.class).getQuery());

        HttpMessageConverters.ServerBuilder builder = HttpMessageConverters.forServer().registerDefaults();
        customizer.customize(builder);
        List<JacksonJsonHttpMessageConverter> jsonConverters = new ArrayList<>();
        builder.build().forEach(converter -> {
            if (converter instanceof JacksonJsonHttpMessageConverter jackson) {
                jsonConverters.add(jackson);
            }
        });
        assertEquals(1, jsonConverters.size());
        assertSame(strict, jsonConverters.get(0).getMapper());
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
