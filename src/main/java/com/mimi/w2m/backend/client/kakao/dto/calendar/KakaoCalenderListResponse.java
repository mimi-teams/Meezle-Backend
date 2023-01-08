package com.mimi.w2m.backend.client.kakao.dto.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendar;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoSubscribeCalendar;

import java.util.List;

/**
 * KakaoCalenderListResponse
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/08
 **/
public record KakaoCalenderListResponse(
        @JsonProperty(value = "calendars")
        List<KakaoCalendar> calendars,
        @JsonProperty(value = "subscribe_calendars")
        List<KakaoSubscribeCalendar> subscribeCalendars
) {

}