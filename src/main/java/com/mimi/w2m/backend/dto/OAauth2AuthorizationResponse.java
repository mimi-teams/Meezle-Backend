package com.mimi.w2m.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OAauth2AuthorizationResponse {

    @Schema(description = "이 URL로 프론트에서 리다이렉션 처리를 해줘야한다.")
    private final String authorizationUrl;
}
