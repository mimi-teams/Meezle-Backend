package com.mimi.w2m.backend.dto.participant;

import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.domain.EventParticipant;
import com.mimi.w2m.backend.dto.guest.GuestResponseDto;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import com.mimi.w2m.backend.config.exception.InvalidValueException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * EventParticipantResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema(title = "Event 참여자에 대한 반환 정보", description = "참여자에 대한 정보를 반환",
        requiredProperties = {"eventId", "ableDaysAndTimes"})
public class EventParticipantResponseDto implements Serializable {

    @Schema(title = "Event 의 ID", type = "Integer")
    @NotNull
    @PositiveOrZero
    private Long eventId;

    @Schema(title = "참여자의 정보(User)")
    @Valid
    @Nullable
    private UserResponseDto user;

    @Schema(title = "참여자의 정보(guest)")
    @Valid
    @Nullable
    private GuestResponseDto guest;

    @Schema(title = "참여자가 선택한 시간", description = "참여자가 선택한 시간 정보를 받음(null = 모든 선택 가능한 시간이 가능하다고 가정)")
    @Nullable
    @Valid
    private Set<ParticipleTime> ableDaysAndTimes;

    protected EventParticipantResponseDto() {
    }

    @Builder
    public EventParticipantResponseDto(Long eventId,
                                       @Nullable UserResponseDto user,
                                       @Nullable GuestResponseDto guest,
                                       @Nullable Set<ParticipleTime> ableDaysAndTimes) {
        this.eventId = eventId;
        this.user = user;
        this.guest = guest;
        this.ableDaysAndTimes = ableDaysAndTimes;
    }

    public static EventParticipantResponseDto of(EventParticipant entity) throws InvalidValueException {
        if (Objects.nonNull(entity.getUser())) {
            return EventParticipantResponseDto.builder()
                    .eventId(entity.getEvent()
                            .getId())
                    .user(UserResponseDto.of(entity.getUser()))
                    .ableDaysAndTimes(entity.getAbleDaysAndTimes())
                    .build();
        } else if (Objects.nonNull(entity.getGuest())) {
            return EventParticipantResponseDto.builder()
                    .eventId(entity.getEvent()
                            .getId())
                    .guest(GuestResponseDto.of(entity.getGuest()))
                    .ableDaysAndTimes(entity.getAbleDaysAndTimes())
                    .build();
        } else {
            throw new InvalidValueException("[EventParticipantResponseDto] Invalid Entity: id=" + entity.getId());
        }
    }
}