package com.mimi.w2m.backend.dto.user;

import com.mimi.w2m.backend.domain.user.User;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author : teddy
 * @since : 2022/10/09
 */

@Getter
@ToString
public class UserResponseDto implements Serializable {
    private Long id;
    private String name;
    private String email;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
    public UserResponseDto() {
        this.id = 0L;
        this.name = "teddy";
        this.email = "teddy@super.com";
    }
}