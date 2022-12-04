package com.mimi.w2m.backend.type.response;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

/**
 * API 응답 코드 규약
 *
 * @author yeh35
 * @since 2022-11-05
 */

@Schema(title = "API 결과 코드",
        description = "각 상태에 맞는 코드를 제공한다",
        requiredProperties = {"code", "httpStatus", "defaultMessage"})
public enum ApiResultCode {

    SUCCESS(1, ApiHttpStatus.OK, "요청이 정상적으로 처리되었습니다"),
    ILLEGAL_ACCESS(2, ApiHttpStatus.ILLEGAL_ACCESS, "허가되지 않은 접근입니다"),
    ENTITY_NOT_FOUND(3, ApiHttpStatus.NOT_FOUND, "찾으려는 대상이 존재하지 않습니다"),
    ENTITY_DUPLICATED(4, ApiHttpStatus.DUPLICATED, "대상이 이미 존재합니다"),
    BAD_REQUEST(5, ApiHttpStatus.BAD_REQUEST, "잘못된 형식의 요청입니다"),
    SERVER_ERROR(6, ApiHttpStatus.ERROR, "서버 에러가 발생했습니다"),

    /*
     * 외부 시스템에서 에러가 발생한 경우
     */
    BAD_GATEWAY(7, ApiHttpStatus.ERROR, "일시적인 에러가 발생하였습니다.\n잠시 후 다시 시도해주세요!"),
    ;

    /**
     * 코드는 추가되는 순서대로 붙이자 규칙성을 부여하면 좋겠지만 나중에 APP 서비스가 나와서 클라이언트를 수정하기 힘든 경우 고도화 하자
     *
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
                .orElseThrow(() -> new EntityNotFoundException("잘못된 코드 입니다. code : " + code));
    }
}
