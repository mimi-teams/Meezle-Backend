package com.mimi.w2m.backend.dto.security;

import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@Schema
public class SessionInfoResponseDto implements Serializable {
private final Long   id;
private final String role;
private final String name;
private final String email;

public static SessionInfoResponseDto of(User entity) {
    return SessionInfoResponseDto
                   .builder()
                   .id(entity.getId())
                   .role(Role.USER.getKey())
                   .name(entity.getName())
                   .email(entity.getEmail())
                   .build();
}

public static SessionInfoResponseDto of(Participant entity) {
    return SessionInfoResponseDto
                   .builder()
                   .id(entity.getId())
                   .role(Role.PARTICIPANT.getKey())
                   .name(entity.getName())
                   .build();
}
}