package com.mimi.w2m.backend.dto.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * CalendarEventTime
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/10
 **/
@Builder
public record CalendarEventTime(
        /*
        all_day = true 인 경우, start_at = end_at = yyyy-mm-ddT00:00:00Z 으로 설정 되어야 한다.
         */
        @JsonProperty(value = "start_at")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startAt,
        @JsonProperty(value = "end_at")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endAt,
        @JsonProperty(value = "time_zone")
        TimeZone timeZone,
        @JsonProperty(value = "all_day")
        Boolean allDay,
        // 음력을 기준으로 설정(default : false)
        @JsonProperty(value = "lunar")
        Boolean lunar
) {
}