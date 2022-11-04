package com.mimi.w2m.backend.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : teddy
 * @since : 2022/10/05
 */
@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "사용자"),
    PARTICIPANT("ROLE_PARTICIPANT", "참여자"),
    Tester("ROLE_TESTER", "테스터");
    private final String key;
    private final String name;
}