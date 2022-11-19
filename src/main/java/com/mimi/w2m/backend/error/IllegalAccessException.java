package com.mimi.w2m.backend.error;

import com.mimi.w2m.backend.dto.ApiResultCode;

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