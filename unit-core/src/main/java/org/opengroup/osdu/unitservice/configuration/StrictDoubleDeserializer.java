package org.opengroup.osdu.unitservice.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

/**
 * Rejects non-numeric JSON for {@code double} fields (for example {@code ""}, {@code false},
 * {@code null}). Integral JSON numbers remain valid.
 */
public final class StrictDoubleDeserializer extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (!parser.hasToken(JsonToken.VALUE_NUMBER_INT) && !parser.hasToken(JsonToken.VALUE_NUMBER_FLOAT)) {
            return (Double) context.handleUnexpectedToken(Double.TYPE, parser);
        }
        return parser.getDoubleValue();
    }

    @Override
    public Double getNullValue(DeserializationContext context) throws JsonMappingException {
        throw JsonMappingException.from(context.getParser(), "JSON null is not a number");
    }
}
