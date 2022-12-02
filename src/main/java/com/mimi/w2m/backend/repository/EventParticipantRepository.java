package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.type.domain.Event;
import com.mimi.w2m.backend.type.domain.EventParticipant;
import com.mimi.w2m.backend.type.domain.Guest;
import com.mimi.w2m.backend.type.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * EventParticipantRepository
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    @Query("SELECT t FROM EventParticipant t WHERE t.user = :user and t.event = :event")
    Optional<EventParticipant> findByUserInEvent(
            @Param("user") User user,
            @Param("event") Event event);

    @Query("SELECT t FROM EventParticipant t WHERE t.event = :event")
    List<EventParticipant> findAllInEvent(
            @Param("event") Event event);

    @Query("SELECT t FROM EventParticipant t WHERE t.guest = :guest and t.event = :event")
    Optional<EventParticipant> findByGuestInEvent(
            @Param("guest") Guest guest,
            @Param("event") Event event);
}
