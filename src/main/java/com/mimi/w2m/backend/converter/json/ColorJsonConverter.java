package com.mimi.w2m.backend.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.mimi.w2m.backend.config.exception.InvalidValueException;
import com.mimi.w2m.backend.dto.event.ColorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ColorJsonConverter {
    private final static Logger logger = LoggerFactory.getLogger(ColorJsonConverter.class.getName());

    public static class Serializer extends JsonSerializer<ColorDto> {
        @Override
        public void serialize(ColorDto value, JsonGenerator gen, SerializerProvider serializers)
                throws InvalidValueException {
            try {
                gen.writeString(value.toString());
            } catch (IOException e) {
                final var msg = "Serialize Failed";
                logger.error(msg);
                throw new InvalidValueException(msg);
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<ColorDto> {
        @Override
        public ColorDto deserialize(JsonParser p, DeserializationContext ctxt) {
            try {
                JsonNode jsonNode = p.getCodec()
                        .readTree(p);
                return ColorDto.of(jsonNode.asText());
            } catch (IOException e) {
                final var msg = "Deserialize Failed";
                logger.error(msg);
                throw new InvalidValueException(msg);
            }

        }
    }

}
