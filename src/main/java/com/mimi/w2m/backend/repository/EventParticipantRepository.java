package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipant;
import com.mimi.w2m.backend.domain.Guest;
import com.mimi.w2m.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * EventParticipantRepository
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/

public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {
    @Query("SELECT t FROM EventParticipant t WHERE t.user = :user and t.event = :event")
    Optional<EventParticipant> findByUserInEvent(
            @Param("user") User user,
            @Param("event") Event event
    );

    @Query("SELECT t FROM EventParticipant t WHERE t.event = :event")
    List<EventParticipant> findAllInEvent(@Param("event") Event event);

    @Query("SELECT t FROM EventParticipant t WHERE t.guest = :guest and t.event = :event")
    Optional<EventParticipant> findByGuestInEvent(
            @Param("guest") Guest guest,
            @Param("event") Event event
    );


    @Query("SELECT t FROM EventParticipant t WHERE t.event = :event AND (t.user.id = :userId OR t.guest.id = :guestId)")
    Optional<EventParticipant> findByEventAndUserOrGuest(
            @Param("event") Event event,
            @Param("userId") UUID userId,
            @Param("guestId") UUID guestId
    );

    @Modifying
    @Query("DELETE FROM EventParticipant t WHERE t = :participant")
    void deleteByEventParticipant(@Param("participant") EventParticipant participant);

    void deleteByEvent(Event event);
}
