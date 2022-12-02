package com.mimi.w2m.backend.type.response.exception;

import com.mimi.w2m.backend.type.response.ApiResultCode;

public class EntityDuplicatedException extends BusinessException {
    public EntityDuplicatedException(String message) {
        super(ApiResultCode.ENTITY_DUPLICATED, message);
    }

    public EntityDuplicatedException(String message, String messageToClient) {
        super(ApiResultCode.ENTITY_DUPLICATED, message, messageToClient);
    }

    public EntityDuplicatedException(String message, String messageToClient, Throwable cause) {
        super(ApiResultCode.ENTITY_DUPLICATED, message, messageToClient, cause);
    }
}