package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipableTime;
import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : teddy
 * @since : 2022/09/30
 */

public interface EventParticipableTimeRepository extends JpaRepository<EventParticipableTime, Long> {
    @Query("SELECT p FROM EventParticipableTime p WHERE p.user = :user")
    List<EventParticipableTime> findAllByUser(@Param("user") User user);

    @Query("SELECT t FROM EventParticipableTime t WHERE t.event = :event")
    List<EventParticipableTime> findAllByEvent(@Param("event") Event event);

    @Query("SELECT t FROM EventParticipableTime t WHERE t.participant = :participant")
    List<EventParticipableTime> findAllByParticipant(@Param("participant") Participant participant);
}
