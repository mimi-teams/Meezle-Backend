package com.mimi.w2m.backend.dto;

import com.mimi.w2m.backend.client.kakao.dto.KakaoTokenResponse;
import com.mimi.w2m.backend.type.OAuth2PlatformType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Oauth2 토큰 정보
 *
 * @author yeh35
 * @since 2022-12-04
 */

@Builder
@Getter
public class OAuth2TokenInfo {

    private final OAuth2PlatformType platformType;

    /**
     * 사용자 액세스 토큰 값
     */
    private final String accessToken;

    /**
     * 액세스 토큰과 ID 토큰의 만료 시간(초) 참고: 액세스 토큰과 ID 토큰의 만료 시간은 동일
     */
    private final LocalDateTime accessTokenExpires;

    /**
     * 사용자 리프레시 토큰 값
     */
    private final String refreshToken;

    /**
     * 리프레시 토큰 만료 시간(초)
     */
    private final LocalDateTime refreshTokenExpires;

    public static OAuth2TokenInfo of(KakaoTokenResponse tokenResponse) {
        return OAuth2TokenInfo.builder()
                .platformType(OAuth2PlatformType.KAKAO)
                .accessToken(tokenResponse.getAccessToken())
                .accessTokenExpires(LocalDateTime.now()
                        .plusSeconds(tokenResponse.getAccessTokenExpiresIn()))
                .refreshToken(tokenResponse.getRefreshToken())
                .refreshTokenExpires(LocalDateTime.now()
                        .plusSeconds(tokenResponse.getRefreshTokenExpiresIn()))
                .build();
    }

}