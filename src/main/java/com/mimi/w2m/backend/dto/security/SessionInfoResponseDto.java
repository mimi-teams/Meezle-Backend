package com.mimi.w2m.backend.dto.security;

import com.mimi.w2m.backend.common.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Getter
@Schema(title = "로그인한 이용자 정보", description = "로그인 성공 시, 얻을 수 있다", example = "{\"id\":0, \"role\":\"ROLE_USER\"}",
        requiredProperties = {"id", "role"})
public class SessionInfoResponseDto implements Serializable {
    @Schema(type = "Integer", description = "로그인한 이용자의 ID")
    @NotNull
    @PositiveOrZero
    private Long id;
    @NotNull
    private Role role;

    @Builder
    public SessionInfoResponseDto(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    protected SessionInfoResponseDto() {
    }

    public static SessionInfoResponseDto of(LoginInfo info) {
        return SessionInfoResponseDto.builder()
                .id(info.loginId())
                .role(info.role())
                .build();
    }
}