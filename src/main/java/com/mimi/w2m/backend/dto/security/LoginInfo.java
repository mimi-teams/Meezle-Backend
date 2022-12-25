package com.mimi.w2m.backend.dto.security;

import com.mimi.w2m.backend.domain.type.Role;

import java.io.Serializable;
import java.util.UUID;

/**
 * SessionInfo
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/25
 **/
public record LoginInfo(
        UUID loginId,
        Role role
) implements Serializable {
    public static String key = "info";
}