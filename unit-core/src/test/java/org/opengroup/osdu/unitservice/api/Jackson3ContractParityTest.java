package org.opengroup.osdu.unitservice.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.core.common.http.HttpConfiguration;
import org.opengroup.osdu.unitservice.configuration.JacksonConfiguration;
import org.opengroup.osdu.unitservice.model.extended.UnitSystemInfo;
import org.opengroup.osdu.unitservice.logging.AuditLogger;
import org.opengroup.osdu.unitservice.model.CatalogImpl;
import org.opengroup.osdu.unitservice.request.ConversionABCDRequest;
import org.opengroup.osdu.unitservice.request.ConversionScaleOffsetRequest;
import org.opengroup.osdu.unitservice.request.MeasurementRequest;
import org.opengroup.osdu.unitservice.request.SearchRequest;
import org.opengroup.osdu.unitservice.request.UnitRequest;
import org.opengroup.osdu.unitservice.request.UnitSystemRequest;
import org.opengroup.osdu.unitservice.util.AppError;
import tools.jackson.databind.json.JsonMapper;

/**
 * Compares the JSON the service produces and accepts under Jackson 3 with the Jackson 2 contract.
 *
 * <p>The Jackson 2 mapper is configured like the os-core-common 8.x {@code jsonObjectMapper} bean the
 * service used before. The Jackson 3 mapper is the strict MVC copy of the os-core-common 10 bean. Responses
 * come from the deployed unit catalog (data/unit_catalog_v2.json) through both API versions and are compared as trees, so property order is
 * ignored (Jackson 3 sorts properties alphabetically).
 */
class Jackson3ContractParityTest {

    private static ObjectMapper jackson2;
    private static JsonMapper jackson3;
    private static UnitApiV2 v2;
    private static UnitApiV3 v3;

    @BeforeAll
    static void setUp() throws Exception {
        jackson2 = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setTimeZone(TimeZone.getDefault());
        jackson3 = JacksonConfiguration.strictCopyOf(new HttpConfiguration().jsonObjectMapper());

        CatalogImpl catalog;
        try (Reader reader = Files.newBufferedReader(Path.of("../data/unit_catalog_v2.json"), StandardCharsets.UTF_8)) {
            catalog = CatalogImpl.createCatalog(reader);
        }
        v2 = new UnitApiV2(catalog);
        v3 = new UnitApiV3(catalog, mock(AuditLogger.class));
    }

    @Test
    void responsesSerializeTheSameUnderJackson2AndJackson3() throws Exception {
        Map<String, Supplier<Object>> responses = new LinkedHashMap<>();
        responses.put("v3 catalog", v3::getCatalog);
        responses.put("v3 lastModified", v3::getLastModified);
        responses.put("v3 measurements", () -> v3.getMeasurements(0, 10000));
        responses.put("v3 units", () -> v3.getUnits(0, 10000));
        responses.put("v3 measurement", () -> v3.getMeasurement("Length"));
        responses.put("v3 unitsBySymbol", () -> v3.getUnitsBySymbol("m"));
        responses.put("v3 unitBySymbol", () -> v3.getUnitBySymbol("Energistics_UoM", "ft"));
        responses.put("v3 unitsByMeasurement", () -> v3.getUnitsByMeasurement("Length"));
        responses.put("v3 preferredUnits", () -> v3.getPreferredUnitsByMeasurement("Length"));
        responses.put("v3 unitBySystem", () -> v3.getUnitBySystemAndMeasurement("English", "Length"));
        responses.put("v3 scaleOffset", () -> v3.getConversionScaleOffsetBySymbols("Energistics_UoM", "ft", "m"));
        responses.put("v3 abcd", () -> v3.getConversionABCDBySymbols("Energistics_UoM", "degF", "degC"));
        responses.put("v3 unitSystemInfo", () -> v3.getUnitSystemInfoList(0, 100));
        responses.put("v3 unitMaps", () -> v3.getUnitMaps(0, 100));
        responses.put("v3 measurementMaps", () -> v3.getMeasurementMaps(0, 100));
        responses.put("v3 mapStates", () -> v3.getMapStates(0, 100));
        responses.put("v3 search", () -> v3.postSearch(new SearchRequest("Length"), 0, 100));
        responses.put("v3 searchUnits", () -> v3.postSearchUnits(new SearchRequest("ft"), 0, 100));
        responses.put("v2 catalog", v2::getCatalog);
        responses.put("v2 units", () -> v2.getUnits(0, 10000));
        responses.put("v2 measurement", () -> v2.getMeasurement("Length"));
        responses.put("v2 unitSystemInfo", () -> v2.getUnitSystemInfoList(0, 100));
        responses.put("error body", () -> AppError.builder().code(400).reason("Bad Request").message("m").build());
        for (UnitSystemInfo info : v3.getUnitSystemInfoList(0, 100).getUnitSystemInfoList()) {
            responses.put("v3 unitSystem " + info.getName(), () -> v3.getUnitSystem(info.getName(), 0, 10000));
        }

        List<String> differences = new ArrayList<>();
        for (Map.Entry<String, Supplier<Object>> response : responses.entrySet()) {
            Object body = response.getValue().get();
            JsonNode before = jackson2.readTree(jackson2.writeValueAsString(body));
            JsonNode after = jackson2.readTree(jackson3.writeValueAsString(body));
            assertTrue(before.size() > 0, response.getKey() + " serialized to an empty document");
            if (!before.equals(after)) {
                differences.add(response.getKey() + "\n  jackson2: " + before + "\n  jackson3: " + after);
            }
        }
        assertEquals(List.of(), differences);
    }

    @Test
    void validRequestBodiesBindTheSameUnderJackson2AndJackson3() throws Exception {
        String measurementEssence = "{\"ancestry\": \"Length\", \"type\": \"UM\"}";
        String ftEssence = jackson2.writeValueAsString(v3.getUnitBySymbol("Energistics_UoM", "ft").getEssence());
        String mEssence = jackson2.writeValueAsString(v3.getUnitBySymbol("Energistics_UoM", "m").getEssence());
        Map<String, Class<?>> requests = new LinkedHashMap<>();
        requests.put("{\"essence\": " + measurementEssence + "}", MeasurementRequest.class);
        requests.put("{\"persistableReference\": \"%7B%22Ancestry%22%3A%22Length%22%7D\", \"unknown\": 1}",
                MeasurementRequest.class);
        requests.put("{\"essence\": " + ftEssence + "}", UnitRequest.class);
        requests.put("{\"persistableReference\": " + jackson2.writeValueAsString(ftEssence) + "}", UnitRequest.class);
        requests.put("{\"fromUnitPersistableReference\": " + jackson2.writeValueAsString(ftEssence)
                + ", \"toUnit\": " + mEssence + "}", ConversionScaleOffsetRequest.class);
        requests.put("{\"fromUnit\": " + ftEssence + ", \"toUnit\": " + mEssence + "}",
                ConversionScaleOffsetRequest.class);
        requests.put("{\"fromUnit\": " + ftEssence + ", \"toUnit\": " + mEssence + "}", ConversionABCDRequest.class);
        requests.put("{\"essence\": {\"ancestry\": \"English\"}}", UnitSystemRequest.class);
        requests.put("{\"query\": \"Length\"}", SearchRequest.class);

        for (Map.Entry<String, Class<?>> request : requests.entrySet()) {
            Object before = jackson2.readValue(request.getKey(), request.getValue());
            Object after = jackson3.readValue(request.getKey(), request.getValue());
            assertEquals(jackson2.readTree(jackson2.writeValueAsString(before)),
                    jackson2.readTree(jackson2.writeValueAsString(after)), request.getKey());
        }
    }
}
