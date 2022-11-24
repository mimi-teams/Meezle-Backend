package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * EventRepositoryTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {
@Autowired private EventRepository eventRepository;
@Autowired private UserRepository  userRepository;

@BeforeEach
void setUp() {
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    userRepository.save(user);

    final var event1 = Event
                               .builder()
                               .title("event1")
                               .user(user)
                               .color(Color.RED)
                               .build();
    eventRepository.save(event1);
    final var event2 = Event
                               .builder()
                               .title("event2")
                               .dayOfWeeks(Set.of(DayOfWeek.values()))
                               .participleTime(ParticipleTime.of("00:00:00-11:11:11"))
                               .user(user)
                               .color(Color.RED)
                               .build();
    eventRepository.save(event2);
}

@Test
void findByTitle() {
    //given
    final var expectedTitle = "event1";

    //when
    final var givenEvent = eventRepository.findByTitle(expectedTitle);

    //then
    assertThat(givenEvent.size()).isEqualTo(1);
    assertThat(givenEvent.get(0).getTitle()).isEqualTo(expectedTitle);
}

@Test
void findAllByUser() {
    //given
    final var user           = userRepository.findByName("user").get(0);
    final var expectedEvent1 = eventRepository.findByTitle("event1").get(0);
    final var expectedEvent2 = eventRepository.findByTitle("event2").get(0);

    //when
    final var givenEvents = eventRepository.findAllByUser(user);

    //then
    assertThat(givenEvents).asList().containsExactly(expectedEvent1, expectedEvent2);
}

@Test
void update() {
    //given
    final var updatedTitle       = "updatedTitle";
    final var updatedDDay        = LocalDateTime.of(2000, 1, 1, 0, 0);
    final var updatedColor       = Color.BLUE;
    final var updatedDescription = "UpdatedDescription";

    final var updatedDayOfWeeks     = Set.of(DayOfWeek.values());
    final var updatedParticipleTime = ParticipleTime.of("00:00:00-11:11:11");

    final var expectedEvent = eventRepository.findByTitle("event1").get(0);

    //when
    expectedEvent.update(updatedTitle, updatedDescription, updatedColor, updatedDDay);
    expectedEvent.update(updatedDayOfWeeks, updatedParticipleTime);

    final var givenEvent = eventRepository.findByTitle("updatedTitle").get(0);

    //then
    assertThat(expectedEvent).isEqualTo(givenEvent);
}

@Test
void delete() {
    final var event         = eventRepository.findByTitle("event1").get(0);
    final var beforeDeleted = LocalDateTime.now();

    //when
    event.delete();
    final var afterDeleted = LocalDateTime.now();

    //then
    assertThat(event.getDeletedDate()).isAfter(beforeDeleted).isBefore(afterDeleted);

}
}