package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * EventParticipleTimeRepository
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/

public interface EventParticipleTimeRepository extends JpaRepository<EventParticipleTime, Long> {
@Query("SELECT t FROM EventParticipleTime t WHERE t.user = :user and t.event = :event")
List<EventParticipleTime> findAllByUserAtEvent(
        @Param("user") User user,
        @Param("event") Event event);

@Query("SELECT t FROM EventParticipleTime t WHERE t.event = :event")
List<EventParticipleTime> findAllByEvent(
        @Param("event") Event event);

@Query("SELECT t FROM EventParticipleTime t WHERE t.participant = :participant and t.event = :event")
List<EventParticipleTime> findAllByParticipantAtEvent(
        @Param("participant") Participant participant,
        @Param("event") Event event);
}
