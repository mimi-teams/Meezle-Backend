package com.mimi.w2m.backend.domain.eventAbleTime;

import com.mimi.w2m.backend.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : teddy
 * @since : 2022/09/30
 */

public interface AbleTimeRepository extends JpaRepository<AbleTime, Long> {
}
