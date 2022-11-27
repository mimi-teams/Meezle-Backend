package com.mimi.w2m.backend.dto.participle;

import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import com.mimi.w2m.backend.domain.converter.SetParticipleTimeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * EventParticipleTimeResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema
public class EventParticipleTimeResponseDto implements Serializable {
private Long   eventParticipleTimeId;
@Schema(description = "MONDAY,... 의 형식")
private String ableDayOfWeeks;
@Schema(description = "beginTime-endTime,... 의 형식")
private String participleTimes;
@Schema(description = "연관된 event")
private Long   eventId;

@Builder
public EventParticipleTimeResponseDto(Long eventParticipleTimeId, String ableDayOfWeeks, String participleTimes,
                                      Long eventId) {
    this.eventParticipleTimeId = eventParticipleTimeId;
    this.ableDayOfWeeks        = ableDayOfWeeks;
    this.participleTimes       = participleTimes;
    this.eventId               = eventId;
}

protected EventParticipleTimeResponseDto() {
}

public static EventParticipleTimeResponseDto of(EventParticipleTime entity) {
    return EventParticipleTimeResponseDto.builder()
                                         .eventParticipleTimeId(entity.getId())
                                         .ableDayOfWeeks(new SetDayOfWeekConverter().convertToDatabaseColumn(entity.getAbleDayOfWeeks()))
                                         .participleTimes(new SetParticipleTimeConverter().convertToDatabaseColumn(entity.getParticipleTimes()))
                                         .eventId(entity.getEvent().getId())
                                         .build();
}
}