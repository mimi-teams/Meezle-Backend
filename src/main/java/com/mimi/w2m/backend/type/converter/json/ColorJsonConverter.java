package com.mimi.w2m.backend.type.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.mimi.w2m.backend.type.dto.event.ColorDto;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ColorJsonConverter {

    public static class Serializer extends JsonSerializer<ColorDto> {

        @Override
        public void serialize(ColorDto value, JsonGenerator gen, SerializerProvider serializers) {
            try {
                gen.writeString(value.toString());
            } catch(IOException e) {
                throw new RuntimeException(e);
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

            } catch(IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
