package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.domain.Event;
import com.mimi.w2m.backend.type.domain.Guest;
import com.mimi.w2m.backend.type.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.awt.*;
import java.time.DayOfWeek;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * GuestRepositoryTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GuestRepositoryTest {
@Autowired private GuestRepository guestRepository;
@Autowired private UserRepository  userRepository;
@Autowired private EventRepository       eventRepository;

@BeforeEach
void setUp() {
    final var user = User
                             .builder()
                             .name("tester")
                             .email("tester@meezle.org")
                             .build();
    userRepository.save(user);
    final var event = Event
                              .builder()
                              .title("event")
                              .dayOfWeeks(Set.of(DayOfWeek.values()))
                              .participleTime(ParticipleTime.of("00:00:00-11:11:11"))
                              .user(user)
                              .color(Color.RED)
                              .build();
    eventRepository.save(event);
    final var participant1 = Guest
                                     .builder()
                                     .name("participant1")
                                     .password("not-empty-password")
                                     .salt("salt")
                                     .event(event)
                                     .build();
    guestRepository.save(participant1);

    final var participant2 = Guest
                                     .builder()
                                     .name("participant2")
                                     .password("not-empty-password")
                                     .salt("salt")
                                     .event(event)
                                     .build();
    guestRepository.save(participant2);
}

@Test
void findByName() {
    //given
    final var expectedName = "participant1";

    //when
    final var givenParticipant = guestRepository.findByName(expectedName);

    //then
    assertThat(givenParticipant).isPresent();
    assertThat(givenParticipant.get().getName()).isEqualTo(expectedName);
}

@Test
void findAllByEvent() {
    //given
    final var event                = eventRepository.findByTitle("event").get(0);
    final var expectedParticipant1 = guestRepository.findByName("participant1").get();
    final var expectedParticipant2 = guestRepository.findByName("participant2").get();

    //when
    final var expectedParticipants = guestRepository.findAllByEvent(event);

    //then
    assertThat(expectedParticipants.size()).isEqualTo(2);
    assertThat(expectedParticipants).asList().contains(expectedParticipant1, expectedParticipant2);
}

@Test
void update() {
    //given
    final var updatedName         = "teddy";
    final var updatedPw           = "good-pw";
    final var updatedSalt         = "good-salt";
    final var expectedParticipant = guestRepository.findByName("participant1").get();

    //when
    expectedParticipant.updateName(updatedName);
    expectedParticipant.updatePassword(updatedPw, updatedSalt);
    final var givenParticipant = guestRepository.findByName(updatedName);

    //then
    assertThat(expectedParticipant).isEqualTo(givenParticipant.get());
}
}