package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.Participant;
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
    @Query("SELECT p FROM Participant p WHERE p.event = :event")
    List<Participant> findAllByEvent(@Param("event") Event event);
}
