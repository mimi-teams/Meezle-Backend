package com.mimi.w2m.backend.dto.security;

import com.mimi.w2m.backend.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Getter
@Setter
public class LoginSession implements Serializable {
    private String name;
    private String email;

    public LoginSession(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}