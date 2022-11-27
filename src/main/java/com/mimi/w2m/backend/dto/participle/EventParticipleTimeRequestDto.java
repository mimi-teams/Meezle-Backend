package com.mimi.w2m.backend.dto.participle;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import com.mimi.w2m.backend.domain.converter.SetParticipleTimeConverter;
import com.mimi.w2m.backend.error.InvalidValueException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * EventParticipleTimeRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema
public class EventParticipleTimeRequestDto implements Serializable {
@Schema(description = "MONDAY,... 의 형식")
private String ableDayOfWeeks;
@Schema(description = "beginTime-endTime, ... 의 형식")
private String participleTimes;
@Schema(description = "연관된 event")
private Long   eventId;
@Schema(description = "User or Participant의 id")
private Long   ownerId;

@Builder
public EventParticipleTimeRequestDto(String ableDayOfWeeks, String participleTimes, Long eventId, Long ownerId) {
    this.ableDayOfWeeks  = ableDayOfWeeks;
    this.participleTimes = participleTimes;
    this.eventId         = eventId;
    this.ownerId         = ownerId;
}

protected EventParticipleTimeRequestDto() {
}

public EventParticipleTime to(Event event, User user) throws InvalidValueException {
    return EventParticipleTime.builder()
                              .event(event)
                              .user(user)
                              .ableDayOfWeeks(new SetDayOfWeekConverter().convertToEntityAttribute(ableDayOfWeeks))
                              .participleTimes(new SetParticipleTimeConverter().convertToEntityAttribute(participleTimes))
                              .build();
}

public EventParticipleTime to(Event event, Participant participant) throws InvalidValueException {
    return EventParticipleTime.builder()
                              .event(event)
                              .participant(participant)
                              .ableDayOfWeeks(new SetDayOfWeekConverter().convertToEntityAttribute(ableDayOfWeeks))
                              .participleTimes(new SetParticipleTimeConverter().convertToEntityAttribute(participleTimes))
                              .build();
}

}