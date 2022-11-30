package com.mimi.w2m.backend.type.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.mimi.w2m.backend.type.common.Role;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Objects;

/**
 * RoleJsonConverter
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/01
 **/
@JsonComponent
public class RoleJsonConverter {
    public static class Serializer extends JsonSerializer<Role> {

        @Override
        public void serialize(Role value, JsonGenerator gen, SerializerProvider serializers) {
            try {
                gen.writeString(value.getKey());
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<Role> {

        @Override
        public Role deserialize(JsonParser p, DeserializationContext ctxt) {
            JsonNode jsonNode = null;
            try {
                jsonNode = p.getCodec()
                            .readTree(p);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
            final var roleStr = jsonNode.asText();
            if(Objects.equals(roleStr, Role.USER.getKey())) {
                return Role.USER;
            } else if(Objects.equals(roleStr, Role.GUEST.getKey())) {
                return Role.GUEST;
            } else {
                return Role.NONE;
            }
        }
    }
}