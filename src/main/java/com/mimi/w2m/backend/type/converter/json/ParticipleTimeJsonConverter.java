package com.mimi.w2m.backend.type.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.response.exception.InvalidValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Formatter;

@JsonComponent
public class ParticipleTimeJsonConverter {
    private final static Logger logger = LoggerFactory.getLogger(ParticipleTime.class.getName());

    public static class Serializer extends JsonSerializer<ParticipleTime> {
        @Override
        public void serialize(ParticipleTime value, JsonGenerator gen, SerializerProvider serializers)
                throws InvalidValueException {
            try {
                gen.writeString(value.toString());
            } catch (IOException e) {
                final var formatter = new Formatter();
                final var msg = formatter.format("Serialize Failed")
                        .toString();
                logger.error(msg);
                throw new InvalidValueException(msg);
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<ParticipleTime> {
        @Override
        public ParticipleTime deserialize(JsonParser p, DeserializationContext ctxt) throws InvalidValueException {
            try {
                JsonNode jsonNode = p.getCodec()
                        .readTree(p);
                return ParticipleTime.of(jsonNode.asText());
            } catch (IOException e) {
                final var formatter = new Formatter();
                final var msg = formatter.format("Deserialize Failed")
                        .toString();
                logger.error(msg);
                throw new InvalidValueException(msg);
            }
        }
    }
}