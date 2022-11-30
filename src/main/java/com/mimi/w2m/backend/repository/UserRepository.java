package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.type.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 *
 * Repository는 어떤 Entity를 반환하는지가 중요! UserRepository면 User Entity만 반환해야 한다.
 **/

public interface UserRepository extends JpaRepository<User, Long> {
@Query("SELECT u FROM User u WHERE u.name = :name")
List<User> findByName(
        @Param("name") String name);

Optional<User> findByEmail(String email);

}