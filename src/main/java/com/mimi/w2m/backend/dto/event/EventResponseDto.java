package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.domain.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;

/**
 * EventResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema(title = "Event 에 대한 반환 정보", description = "이벤트와 관련된 정보를 반환",
        requiredProperties = {"id", "hostId", "title", "dDay", "selectableParticipleTime", "selectedParticipleTime",
                "color", "description"})
public class EventResponseDto implements Serializable {

    private EventDto event;

    protected EventResponseDto() {
    }

    public static EventResponseDto of(Event entity) {
        final var responseDto = new EventResponseDto();
        responseDto.event = EventDto.builder()
                .id(entity.getId())
                .hostId(entity.getHost()
                        .getId())
                .title(entity.getTitle())
                .dDay(entity.getDDay())
                .selectableParticipleTimes(entity.getSelectableDaysAndTimes())
                .selectedParticipleTimes(entity.getSelectedDaysAndTimes())
                .color(ColorDto.of(entity.getColor()))
                .description(entity.getDescription())
                .build();

        return responseDto;
    }
}
