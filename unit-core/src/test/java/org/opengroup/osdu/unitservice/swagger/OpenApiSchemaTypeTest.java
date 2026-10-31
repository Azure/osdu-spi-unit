package org.opengroup.osdu.unitservice.swagger;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.unitservice.model.ABCDImpl;
import org.opengroup.osdu.unitservice.model.ScaleOffsetImpl;
import org.opengroup.osdu.unitservice.model.extended.UnitSystemInfoResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * OpenAPI 3.1 must describe JSON numbers as integer/number. Setting
 * {@code @Schema(type = "integer")} or {@code type = "number"} makes swagger-core emit string.
 */
public class OpenApiSchemaTypeTest {

    @Test
    public void should_emitIntegerAndNumberTypes_forJsonNumericFields() {
        ModelConverters converters = new ModelConverters();
        converters.addConverter(new ModelResolver(Json31.mapper()));

        Map<String, Schema> unitSystems = converters.read(UnitSystemInfoResponse.class);
        Schema<?> unitSystem = unitSystems.get("UnitSystemInfoResponse");
        assertEquals("integer", property(unitSystem, "offset").getType());
        assertEquals("int32", property(unitSystem, "offset").getFormat());
        assertEquals("integer", property(unitSystem, "totalCount").getType());
        assertEquals("int32", property(unitSystem, "totalCount").getFormat());
        assertEquals("integer", property(unitSystem, "count").getType());

        Map<String, Schema> abcds = converters.read(ABCDImpl.class);
        Schema<?> abcd = abcds.get("ABCDImpl");
        assertEquals("number", property(abcd, "a").getType());
        assertEquals("double", property(abcd, "a").getFormat());

        Map<String, Schema> scales = converters.read(ScaleOffsetImpl.class);
        Schema<?> scale = scales.get("ScaleOffsetImpl");
        assertEquals("number", property(scale, "scale").getType());
        assertEquals("number", property(scale, "offset").getType());
    }

    @SuppressWarnings("rawtypes")
    private static Schema property(Schema schema, String name) {
        return (Schema) schema.getProperties().get(name);
    }
}
