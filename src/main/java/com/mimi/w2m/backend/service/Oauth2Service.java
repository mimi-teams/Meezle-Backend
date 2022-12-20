package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.client.kakao.KaKaoApiClient;
import com.mimi.w2m.backend.client.kakao.KaKaoAuthApiClient;
import com.mimi.w2m.backend.client.kakao.dto.KakaoTokenRequest;
import com.mimi.w2m.backend.client.kakao.dto.KakaoTokenResponse;
import com.mimi.w2m.backend.client.kakao.dto.KakaoUserInfoResponse;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.dto.auth.OAuth2TokenInfo;
import com.mimi.w2m.backend.dto.auth.OAuth2UserInfo;
import com.mimi.w2m.backend.domain.type.OAuth2PlatformType;
import com.mimi.w2m.backend.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth2 관련 처리를 해주는 서비스
 *
 * @author yeh35
 * @since 2022-12-04
 */

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class Oauth2Service {

    @Value("${external.client.kakao.oauth2.profile.base-url}")
    protected String kakaoOauth2BaseUrl;
    @Value("${external.client.kakao.oauth2.profile.authorization-uri}")
    protected String kakaoOauth2AuthorizationUrl;
    @Value("${oauth2.kakao.client-id}")
    protected String kakaoOauth2ClientId;
    @Value("${oauth2.kakao.client-secret}")
    protected String kakaoOauth2ClientSecret;
    @Value("${oauth2.kakao.redirect-url}")
    protected String kakaoOauth2RedirectUri;

    private final KaKaoAuthApiClient kaKaoAuthApiClient;
    private final KaKaoApiClient kaoApiClient;
    private final UserService userService;

    /**
     * OAuth2 로그인 URL
     *
     * @author yeh35
     * @since 2022-12-04
     */
    public String getOauthAuthorizationUrl(OAuth2PlatformType platformType) {
        switch (platformType) {
            case KAKAO -> {
                return new StringBuilder(200).append(kakaoOauth2BaseUrl)
                        .append(kakaoOauth2AuthorizationUrl)
                        .append("?")
                        .append("client_id=")
                        .append(kakaoOauth2ClientId)
                        .append("&")
                        .append("redirect_uri=")
                        .append(kakaoOauth2RedirectUri)
                        .append("&")
                        .append("response_type=code")
                        .toString();
            }
            default -> {
                assert false : "처리되지 않은 플렛폼입니다.";
                return "";
            }
        }
    }

    /**
     * OAuth2 로그인 이후 redirect 처리
     *
     * @return token
     * @author yeh35
     * @since 2022-12-04
     */
    @Transactional
    public User afterAuthorization(
            OAuth2PlatformType platformType,
            String authorizationCode
    ) {
        final OAuth2TokenInfo tokenInfo = loadToken(platformType, authorizationCode);
        final OAuth2UserInfo OAuth2UserInfo = loadOAuth2UserInfo(tokenInfo);
        //당장은 뭔가 하는게 없어서 Oauth2 Token을 저장하지 않고 날린다.

        //noinspection UnnecessaryLocalVariable
        final User user = userService.getUserByEmail(OAuth2UserInfo.getEmail()).orElseGet(() -> {
            /*
             * 회원가입 처리
             */
            return userService.registerUser(OAuth2UserInfo.getName(), OAuth2UserInfo.getEmail());
        });

        return user;
    }

    /**
     * OAuth2 토큰 받아오기
     *
     * @author yeh35
     * @since 2022-12-04
     */
    protected OAuth2TokenInfo loadToken(
            OAuth2PlatformType platformType,
            String authorizationCode
    ) {
        switch (platformType) {
            case KAKAO -> {
                final var tokenRequest = KakaoTokenRequest.builder()
                        .clientId(kakaoOauth2ClientId)
                        .clientSecret(kakaoOauth2ClientSecret)
                        .redirectUri(kakaoOauth2RedirectUri)
                        .code(authorizationCode)
                        .build();


                KakaoTokenResponse tokenResponse = kaKaoAuthApiClient.getToken(tokenRequest.toMap());
                return OAuth2TokenInfo.of(tokenResponse);
            }
            default -> {
                assert false : "처리되지 않은 플렛폼입니다.";
                return null;
            }
        }
    }

    /**
     * 유저 정보 가져오기
     *
     * @author yeh35
     * @since 2022-12-04
     */
    protected OAuth2UserInfo loadOAuth2UserInfo(OAuth2TokenInfo tokenInfo) {
        switch (tokenInfo.getPlatformType()) {
            case KAKAO -> {
                KakaoUserInfoResponse userInfo = kaoApiClient.getUserInfo(HttpUtils.withBearerToken(tokenInfo.getAccessToken()));
                return OAuth2UserInfo.of(userInfo);
            }
            default -> {
                assert false : "처리되지 않은 플렛폼입니다.";
                return null;
            }
        }
    }

}