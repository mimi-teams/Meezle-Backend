package com.mimi.w2m.backend.dto.participant;

import com.mimi.w2m.backend.domain.Participant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * ParticipantResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema
public class ParticipantResponseDto implements Serializable {
private Long   participantId;
private Long   eventId;
private String name;

@Builder
public ParticipantResponseDto(Long participantId, Long eventId, String name) {
    this.participantId = participantId;
    this.eventId       = eventId;
    this.name          = name;
}

protected ParticipantResponseDto() {
}

public static ParticipantResponseDto of(Participant entity) {
    return ParticipantResponseDto.builder()
                                 .participantId(entity.getId())
                                 .eventId(entity.getEvent().getId())
                                 .name(entity.getName())
                                 .build();
}

}