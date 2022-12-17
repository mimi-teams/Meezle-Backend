package com.mimi.w2m.backend.dto.participant;

import com.mimi.w2m.backend.config.exception.InvalidValueException;
import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipant;
import com.mimi.w2m.backend.domain.Guest;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Set;

/**
 * EventParticipantRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema(title = "Event 참여 정보", description = "이벤트에 참여하는 경우")
public class EventParticipantRequest implements Serializable {

    @Schema(title = "참여자가 선택한 시간", description = "참여자가 선택한 시간 정보를 받음(null = 모든 선택 가능한 시간이 가능하다고 가정)")
    @Nullable
    @Valid
    private Set<ParticipleTime> ableDaysAndTimes;

    @SuppressWarnings("unused")
    protected EventParticipantRequest() {
    }

    @SuppressWarnings("unused")
    public EventParticipantRequest(@Nullable Set<ParticipleTime> ableDaysAndTimes) {
        this.ableDaysAndTimes = ableDaysAndTimes;
    }

    public EventParticipant to(Event event, User user) throws InvalidValueException {
        return EventParticipant.builder()
                .event(event)
                .user(user)
                .ableDaysAndTimes(ableDaysAndTimes)
                .build();
    }

    public EventParticipant to(Event event, Guest guest) throws InvalidValueException {
        return EventParticipant.builder()
                .event(event)
                .guest(guest)
                .ableDaysAndTimes(ableDaysAndTimes)
                .build();
    }

}