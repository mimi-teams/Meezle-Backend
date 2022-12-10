package com.mimi.w2m.backend.config.exception;

import com.mimi.w2m.backend.dto.base.ApiResultCode;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super(ApiResultCode.ENTITY_NOT_FOUND, message);
    }

}
