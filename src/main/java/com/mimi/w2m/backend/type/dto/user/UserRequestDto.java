package com.mimi.w2m.backend.type.dto.user;

import com.mimi.w2m.backend.type.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * UserRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
@Getter
@Schema(title = "가입한 이용자의 요청 정보", description = "이용자의 이름과 이메일을 받음", requiredProperties = {"name", "email"},
        example = "{\"name\":\"user\", \"email\":\"user@meezle.xyz\"}")
public class UserRequestDto implements Serializable {
    @Schema(type = "String", description = "사용지 이름", maxLength = 20)
    @Size(min = 1, max = 20)
    @NotNull
    private String name;
    @Schema(type = "String", description = "사용자 이메일", pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @Email
    @NotNull
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