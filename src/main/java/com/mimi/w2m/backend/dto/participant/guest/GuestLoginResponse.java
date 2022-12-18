package com.mimi.w2m.backend.dto.participant.guest;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(title = "게스트 로그인 응답")
public record GuestLoginResponse(
        @Schema(description = "게스트 이름", maxLength = 20) @Size(min = 1, max = 20) @NotNull String name,
        @Schema(description = "게스트 토큰") String token
) {
}
