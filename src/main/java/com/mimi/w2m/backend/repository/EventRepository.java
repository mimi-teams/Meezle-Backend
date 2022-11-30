package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.type.domain.Event;
import com.mimi.w2m.backend.type.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * EventRepository
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/

public interface EventRepository extends JpaRepository<Event, Long> {
@Query("SELECT e FROM Event e WHERE e.title = :title")
List<Event> findByTitle(
        @Param("title") String title);

@Query("SELECT e FROM Event e WHERE e.host = :user")
List<Event> findAllByUser(
        @Param("user") User user);
}