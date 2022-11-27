package com.mimi.w2m.backend.dto.security;

import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Schema
public class SessionInfoResponseDto implements Serializable {
private Long   id;
private String role;
private String name;
private String email;

@Builder
public SessionInfoResponseDto(Long id, String role, String name, String email) {
    this.id    = id;
    this.role  = role;
    this.name  = name;
    this.email = email;
}

protected SessionInfoResponseDto() {
}

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