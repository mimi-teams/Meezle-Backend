package com.mimi.w2m.backend.client.kakao.dto.calendar.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mimi.w2m.backend.dto.calendar.CalendarEventTime;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * KakaoCalendarEventTime
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/10
 **/
@Builder
public record KakaoCalendarEventTime(
        /*
        all_day = true 인 경우, start_at = end_at = yyyy-mm-ddT00:00:00Z 으로 설정 되어야 한다.
         */
        @JsonProperty(value = "start_at")
        @DateTimeFormat(pattern = "yyyy-MM-dd`T`HH:mm:ss`Z`")
        LocalDateTime startAt,
        @JsonProperty(value = "end_at")
        @DateTimeFormat(pattern = "yyyy-MM-dd`T`HH:mm:ss`Z`")
        LocalDateTime endAt,
        @JsonProperty(value = "time_zone")
        TimeZone timeZone,
        @JsonProperty(value = "all_day")
        Boolean allDay,
        // 음력을 기준으로 설정(default : false)
        @JsonProperty(value = "lunar")
        Boolean lunar

) {
    public static KakaoCalendarEventTime of(CalendarEventTime eventTime) {
        return KakaoCalendarEventTime.builder()
                .startAt(eventTime.startAt())
                .endAt(eventTime.endAt())
                .timeZone(eventTime.timeZone())
                .allDay(eventTime.allDay())
                .lunar(eventTime.lunar())
                .build();
    }

    public CalendarEventTime to() {
        return CalendarEventTime.builder()
                .startAt(startAt)
                .endAt(endAt)
                .timeZone(timeZone)
                .allDay(allDay)
                .lunar(lunar)
                .build();
    }
}