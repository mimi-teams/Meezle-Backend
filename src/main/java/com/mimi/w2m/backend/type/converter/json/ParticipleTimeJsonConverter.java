package com.mimi.w2m.backend.type.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.mimi.w2m.backend.type.common.ParticipleTime;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ParticipleTimeJsonConverter {
    public static class Serializer extends JsonSerializer<ParticipleTime> {

        @Override
        public void serialize(ParticipleTime value, JsonGenerator gen, SerializerProvider serializers) {
            try {
                gen.writeString(value.toString());
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<ParticipleTime> {

        @Override
        public ParticipleTime deserialize(JsonParser p, DeserializationContext ctxt) {
            try {
                JsonNode jsonNode = p.getCodec()
                                     .readTree(p);
                return ParticipleTime.of(jsonNode.asText());
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}