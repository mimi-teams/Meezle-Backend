package com.mimi.w2m.backend.type.dto.response;

/**
 * 우리 서비스에서 사용 가능한 HTTP Status를 규정한다. <br>
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Status">HTTP status</a>
 *
 * @author yeh35
 * @since 2022-11-05
 */
public enum ApiHttpStatus {

    OK(200),

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    DUPLICATED(405),

    ERROR(500),
    ;

public final int httpStatus;

ApiHttpStatus(int httpStatus) {
    this.httpStatus = httpStatus;
}
}
