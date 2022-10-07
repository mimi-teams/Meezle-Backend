package com.mimi.w2m.backend.domain.event;

import com.mimi.w2m.backend.domain.eventAbleTime.AbleTime;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTime;
import com.mimi.w2m.backend.domain.eventParticipant.Participant;
import com.mimi.w2m.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author : teddy
 * @since : 2022/09/30
 */

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    @Query("SELECT e FROM Event e WHERE e.user = :user")
    List<Event> findAllByUser(@Param("user") User user);
}