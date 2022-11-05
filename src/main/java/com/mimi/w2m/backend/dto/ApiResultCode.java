package com.mimi.w2m.backend.dto;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

import static com.mimi.w2m.backend.dto.ApiHttpStatus.*;

/**
 * API 응답 코드 규약
 *
 * @since 2022-11-05
 * @author yeh35
 */

public enum ApiResultCode {

    SUCCESS(1, OK, "처리 완료"),
    SERVER_ERROR(2, ERROR, "알 수 없는 서버 에러"),
    ENTITY_NOT_FOUND(3, NOT_FOUND, "해당 Entity 를 찾을 수 없음"),
    INVALID_VALUE(4, BAD_REQUEST, "잘못된 값으로 요청"),

    ;

    /**
     * 코드는 추가되는 순서대로 붙이자
     * 규칙성을 부여하면 좋겠지만 나중에 APP 서비스가 나와서 클라이언트를 수정하기 힘든 경우 고도화 하자
     * @since 2022-11-05
     */
    public final int code;
    public final ApiHttpStatus httpStatus;
    public final String defaultMessage;

    ApiResultCode(int code, ApiHttpStatus httpStatus, String defaultMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public static ApiResultCode ofCode(int code) {
        return Arrays.stream(ApiResultCode.values())
                .filter(apiResultCode -> apiResultCode.code == code)
                .findFirst()
                .orElseThrow(() ->  new EntityNotFoundException("잘못된 코드 입니다. code : " + code));
    }
}
