package com.mimi.w2m.backend.domain.eventAbleTime;

import com.mimi.w2m.backend.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * @author : teddy
 * @since : 2022/09/30
 */

public interface AbleTimeRepository extends JpaRepository<AbleTime, Long> {
    @Query("SELECT t FROM AbleTime t WHERE t.event = :event")
    List<AbleTime> findAllByEvent(@Param("event") Event event);
}
