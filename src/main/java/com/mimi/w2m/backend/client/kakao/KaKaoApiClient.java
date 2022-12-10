package com.mimi.w2m.backend.client.kakao;

import com.mimi.w2m.backend.client.kakao.dto.KakaoUserInfoResponse;
import com.mimi.w2m.backend.config.exception.BadGatewayException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoApiClient",
        url = "${external.client.kakao.kapi.profile.base-url}",
        configuration = {KaKaoFeignConfig.class})
public interface KaKaoApiClient {

    /**
     * @author yeh35
     * @see <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info">Kakao 공식 문서</a>
     * @since 2022-12-04
     */
    @Retryable(backoff = @Backoff(delay = 50, multiplier = 2, maxDelay = 1000), value = BadGatewayException.class)
    @PostMapping("${external.client.kakao.kapi.profile.user-info-uri}")
    KakaoUserInfoResponse getUserInfo(
            @RequestHeader("Authorization") String accessToken
    );


}
