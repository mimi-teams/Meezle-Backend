package com.mimi.w2m.backend.type.dto.user;

import com.mimi.w2m.backend.type.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * UserResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema(title = "가입한 이용자에 대한 반환 정보", description = "Id, name, email 을 제공한다",
        requiredProperties = {"id", "name", "email"},
        example = "{\"id\":0,\"name\":\"user\", \"email\":\"user@meezle.xyz\"}")
public class UserResponseDto implements Serializable {

    @Schema(type = "Integer", description = "이용자의 Unique ID")
    @PositiveOrZero
    @NotNull
    private Long id;

    @Schema(type = "String", description = "사용지 이름", maxLength = 20)
    @Size(max = 20)
    @NotNull
    private String name;
    @Schema(type = "String", description = "사용자 이메일", format = "email")
    @Email
    @NotNull
    private String email;

    @Builder
    public UserResponseDto(Long id, String name, String email) {
        this.id    = id;
        this.name  = name;
        this.email = email;
    }

    protected UserResponseDto() {
    }

    public static UserResponseDto of(User entity) {
        return UserResponseDto.builder()
                              .id(entity.getId())
                              .name(entity.getName())
                              .email(entity.getEmail())
                              .build();
    }
}