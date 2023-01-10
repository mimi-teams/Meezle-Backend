package com.mimi.w2m.backend.client.kakao.dto.calendar.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendarColor;
import com.mimi.w2m.backend.dto.calendar.CalendarEventPostRequest;
import lombok.Builder;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * KakaoCalendarEvent
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/10
 **/
@Builder
public record KakaoCalendarEvent(
        @JsonProperty(value = "event_id")
        String eventId,
        @Size(min = 1, max = 50)
        @JsonProperty(value = "title")
        String title,
        @Valid
        @JsonProperty(value = "time")
        KakaoCalendarEventTime time,
        @Valid
        @JsonProperty(value = "rrule")
        RRule rRule,
        @Size(max = 5000)
        @JsonProperty(value = "description")
        String description,
        @JsonProperty(value = "location")
        Location location,
        @Size(max = 2)
        @JsonProperty(value = "reminders")
        List<Integer> reminders,
        @JsonProperty(value = "color")
        KakaoCalendarColor color

) {
    public static KakaoCalendarEvent of(CalendarEventPostRequest event) {
        return KakaoCalendarEvent.builder()
                .title(event.title())
                .time(KakaoCalendarEventTime.of(event.time()))
                .rRule(event.rRule())
                .description(event.description())
                .location(event.location())
                .reminders(event.reminders())
                .color(event.color())
                .build();
    }
}
