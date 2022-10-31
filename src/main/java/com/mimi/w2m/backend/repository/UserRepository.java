package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : teddy
 * @since : 2022/09/30
 */

/**
 * Repository는 어떤 Entity를 반환하는지가 중요!
 * UserRepository면 User Entity만 반환해야 한다.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);

}