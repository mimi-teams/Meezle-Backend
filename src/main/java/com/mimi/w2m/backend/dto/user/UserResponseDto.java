package com.mimi.w2m.backend.dto.user;

import com.mimi.w2m.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * UserResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema
public class UserResponseDto implements Serializable {

private Long userId;

private String name;
private String email;

@Builder
public UserResponseDto(Long userId, String name, String email) {
    this.userId = userId;
    this.name   = name;
    this.email  = email;
}

protected UserResponseDto() {
}

public static UserResponseDto of(User entity) {
    return UserResponseDto.builder()
                          .userId(entity.getId())
                          .name(entity.getName())
                          .email(entity.getEmail())
                          .build();
}
}