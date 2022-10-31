package com.mimi.w2m.backend.dto.security;

import com.mimi.w2m.backend.domain.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Getter
public class UserSession implements Serializable{
    private Long userId;

    public UserSession(User user) {
        this.userId = user.getId();
    }
}