package org.opengroup.osdu.unitservice.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

/**
 * Rejects non-string JSON tokens for {@link String} fields (for example {@code false},
 * {@code 123}, {@code null}). Omitted properties stay unset. Same rule as legal: this does
 * not depend on ObjectMapper coercion being installed on the MVC converter.
 */
public final class StrictStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (!parser.hasToken(JsonToken.VALUE_STRING)) {
            return (String) context.handleUnexpectedToken(String.class, parser);
        }
        return parser.getText();
    }

    @Override
    public String getNullValue(DeserializationContext context) throws JsonMappingException {
        throw JsonMappingException.from(context.getParser(), "JSON null is not a string");
    }
}
