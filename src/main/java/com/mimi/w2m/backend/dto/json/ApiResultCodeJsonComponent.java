package com.mimi.w2m.backend.dto.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mimi.w2m.backend.dto.ApiResultCode;
import com.mimi.w2m.backend.error.InvalidValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author yeh35
 * @since 2022-11-05
 */

@JsonComponent
public class ApiResultCodeJsonComponent {

private static final Logger logger = LoggerFactory.getLogger(ApiResultCodeJsonComponent.class);

public static class Serializer extends JsonSerializer<ApiResultCode> {
    @Override
    public void serialize(ApiResultCode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(String.valueOf(value.code));
    }
}

public static class Deserializer extends JsonDeserializer<ApiResultCode> {

    @Override
    public ApiResultCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final var valueAsString = p.getValueAsString();
        try {
            final var code = Integer.parseInt(valueAsString);
            return ApiResultCode.ofCode(code);
        } catch(NumberFormatException e) {
            logger.error("숫자 변환 실패", e);
            throw new InvalidValueException(String.format("ApiResult CODE가 숫자가 아닙니다. : %s", valueAsString));
        }
    }
}

}
