package com.mimi.w2m.backend.error;

import com.mimi.w2m.backend.dto.ApiResultCode;

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