package com.mimi.w2m.backend.error;

import com.mimi.w2m.backend.type.dto.response.ApiResultCode;

/**
 * `Entity`를 못 찾은 경우 사용
 *
 * @author yeh35
 * @since 2022-11-05
 */
@SuppressWarnings("unused")
public class EntityNotFoundException extends BusinessException {
public EntityNotFoundException(String message) {
    super(ApiResultCode.ENTITY_NOT_FOUND, message);
}

public EntityNotFoundException(String message, String messageToClient) {
    super(ApiResultCode.ENTITY_NOT_FOUND, message, messageToClient);
}

public EntityNotFoundException(String message, String messageToClient, Throwable cause) {
    super(ApiResultCode.ENTITY_NOT_FOUND, message, messageToClient, cause);
}
}
