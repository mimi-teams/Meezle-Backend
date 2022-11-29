package com.mimi.w2m.backend.converter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mimi.w2m.backend.dto.event.ColorDto;
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
            return null;
        }
    }

}
