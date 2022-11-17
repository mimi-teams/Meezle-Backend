package com.mimi.w2m.backend.dto.participle;

import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalTime;

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
private final Long      eventParticipleTimeId;
@Schema(description = "MONDAY,... 의 형식")
private final String    ableDayOfWeeks;
@DateTimeFormat(pattern = "HH:mm:ss")
private final LocalTime startTime;
@DateTimeFormat(pattern = "HH:mm:ss")
private final LocalTime endTime;
@Schema(description = "연관된 event")
private final Long      eventId;

public static EventParticipleTimeResponseDto of(EventParticipleTime entity) {
    return EventParticipleTimeResponseDto.builder()
                                         .eventParticipleTimeId(entity.getId())
                                         .ableDayOfWeeks(new SetDayOfWeekConverter().convertToDatabaseColumn(entity.getAbleDayOfWeeks()))
                                         .startTime(entity.getStartTime())
                                         .endTime(entity.getEndTime())
                                         .eventId(entity.getEvent().getId())
                                         .build();
}

}