package org.opengroup.osdu.unitservice.configuration;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;

/**
 * Strict JSON coercion applied to a copy of the MVC message converter.
 *
 * <p>os-core-common publishes its own {@link ObjectMapper}, so a builder customizer
 * never configures that converter. {@code SpringWebMvcConfiguration} copies the
 * shared mapper and applies these rules only to the copy. String and number fields
 * reject the wrong token, including JSON null, through their own deserializers.
 * Every other property rejects an explicit JSON null as well, because the OpenAPI
 * schemas do not mark those properties nullable. Omitting a property is still valid.
 */
public final class JacksonConfiguration {

    private JacksonConfiguration() {
    }

    public static void applyStrictCoercion(ObjectMapper objectMapper) {
        objectMapper.setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.FAIL));
        objectMapper.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        objectMapper.coercionConfigFor(String.class)
                .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.EmptyArray, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.EmptyObject, CoercionAction.Fail);
        failNonIntegral(objectMapper, Integer.class);
        failNonIntegral(objectMapper, Integer.TYPE);
        failNonNumeric(objectMapper, Double.class);
        failNonNumeric(objectMapper, Double.TYPE);
        failNonNumeric(objectMapper, Float.class);
        failNonNumeric(objectMapper, Float.TYPE);
    }

    private static void failNonIntegral(ObjectMapper objectMapper, Class<?> type) {
        objectMapper.coercionConfigFor(type)
                .setCoercion(CoercionInputShape.String, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);
    }

    private static void failNonNumeric(ObjectMapper objectMapper, Class<?> type) {
        objectMapper.coercionConfigFor(type)
                .setCoercion(CoercionInputShape.String, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);
    }
}
