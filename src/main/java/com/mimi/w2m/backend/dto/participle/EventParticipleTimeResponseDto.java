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
@Schema(description = "MONDAY,... 의 형식")
private String ableDayOfWeeks;
@Schema(description = "beginTime-endTime,... 의 형식")
private String participleTimes;

@Builder
public EventParticipleTimeResponseDto(String ableDayOfWeeks, String participleTimes) {
    this.ableDayOfWeeks        = ableDayOfWeeks;
    this.participleTimes       = participleTimes;
}

protected EventParticipleTimeResponseDto() {
}

public static EventParticipleTimeResponseDto of(EventParticipleTime entity) {
    return EventParticipleTimeResponseDto.builder()
                                         .ableDayOfWeeks(new SetDayOfWeekConverter().convertToDatabaseColumn(entity.getAbleDayOfWeeks()))
                                         .participleTimes(new SetParticipleTimeConverter().convertToDatabaseColumn(entity.getParticipleTimes()))
                                         .build();
}
}