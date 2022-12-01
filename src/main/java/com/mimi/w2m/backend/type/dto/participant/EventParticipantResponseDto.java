package com.mimi.w2m.backend.type.dto.participant;

import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.domain.EventParticipant;
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

    @Schema(title = "참여자가 선택한 시간", description = "참여자가 선택한 시간 정보를 받음(null = 모든 선택 가능한 시간이 가능하다고 가정)")
    @Nullable
    @Valid
    private Set<ParticipleTime> ableDaysAndTimes;

    @Builder
    public EventParticipantResponseDto(Long eventId,
                                       @Nullable Set<ParticipleTime> ableDaysAndTimes) {
        this.eventId          = eventId;
        this.ableDaysAndTimes = ableDaysAndTimes;
    }

    protected EventParticipantResponseDto() {
    }

    public static EventParticipantResponseDto of(EventParticipant entity) {
        return EventParticipantResponseDto.builder()
                                          .eventId(entity.getEvent()
                                                         .getId())
                                          .ableDaysAndTimes(entity.getAbleDaysAndTimes())
                                          .build();
    }
}