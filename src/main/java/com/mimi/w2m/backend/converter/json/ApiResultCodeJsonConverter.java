package com.mimi.w2m.backend.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mimi.w2m.backend.config.exception.InvalidValueException;
import com.mimi.w2m.backend.dto.base.ApiResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author yeh35
 * @since 2022-11-05
 */

@JsonComponent
public class ApiResultCodeJsonConverter {
    private final static Logger logger = LoggerFactory.getLogger(ApiResultCodeJsonConverter.class.getName());

    public static class Serializer extends JsonSerializer<ApiResultCode> {
        @Override
        public void serialize(ApiResultCode value, JsonGenerator gen, SerializerProvider serializers)
                throws InvalidValueException {
            try {
                gen.writeString(String.valueOf(value.code));
            } catch (IOException e) {
                final var msg = "Serialize Failed";
                logger.error(msg);
                throw new InvalidValueException(msg);
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<ApiResultCode> {
        @Override
        public ApiResultCode deserialize(JsonParser p, DeserializationContext ctxt) throws InvalidValueException {
            try {
                final var valueAsString = p.getValueAsString();
                final var code = Integer.parseInt(valueAsString);
                return ApiResultCode.ofCode(code);
            } catch (Exception e) {
                final var msg = "Deserialize Failed";
                logger.error(msg);
                throw new InvalidValueException(msg);
            }
        }
    }

}
