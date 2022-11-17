package com.mimi.w2m.backend.dto.participant;

import com.mimi.w2m.backend.domain.Participant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * ParticipantResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Data
@Builder
@Schema
public class ParticipantResponseDto implements Serializable {
private final Long   participantId;
private final Long   eventId;
private final String name;

public static ParticipantResponseDto of(Participant entity) {
    return ParticipantResponseDto.builder()
                                 .participantId(entity.getId())
                                 .eventId(entity.getEvent().getId())
                                 .name(entity.getName())
                                 .build();
}

}