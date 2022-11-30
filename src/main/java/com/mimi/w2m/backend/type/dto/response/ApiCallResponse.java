package com.mimi.w2m.backend.type.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author yeh35
 * @since 2022-11-05
 */

@Schema(title = "ApiCallResponse", description = "API 에 응답에 일관성을 유지하기 위해서 존재한다.",
        requiredProperties = {"code", "message"})
@Getter
public class ApiCallResponse<T> implements Serializable {

    @Valid
    @NotNull
    private final ApiResultCode code;

    /**
     * `null`또는 `blank`인 경우 `code`에 `defaultMessage`가 대체한다.
     */
    @NotNull
    private final String message;
    @Valid
    @Nullable
    private final T      data;

    public ApiCallResponse(ApiResultCode code, String message, T data) {
        this.code    = code;
        this.message = Objects.nonNull(message) ? message : code.defaultMessage;
        this.data    = data;
    }

    public static <T> ApiCallResponse<T> ofSuccess(T data) {
        return new ApiCallResponse<>(ApiResultCode.SUCCESS, "", data);
    }

    @SuppressWarnings("unused")
    public static <T> ApiCallResponse<T> of(ApiResultCode code, T data) {
        return new ApiCallResponse<>(code, code.defaultMessage, data);
    }

    @SuppressWarnings("unused")
    public static <T> ApiCallResponse<T> of(ApiResultCode code, String message, T data) {
        return new ApiCallResponse<>(code, message, data);
    }
}
