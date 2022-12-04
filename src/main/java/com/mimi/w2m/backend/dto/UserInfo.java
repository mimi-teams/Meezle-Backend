package com.mimi.w2m.backend.dto;

import com.mimi.w2m.backend.client.kakao.dto.KakaoUserInfoResponse;
import lombok.Builder;
import lombok.Getter;

/**
 * 유저 정보
 */
@Builder
@Getter 
public class UserInfo {

    private final String email;

    public static UserInfo of(KakaoUserInfoResponse info) {
        return UserInfo.builder()
                       .email(info.getKakaoAccount().getEmail())
                       .build();
    }
}