package com.mimi.w2m.backend.type.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

/**
 * API 응답 코드 규약
 *
 * @author yeh35
 * @since 2022-11-05
 */

@Schema(title = "API 결과 코드", description = "각 상태에 맞는 코드를 제공한다",
        requiredProperties = {"code", "httpStatus", "defaultMessage"})
public enum ApiResultCode {

    SUCCESS(1, ApiHttpStatus.OK, "처리 완료"),
    SERVER_ERROR(2, ApiHttpStatus.ERROR, "알 수 없는 서버 에러"),
    ENTITY_NOT_FOUND(3, ApiHttpStatus.NOT_FOUND, "해당 Entity 를 찾을 수 없음"),

    INVALID_VALUE(4, ApiHttpStatus.BAD_REQUEST, "잘못된 값으로 요청"),
    UNAUTHORIZED(5, ApiHttpStatus.UNAUTHORIZED, "잘못된 접근입니다."),
    EVENT_NOT_FOUND(7, ApiHttpStatus.NOT_FOUND, "이벤트가 존재하지 않습니다."),
    ENTITY_DUPLICATED(8, ApiHttpStatus.DUPLICATED, "해당 Entity 가 이미 존재함"),
    UNUSED_API(9, ApiHttpStatus.BAD_REQUEST, "사용하지 않는 API 입니다"),

    ;

    /**
     * 코드는 추가되는 순서대로 붙이자 규칙성을 부여하면 좋겠지만 나중에 APP 서비스가 나와서 클라이언트를 수정하기 힘든 경우 고도화 하자
     *
     * @since 2022-11-05
     */
    public final int           code;
    public final ApiHttpStatus httpStatus;
    public final String        defaultMessage;

    ApiResultCode(int code, ApiHttpStatus httpStatus, String defaultMessage) {
        this.code           = code;
        this.httpStatus     = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public static ApiResultCode ofCode(int code) {
        return Arrays.stream(ApiResultCode.values())
                     .filter(apiResultCode -> apiResultCode.code == code)
                     .findFirst()
                     .orElseThrow(() -> new EntityNotFoundException("잘못된 코드 입니다. code : " + code));
    }
}
