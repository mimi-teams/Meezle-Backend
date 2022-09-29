package com.mimi.w2m.backend.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : teddy
 * @since : 2022/09/30
 */

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
}