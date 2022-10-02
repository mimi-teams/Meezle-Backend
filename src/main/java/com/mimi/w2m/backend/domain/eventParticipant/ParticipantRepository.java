package com.mimi.w2m.backend.domain.eventParticipant;

import com.mimi.w2m.backend.domain.event.Event;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTime;
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

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByName(String name);

    @Query("SELECT t FROM ParticipableTime t WHERE t.participant = :participant")
    List<ParticipableTime> findParticipableTimeList(@Param("participant") Participant participant);
}
