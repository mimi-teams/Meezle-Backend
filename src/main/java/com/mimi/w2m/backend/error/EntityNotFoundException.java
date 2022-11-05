package com.mimi.w2m.backend.error;

import com.mimi.w2m.backend.dto.ApiResultCode;

/**
 * `Entity`를 못 찾은 경우 사용
 *
 * @since 2022-11-05
 * @author yeh35
 */
@SuppressWarnings("unused")
public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super(ApiResultCode.ENTITY_NOT_FOUND, message);
    }

    public EntityNotFoundException(ApiResultCode apiResultCode, String message) {
        super(apiResultCode, message);
    }

    public EntityNotFoundException(ApiResultCode apiResultCode, String message, String messageToClient) {
        super(apiResultCode, message, messageToClient);
    }

    public EntityNotFoundException(ApiResultCode apiResultCode, String message, String messageToClient, Throwable cause) {
        super(apiResultCode, message, messageToClient, cause);
    }
}
