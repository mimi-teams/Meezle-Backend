package com.mimi.w2m.backend.domain.type;

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
    USER(Value.USER_KEY, "사용자"),
    GUEST(Value.GUEST_KEY, "참여자"),
    ;
    private final String key;
    private final String name;

    public static Role ofKey(final String key) {

        switch (key) {
            case Value.USER_KEY -> {
                return USER;
            }
            case Value.GUEST_KEY -> {
                return GUEST;
            }
            default -> {
                assert false : "여기 오면 안되는데..?";
                return GUEST;
            }
        }
    }

    public static class Value {
        private final static String USER_KEY = "ROLE_USER";
        private final static String GUEST_KEY = "ROLE_GUEST";
    }
}