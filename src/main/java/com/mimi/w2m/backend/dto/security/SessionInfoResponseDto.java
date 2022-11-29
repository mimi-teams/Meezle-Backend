package com.mimi.w2m.backend.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Schema(description = "로그인 성공 시 반환되는 값")
public class SessionInfoResponseDto implements Serializable {
@Schema
private Long   id;
@Schema
private String role;

@Builder
public SessionInfoResponseDto(Long id, String role) {
    this.id   = id;
    this.role = role;
}

protected SessionInfoResponseDto() {
}

public static SessionInfoResponseDto of(LoginInfo info) {
    return SessionInfoResponseDto.builder()
                                 .id(info.loginId())
                                 .role(info.role().getKey())
                                 .build();
}
}