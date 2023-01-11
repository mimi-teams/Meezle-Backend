package com.mimi.w2m.backend.client.kakao.dto.calendar.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mimi.w2m.backend.client.kakao.dto.calendar.event.type.KakaoCalendarEventLocation;
import com.mimi.w2m.backend.client.kakao.dto.calendar.event.type.KakaoCalendarEventType;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendarBanner;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendarColor;
import com.mimi.w2m.backend.dto.calendar.CalendarRRule;
import lombok.Builder;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * KakaoCalendarEventGetResponse
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/11
 **/
@Builder
public record KakaoCalendarEventGetResponse(
        @JsonProperty(value = "id", required = true)
        String id,
        @Size(min = 1, max = 50)
        @JsonProperty(value = "title", required = true)
        String title,
        @JsonProperty(value = "type", required = true)
        KakaoCalendarEventType type,
        @JsonProperty(value = "calendar_id", required = true)
        String calendarId,
        @Valid
        @JsonProperty(value = "time", required = true)
        KakaoCalendarEventTime time,
        @JsonProperty(value = "is_host", required = true)
        Boolean isHost,
        @JsonProperty(value = "is_recur_event")
        Boolean isRecurEvent, // 반복 일정 여부
        @Valid
        @JsonProperty(value = "rrule")
        CalendarRRule rRule,
        @JsonProperty(value = "dt_start")
        String dtStart, //반복 일정의 시작 시간
        @Size(max = 5000)
        @JsonProperty(value = "description")
        String description,
        @JsonProperty(value = "location")
        KakaoCalendarEventLocation location,
        @Size(max = 2)
        @JsonProperty(value = "reminders")
        List<Integer> reminders, // 이벤트 시작 전, 알림(단위 : 분)
        @JsonProperty(value = "color")
        KakaoCalendarColor color,
        @JsonProperty(value = "memo")
        String memo, // 일정에 대한 메모
        @JsonProperty(value = "banner")
        KakaoCalendarBanner banner

) {
    public KakaoCalendarEvent to() {
        return KakaoCalendarEvent.builder()
                .eventId(id)
                .title(title)
                .time(time)
                .rRule(rRule)
                .description(description)
                .location(location)
                .reminders(reminders)
                .color(color)
                .build();
    }
}
