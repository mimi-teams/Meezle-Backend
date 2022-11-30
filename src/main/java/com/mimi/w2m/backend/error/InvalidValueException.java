package com.mimi.w2m.backend.error;

import com.mimi.w2m.backend.type.dto.response.ApiResultCode;

/**
 * 잘못된 값으로 요청된 경우 사용
 *
 * @author yeh35
 * @since 2022-11-05
 */
@SuppressWarnings("unused")
public class InvalidValueException extends BusinessException {

public InvalidValueException(String message) {
    super(ApiResultCode.INVALID_VALUE, message);
}

public InvalidValueException(String message, String messageToClient) {
    super(ApiResultCode.INVALID_VALUE, message, messageToClient);
}

public InvalidValueException(String message, String messageToClient, Throwable cause) {
    super(ApiResultCode.INVALID_VALUE, message, messageToClient, cause);
}
}
