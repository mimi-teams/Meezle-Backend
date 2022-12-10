package com.mimi.w2m.backend.dto.participant;

import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipant;
import com.mimi.w2m.backend.domain.Guest;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.config.exception.InvalidValueException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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
@Schema(title = "Event 참여자에 대한 요청 정보", description = "참여자 생성이나 업데이트에 필요한 정보를 받음",
        requiredProperties = {"ableDaysAndTimes", "eventId", "ownerId", "ownerType"})
public class EventParticipantRequestDto implements Serializable {
    @Schema(title = "Event 의 ID", type = "Integer")
    @NotNull
    @PositiveOrZero
    private Long eventId;

    @Schema(title = "참가자의 실제 ID", type = "Integer")
    @NotNull
    @PositiveOrZero
    private Long ownerId;

    @Schema(title = "참가자의 실제 유형", type = "String")
    @NotNull
    private Role ownerType;

    @Schema(title = "참여자가 선택한 시간", description = "참여자가 선택한 시간 정보를 받음(null = 모든 선택 가능한 시간이 가능하다고 가정)")
    @Nullable
    @Valid
    private Set<ParticipleTime> ableDaysAndTimes;

    protected EventParticipantRequestDto() {
    }

    @Builder
    public EventParticipantRequestDto(Long eventId, Long ownerId, Role ownerType,
                                      @Nullable Set<ParticipleTime> ableDaysAndTimes) {
        this.eventId = eventId;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
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