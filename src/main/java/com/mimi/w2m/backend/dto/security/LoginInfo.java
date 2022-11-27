package com.mimi.w2m.backend.dto.security;

import com.mimi.w2m.backend.domain.type.Role;

import java.io.Serializable;

/**
 * SessionInfo
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/25
 **/
public record LoginInfo(Long loginId,
                        Role role) implements Serializable {
public static String key = "info";
}