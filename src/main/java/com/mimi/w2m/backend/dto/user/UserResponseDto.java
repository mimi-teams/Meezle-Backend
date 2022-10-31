package com.mimi.w2m.backend.dto.user;

import com.mimi.w2m.backend.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author : teddy
 * @since : 2022/10/09
 */

@Getter
@ToString
@NoArgsConstructor
public class UserResponseDto implements Serializable {
    private String name;
    private String email;
    public UserResponseDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}