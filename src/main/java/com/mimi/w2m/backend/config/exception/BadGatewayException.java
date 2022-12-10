package com.mimi.w2m.backend.config.exception;

import com.mimi.w2m.backend.dto.base.ApiResultCode;

/**
 * 서버에서 외부에 API를 호출 했는데 실패한 경우
 *
 * @author yeh35
 * @since 2022-12-04
 */
public class BadGatewayException extends BusinessException {

    public BadGatewayException(String message) {
        super(ApiResultCode.ENTITY_NOT_FOUND, message);
    }

}
