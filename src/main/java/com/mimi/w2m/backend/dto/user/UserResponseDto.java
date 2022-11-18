package com.mimi.w2m.backend.dto.user;

import com.mimi.w2m.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * UserResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Data
@Builder
@Schema
public class UserResponseDto implements Serializable {

private final Long userId;

private final String name;
private final String email;

private UserResponseDto(Long userId, String name, String email) {
    this.userId = userId;
    this.name   = name;
    this.email  = email;
}

public static UserResponseDto of(User entity) {
    return UserResponseDto.builder()
                          .userId(entity.getId())
                          .name(entity.getName())
                          .email(entity.getEmail())
                          .build();
}
}