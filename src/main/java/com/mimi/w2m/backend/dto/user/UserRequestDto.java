package com.mimi.w2m.backend.dto.user;

import com.mimi.w2m.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * UserRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
@Data
@Schema
public class UserRequestDto {
@Schema(description = "사용지 이름")
private final String name;
@Schema(description = "사용자 이메일")
private final String email;

public User to() {
    return User.builder()
               .name(name)
               .email(email)
               .build();
}
}