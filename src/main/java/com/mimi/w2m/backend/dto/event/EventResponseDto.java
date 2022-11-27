package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

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

private final Long           eventId;
@Schema(description = "이벤트 생성자 id")
private final Long           hostId;
private final String         title;
@DateTimeFormat(pattern = "yyyy-mm-dd'T'hh:mm:ss")
private final LocalDateTime  deletedDate;
@DateTimeFormat(pattern = "yyyy-mm-dd'T'hh:mm:ss")
private final LocalDateTime  dDay;
@Schema(description = "MONDAY,... 의 형식")
private final String         dayOfWeeks;
@Schema(description = "hh:mm:ss-hh:mm:ss 의 형식")
private final ParticipleTime participleTime;
private final ColorDto       color;
@Schema(description = "1000자까지 됩니다.")
private final String         description;

public static EventResponseDto of(Event entity) {
    return EventResponseDto.builder()
                           .eventId(entity.getId())
                           .hostId(entity.getUser().getId())
                           .title(entity.getTitle())
                           .deletedDate(entity.getDeletedAt())
                           .dDay(entity.getDDay())
                           .dayOfWeeks(new SetDayOfWeekConverter().convertToDatabaseColumn(entity.getDayOfWeeks()))
                           .participleTime(entity.getParticipleTime())
                           .color(ColorDto.of(entity.getColor()))
                           .description(entity.getDescription())
                           .build();
}
}
