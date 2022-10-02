package com.mimi.w2m.backend.domain.eventParticipableTime;

import com.mimi.w2m.backend.domain.event.Event;
import com.mimi.w2m.backend.domain.eventParticipant.Participant;
import com.mimi.w2m.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : teddy
 * @since : 2022/09/30
 */

public interface ParticipableTimeRepository extends JpaRepository<ParticipableTime, Long> {
}
