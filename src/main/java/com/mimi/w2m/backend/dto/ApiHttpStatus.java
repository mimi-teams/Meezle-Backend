package com.mimi.w2m.backend.dto;

/**
 * 우리 서비스에서 사용 가능한 HTTP Status를 규정한다. <br>
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Status">HTTP status</a>
 *
 * @since 2022-11-05
 * @author yeh35
 */
public enum ApiHttpStatus {

    OK(200),

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),

    ERROR(500),
    ;

    public final int httpStatus;

    ApiHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
