package com.mimi.w2m.backend.type.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author yeh35
 * @since 2022-11-05
 */

@Schema(title = "ApiCallResponse", description = "API 에 응답에 일관성을 유지하기 위해서 존재한다.",
        requiredProperties = {"code", "message", "data"})
@Getter
public class ApiCallResponse<T> implements Serializable {

    @NotNull
    private final ApiResultCode code;

    /**
     * `null`또는 `blank`인 경우 `code`에 `defaultMessage`가 대체한다.
     */
    @Schema(type = "String", title = "응답에 대한 상태 메시지")
    @NotNull
    private final String message;
    @Valid
    @Nullable
    private final T      data;

    private ApiCallResponse(ApiResultCode code, String message, T data) {
        this.code    = code;
        this.message = (Objects.isNull(message) || message.isEmpty()) ? code.defaultMessage : message;
        this.data    = data;
    }

    public static <T> ApiCallResponse<T> ofSuccess(T data) {
        return new ApiCallResponse<>(ApiResultCode.SUCCESS, null, data);
    }

    @SuppressWarnings("unused")
    public static <T> ApiCallResponse<T> of(ApiResultCode code, T data) {
        return new ApiCallResponse<>(code, null, data);
    }

    @SuppressWarnings("unused")
    public static <T> ApiCallResponse<T> of(ApiResultCode code, String message, T data) {
        return new ApiCallResponse<>(code, message, data);
    }

    public static Class<?> getGenericClass(Type genericType) {
        return null;
    }
}
