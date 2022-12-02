package com.mimi.w2m.backend.type.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Role
 *
 * @version 1.0.0
 **/
@Getter
@RequiredArgsConstructor
@Schema(type = "String", title = "로그인한 사용자의 유형", description = "USER or GUEST", example = "ROLE_USER",
        allowableValues = {"ROLE_USER", "ROLE_GUEST"})
public enum Role {
    USER("ROLE_USER", "사용자"),
    GUEST("ROLE_GUEST", "참여자"),
    ;
    private final String key;
    private final String name;
}