package com.mimi.w2m.backend.dto.auth;

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.security.LoginInfo;

import java.io.Serializable;

/**
 * 현재 유저 정보
 *
 * @author yeh35
 * @since 2022-12-11
 */
public record CurrentUserInfo(
        Long userId,
        Role role
) implements Serializable {

    public static CurrentUserInfo of(LoginInfo loginInfo) {
        return new CurrentUserInfo(loginInfo.loginId(), loginInfo.role());
    }

}