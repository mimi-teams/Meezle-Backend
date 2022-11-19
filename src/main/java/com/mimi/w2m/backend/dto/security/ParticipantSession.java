package com.mimi.w2m.backend.dto.security;

import com.mimi.w2m.backend.domain.Participant;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ParticipantSession implements Serializable {
private final Long participantId;

public ParticipantSession(Participant participant) {
    this.participantId = participant.getId();
}
}