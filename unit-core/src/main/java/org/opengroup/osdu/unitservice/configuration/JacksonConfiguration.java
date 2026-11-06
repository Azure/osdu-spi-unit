package org.opengroup.osdu.unitservice.configuration;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.cfg.CoercionInputShape;
import tools.jackson.databind.cfg.MutableCoercionConfig;
import tools.jackson.databind.json.JsonMapper;

/**
 * Strict JSON coercion applied to a copy of the MVC message converter's mapper.
 *
 * <p>os-core-common publishes its own {@link JsonMapper}, so a builder customizer
 * never configures that mapper. {@code SpringWebMvcConfiguration} rebuilds the
 * shared mapper and applies these rules only to the copy. String and number fields
 * reject the wrong token, including JSON null, through their own deserializers.
 * Every other property rejects an explicit JSON null as well, because the OpenAPI
 * schemas do not mark those properties nullable. Omitting a property is still valid.
 */
public final class JacksonConfiguration {

    private JacksonConfiguration() {
    }

    public static JsonMapper strictCopyOf(JsonMapper sharedMapper) {
        return applyStrictCoercion(sharedMapper.rebuild()).build();
    }

    public static JsonMapper.Builder applyStrictCoercion(JsonMapper.Builder builder) {
        return builder
                .changeDefaultNullHandling(ignored -> JsonSetter.Value.forValueNulls(Nulls.FAIL))
                .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
                // Jackson 2 era contract: unknown properties are ignored, not rejected.
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // Jackson 3 reads constructor parameter names and would bind request bodies through the
                // multi-argument constructors, where an omitted property becomes null and fails. Keep the
                // Jackson 2 binding through the default constructor and setters.
                .disable(MapperFeature.DETECT_PARAMETER_NAMES)
                .withCoercionConfig(String.class, config -> config
                        .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail)
                        .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail)
                        .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                        .setCoercion(CoercionInputShape.EmptyArray, CoercionAction.Fail)
                        .setCoercion(CoercionInputShape.EmptyObject, CoercionAction.Fail))
                .withCoercionConfig(Integer.class, JacksonConfiguration::failNonIntegral)
                .withCoercionConfig(Integer.TYPE, JacksonConfiguration::failNonIntegral)
                .withCoercionConfig(Double.class, JacksonConfiguration::failNonNumeric)
                .withCoercionConfig(Double.TYPE, JacksonConfiguration::failNonNumeric)
                .withCoercionConfig(Float.class, JacksonConfiguration::failNonNumeric)
                .withCoercionConfig(Float.TYPE, JacksonConfiguration::failNonNumeric);
    }

    private static void failNonIntegral(MutableCoercionConfig config) {
        config.setCoercion(CoercionInputShape.String, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);
    }

    private static void failNonNumeric(MutableCoercionConfig config) {
        config.setCoercion(CoercionInputShape.String, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);
    }
}
