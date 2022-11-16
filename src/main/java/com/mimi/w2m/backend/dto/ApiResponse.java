package com.mimi.w2m.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * @author yeh35
 * @since 2022-11-05
 */

@Schema(name = "ApiResponse", description = "API에 응답에 일관성을 유지하기 위해서 존재한다.")
@Getter
public class ApiResponse<T> {

private final ApiResultCode code;

/**
 * `null`또는 `blank`인 경우 `code`에 `defaultMessage`가 대체한다.
 */
private final @Nullable String message;
private final           T      data;

public ApiResponse(ApiResultCode code, String message, T data) {
    this.code    = code;
    this.message = message;
    this.data    = data;
}

public static <T> ApiResponse<T> ofSuccess(T data) {
    return new ApiResponse<>(ApiResultCode.SUCCESS, "", data);
}

@SuppressWarnings("unused")
public static <T> ApiResponse<T> of(ApiResultCode code, T data) {
    return new ApiResponse<>(code, "", data);
}

@SuppressWarnings("unused")
public static <T> ApiResponse<T> of(ApiResultCode code, String message, T data) {
    return new ApiResponse<>(code, message, data);
}
}
