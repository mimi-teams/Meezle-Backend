package com.mimi.w2m.backend.error;

import com.mimi.w2m.backend.type.dto.response.ApiResultCode;

/**
 * 비지니스 로직에 최상위 Exception 으로 모든 예외를 처리하고 싶으면 이 클래스를 체크하면 된다.
 *
 * @author yeh35
 * @since 2022-11-05
 */
@SuppressWarnings("unused")
public class BusinessException extends RuntimeException {

public final ApiResultCode apiResultCode;

/**
 * 서버 로그상에만 남는 Message이다.
 *
 * @since 2022-11-05
 */
public final String message;

/**
 * 클라이언트에게 넘겨줄 Message
 *
 * @since 2022-11-05
 */
public final String messageToClient;

public BusinessException(String message) {
    super(message);
    this.apiResultCode   = ApiResultCode.SERVER_ERROR;
    this.message         = message;
    this.messageToClient = apiResultCode.defaultMessage;
}

public BusinessException(String message, String messageToClient) {
    super(message);
    this.apiResultCode   = ApiResultCode.SERVER_ERROR;
    this.message         = message;
    this.messageToClient = messageToClient;
}

public BusinessException(String message, String messageToClient, Throwable cause) {
    super(message, cause);
    this.apiResultCode   = ApiResultCode.SERVER_ERROR;
    this.message         = message;
    this.messageToClient = messageToClient;
}

protected BusinessException(ApiResultCode apiResultCode, String message) {
    super(message);
    this.apiResultCode   = apiResultCode;
    this.message         = message;
    this.messageToClient = apiResultCode.defaultMessage;
}

protected BusinessException(ApiResultCode apiResultCode, String message, String messageToClient) {
    super(message);
    this.apiResultCode   = apiResultCode;
    this.message         = message;
    this.messageToClient = messageToClient;
}

protected BusinessException(ApiResultCode apiResultCode, String message, String messageToClient, Throwable cause) {
    super(message, cause);
    this.apiResultCode   = apiResultCode;
    this.message         = message;
    this.messageToClient = messageToClient;
}
}
