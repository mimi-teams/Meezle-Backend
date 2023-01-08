package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Oauth2Token;
import com.mimi.w2m.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Oauth2Repository
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/09
 **/
public interface Oauth2Repository extends JpaRepository<Oauth2Token, Long> {
    Optional<Oauth2Token> findByUser(User user);
}
