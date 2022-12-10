package com.mimi.w2m.backend.config.exception;

import com.mimi.w2m.backend.dto.base.ApiResultCode;

/**
 * 인증이 안된 경우
 *
 * @author yeh35
 * @since 2022-11-05
 */
@SuppressWarnings("unused")
public class IllegalAccessException extends BusinessException {

    public IllegalAccessException(String message) {
        super(ApiResultCode.ILLEGAL_ACCESS, message);
    }

    public IllegalAccessException(String message, String messageToClient) {
        super(ApiResultCode.ILLEGAL_ACCESS, message, messageToClient);
    }

    public IllegalAccessException(String message, String messageToClient, Throwable cause) {
        super(ApiResultCode.ILLEGAL_ACCESS, message, messageToClient, cause);
    }
}
