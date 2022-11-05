package com.mimi.w2m.backend.error;

import com.mimi.w2m.backend.dto.ApiResultCode;

/**
 * 잘못된 값으로 요청된 경우 사용
 *
 * @since 2022-11-05
 * @author yeh35
 */
@SuppressWarnings("unused")
public class InvalidValueException extends BusinessException {

    public InvalidValueException(String message) {
        super(ApiResultCode.INVALID_VALUE, message);
    }

    public InvalidValueException(ApiResultCode apiResultCode, String message) {
        super(apiResultCode, message);
    }

    public InvalidValueException(ApiResultCode apiResultCode, String message, String messageToClient) {
        super(apiResultCode, message, messageToClient);
    }

    public InvalidValueException(ApiResultCode apiResultCode, String message, String messageToClient, Throwable cause) {
        super(apiResultCode, message, messageToClient, cause);
    }
}
