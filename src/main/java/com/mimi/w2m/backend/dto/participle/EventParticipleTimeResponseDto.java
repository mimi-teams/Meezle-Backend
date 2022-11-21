package com.mimi.w2m.backend.dto.participle;

import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import com.mimi.w2m.backend.domain.converter.SetParticipleTimeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * EventParticipleTimeResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Data
@Builder
@Schema
public class EventParticipleTimeResponseDto implements Serializable {
private final Long   eventParticipleTimeId;
@Schema(description = "MONDAY,... 의 형식")
private final String ableDayOfWeeks;
@Schema(description = "beginTime-endTime,... 의 형식")
private final String participleTimes;
@Schema(description = "연관된 event")
private final Long   eventId;

public static EventParticipleTimeResponseDto of(EventParticipleTime entity) {
    return EventParticipleTimeResponseDto.builder()
                                         .eventParticipleTimeId(entity.getId())
                                         .ableDayOfWeeks(new SetDayOfWeekConverter().convertToDatabaseColumn(entity.getAbleDayOfWeeks()))
                                         .participleTimes(new SetParticipleTimeConverter().convertToDatabaseColumn(entity.getParticipleTimes()))
                                         .eventId(entity.getEvent().getId())
                                         .build();
}
}