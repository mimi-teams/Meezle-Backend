package com.mimi.w2m.backend.dto.participle;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

/**
 * EventParticipleTimeRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Data
@Schema
public class EventParticipleTimeRequestDto {
@Schema(description = "MONDAY,... 의 형식")
private final String    ableDayOfWeeks;
@DateTimeFormat(pattern = "HH:mm:ss")
private final LocalTime startTime;
@DateTimeFormat(pattern = "HH:mm:ss")
private final LocalTime endTime;
@Schema(description = "연관된 event")
private final Long      eventId;
@Schema(description = "User or Participant의 id")
private final Long      ownerId;

public EventParticipleTime to(Event event, User user) {
    return EventParticipleTime.builder()
                              .event(event)
                              .user(user)
                              .ableDayOfWeeks(new SetDayOfWeekConverter().convertToEntityAttribute(ableDayOfWeeks))
                              .startTime(startTime)
                              .endTime(endTime)
                              .build();
}

public EventParticipleTime to(Event event, Participant participant) {
    return EventParticipleTime.builder()
                              .event(event)
                              .participant(participant)
                              .ableDayOfWeeks(new SetDayOfWeekConverter().convertToEntityAttribute(ableDayOfWeeks))
                              .startTime(startTime)
                              .endTime(endTime)
                              .build();
}

}