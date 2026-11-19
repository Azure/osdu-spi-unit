package org.opengroup.osdu.unitservice.configuration;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/**
 * Rejects non-numeric JSON for {@code double} fields (for example {@code ""}, {@code false},
 * {@code null}). Integral JSON numbers remain valid.
 */
public final class StrictDoubleDeserializer extends ValueDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser parser, DeserializationContext context) {
        if (!parser.hasToken(JsonToken.VALUE_NUMBER_INT) && !parser.hasToken(JsonToken.VALUE_NUMBER_FLOAT)) {
            return (Double) context.handleUnexpectedToken(Double.TYPE, parser);
        }
        return parser.getDoubleValue();
    }

    @Override
    public Double getNullValue(DeserializationContext context) {
        throw DatabindException.from(context.getParser(), "JSON null is not a number");
    }
}
