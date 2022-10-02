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

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    @Query("SELECT e FROM Event e WHERE e.user = :user")
    List<Event> findEventList(@Param("user") User user);
    @Query("SELECT p FROM ParticipableTime p WHERE p.user = :user")
    List<ParticipableTime> findParticipableTimeList(@Param("user") User user);
}