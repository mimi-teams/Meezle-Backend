package com.mimi.w2m.backend.error;

import com.mimi.w2m.backend.dto.ApiResultCode;

/**
 * 인증이 안된 경우
 *
 * @since 2022-11-05
 * @author yeh35
 */
@SuppressWarnings("unused")
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super(ApiResultCode.NOT_LOGIN, message);
    }

    public UnauthorizedException(String message, String messageToClient) {
        super(ApiResultCode.NOT_LOGIN, message, messageToClient);
    }

    public UnauthorizedException(ApiResultCode apiResultCode, String message) {
        super(apiResultCode, message);
    }

    public UnauthorizedException(ApiResultCode apiResultCode, String message, String messageToClient) {
        super(apiResultCode, message, messageToClient);
    }

    public UnauthorizedException(ApiResultCode apiResultCode, String message, String messageToClient, Throwable cause) {
        super(apiResultCode, message, messageToClient, cause);
    }
}
