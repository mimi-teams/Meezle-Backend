package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * EventResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Builder
@Schema
public class EventResponseDto implements Serializable {

private final Long          eventId;
@Schema(description = "이벤트 생성자 id")
private final Long          userId;
private final String        title;
@DateTimeFormat(pattern = "yyyy-mm-dd'T'hh:mm:ss")
private final LocalDateTime deletedDate;
@DateTimeFormat(pattern = "yyyy-mm-dd'T'hh:mm:ss")
private final LocalDateTime dDay;
@Schema(description = "MONDAY,... 의 형식")
private final String        dayOfWeeks;
@Schema(description = "ParticipleTime의 공통 부분 or 확정된 시간")
@DateTimeFormat(pattern = "hh:mm:ss")
private final LocalTime     beginTime;
@Schema(description = "ParticipleTime의 공통 부분 or 확정된 시간")
@DateTimeFormat(pattern = "hh:mm:ss")
private final LocalTime     endTime;
private final ColorDto      color;
@Schema(description = "1000자까지 됩니다.")
private final String        description;

public static EventResponseDto of(Event entity) {
    return EventResponseDto.builder()
                           .eventId(entity.getId())
                           .userId(entity.getUser().getId())
                           .title(entity.getTitle())
                           .deletedDate(entity.getDeletedDate())
                           .dDay(entity.getDDay())
                           .dayOfWeeks(new SetDayOfWeekConverter().convertToDatabaseColumn(entity.getDayOfWeeks()))
                           .beginTime(entity.getBeginTime())
                           .endTime(entity.getEndTime())
                           .color(ColorDto.of(entity.getColor()))
                           .description(entity.getDescription())
                           .build();
}
}
