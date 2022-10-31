package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.AbleTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : teddy
 * @since : 2022/09/30
 */

public interface AbleTimeRepository extends JpaRepository<AbleTime, Long> {
    @Query("SELECT t FROM AbleTime t WHERE t.event = :event")
    List<AbleTime> findAllByEvent(@Param("event") Event event);
}
