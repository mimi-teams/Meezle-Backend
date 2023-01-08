package com.mimi.w2m.backend.domain.type;

import lombok.Getter;

public enum OAuth2PlatformType {

    /**
     * 카카오 간변 로그인
     */
    KAKAO(Value.KAKAO_KEY);
    @Getter
    private final String key;

    OAuth2PlatformType(String key) {
        this.key = key;
    }

    private static class Value {
        private final static String KAKAO_KEY = "KAKAO";
    }

}
