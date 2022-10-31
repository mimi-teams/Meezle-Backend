package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventAbleTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : teddy
 * @since : 2022/09/30
 */

public interface EventAbleTimeRepository extends JpaRepository<EventAbleTime, Long> {
    @Query("SELECT t FROM EventAbleTime t WHERE t.event = :event")
    List<EventAbleTime> findAllByEvent(@Param("event") Event event);
}
