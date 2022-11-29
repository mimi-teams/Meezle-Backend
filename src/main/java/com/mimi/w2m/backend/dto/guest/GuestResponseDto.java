package com.mimi.w2m.backend.dto.guest;

import com.mimi.w2m.backend.domain.Guest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * GuestResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema
public class GuestResponseDto implements Serializable {
private Long   participantId;
private Long   eventId;
private String name;

@Builder
public GuestResponseDto(Long participantId, Long eventId, String name) {
    this.participantId = participantId;
    this.eventId       = eventId;
    this.name          = name;
}

protected GuestResponseDto() {
}

public static GuestResponseDto of(Guest entity) {
    return GuestResponseDto.builder()
                           .participantId(entity.getId())
                           .eventId(entity.getEvent().getId())
                           .name(entity.getName())
                           .build();
}

}