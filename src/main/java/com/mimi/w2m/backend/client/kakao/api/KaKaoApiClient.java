package com.mimi.w2m.backend.client.kakao.api;

import com.mimi.w2m.backend.client.kakao.config.KaKaoFeignConfig;
import com.mimi.w2m.backend.client.kakao.dto.calendar.KakaoCalenderListResponse;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendarColor;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendarType;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalenderPostResponse;
import com.mimi.w2m.backend.client.kakao.dto.user.KakaoUserInfoResponse;
import com.mimi.w2m.backend.config.exception.BadGatewayException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Retryable(backoff = @Backoff(delay = 50, multiplier = 2, maxDelay = 1000), value = BadGatewayException.class)
    @GetMapping("/v2/api/calendar/calendars")
    KakaoCalenderListResponse getCalenders(
            @RequestHeader("Authorization") String accessToken,
            /*
            목록을 가져올 캘린더 타입
             */
            @RequestParam(name = "filter", required = false) KakaoCalendarType filter
    );

    @Retryable(backoff = @Backoff(delay = 50, multiplier = 2, maxDelay = 1000), value = BadGatewayException.class)
    @PostMapping("/v2/api/calendar/create/calendar")
    KakaoCalenderPostResponse createCalendar(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "color", required = false) KakaoCalendarColor color,
            @RequestParam(name = "reminder", required = false) Integer reminder,
            @RequestParam(name = "reminder_all_day", required = false) Integer reminderAllDay);
}
