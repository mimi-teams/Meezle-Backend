package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * GuestRepository
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/

public interface GuestRepository extends JpaRepository<Guest, Long> {
    @Query("SELECT g FROM Guest g WHERE g.name = :name and g.event = :event")
    Optional<Guest> findByNameInEvent(
            @Param("name") String name,
            @Param("event") Event event
    );

    @Query("SELECT g FROM Guest g WHERE g.event = :event")
    List<Guest> findAllByEvent(@Param("event") Event event);
}
