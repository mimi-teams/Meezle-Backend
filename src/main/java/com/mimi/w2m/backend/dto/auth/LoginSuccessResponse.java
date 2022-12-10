package com.mimi.w2m.backend.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginSuccessResponse {

    private final String name;
    private final String token;

}
