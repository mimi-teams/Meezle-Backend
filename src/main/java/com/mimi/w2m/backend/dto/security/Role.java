package com.mimi.w2m.backend.dto.security;

import lombok.RequiredArgsConstructor;

/**
 * @author : teddy
 * @since : 2022/10/05
 */
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "사용자");
    private final String key;
    private final String name;
}