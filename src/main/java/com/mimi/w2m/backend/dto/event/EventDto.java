package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Builder
@Schema
public class EventDto {

@Schema(description = "신규 생성시에는 null을 넘겨주시면 됩니다.")
private final Long id;

private final String         title;
private final Set<DayOfWeek> dayOfWeeks;
private final LocalTime      beginTime;
private final LocalTime      endTime;

@Schema(description = "선택 안함은 null")
private final LocalDateTime dDay;

private final Color color;

@Schema(description = "1000자까지 됩니다.")
private final String description;

public static EventDto of(Event entity) {
    return EventDto.builder()
                   .title(entity.getTitle())
                   .beginTime(entity.getBeginTime())
                   .endTime(entity.getEndTime())
                   .dayOfWeeks(entity.getDayOfWeeks())
                   .dDay(entity.getDDay())
                   .color(entity.getColor())
                   .description(entity.getDescription())
                   .build();
}

public Event createEntity(User user) {
    return Event.builder()
                .user(user)
                .title(title)
                .beginTime(beginTime)
                .endTime(endTime)
                .dayOfWeeks(dayOfWeeks)
                .dDay(dDay)
                .color(color)
                .description(description)
                .build();
}
}
