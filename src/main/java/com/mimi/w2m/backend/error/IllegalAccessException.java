package com.mimi.w2m.backend.error;

import com.mimi.w2m.backend.dto.ApiResultCode;

public class IllegalAccessException extends BusinessException {

    public IllegalAccessException(String message) {
        super(ApiResultCode.ILLEGAL_ACCESS, message);
    }

    public IllegalAccessException(String message, String messageToClient) {
        super(ApiResultCode.ILLEGAL_ACCESS, message, messageToClient);
    }

    public IllegalAccessException(ApiResultCode apiResultCode, String message) {
        super(apiResultCode, message);
    }

    public IllegalAccessException(ApiResultCode apiResultCode, String message, String messageToClient) {
        super(apiResultCode, message, messageToClient);
    }

    public IllegalAccessException(ApiResultCode apiResultCode, String message, String messageToClient, Throwable cause) {
        super(apiResultCode, message, messageToClient, cause);
    }
}