package com.mimi.w2m.backend.domain.user;

import com.mimi.w2m.backend.domain.event.Event;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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