package org.opengroup.osdu.unitservice.configuration;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/**
 * Rejects non-string JSON tokens for {@link String} fields (for example {@code false},
 * {@code 123}, {@code null}). Omitted properties stay unset. Same rule as legal: this does
 * not depend on ObjectMapper coercion being installed on the MVC converter.
 */
public final class StrictStringDeserializer extends ValueDeserializer<String> {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) {
        if (!parser.hasToken(JsonToken.VALUE_STRING)) {
            return (String) context.handleUnexpectedToken(String.class, parser);
        }
        return parser.getString();
    }

    @Override
    public String getNullValue(DeserializationContext context) {
        throw DatabindException.from(context.getParser(), "JSON null is not a string");
    }
}
