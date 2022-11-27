package com.mimi.w2m.backend.dto.user;

import com.mimi.w2m.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * UserRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
@Getter
@Schema
public class UserRequestDto implements Serializable {
@Schema(description = "사용지 이름")
private String name;
@Schema(description = "사용자 이메일")
private String email;

@Builder
public UserRequestDto(String name, String email) {
    this.name  = name;
    this.email = email;
}

protected UserRequestDto() {
}

public User to() {
    return User.builder()
               .name(name)
               .email(email)
               .build();
}
}