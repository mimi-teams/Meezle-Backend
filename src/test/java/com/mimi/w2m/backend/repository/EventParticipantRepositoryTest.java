package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.domain.Event;
import com.mimi.w2m.backend.type.domain.EventParticipant;
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
 * EventParticipantRepositoryTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventParticipantRepositoryTest {
@Autowired private UserRepository  userRepository;
@Autowired private GuestRepository guestRepository;
@Autowired private EventRepository            eventRepository;
@Autowired private EventParticipantRepository eventParticipantRepository;

@BeforeEach
void setUp() {
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    userRepository.save(user);

    final var event = Event
                              .builder()
                              .title("event")
                              .user(user)
                              .color(Color.RED)
                              .build();
    eventRepository.save(event);

    final var participant = Guest
                                    .builder()
                                    .name("participant")
                                    .event(event)
                                    .build();
    guestRepository.save(participant);

    final var eventParticipleTime1 = EventParticipant
                                             .builder()
                                             .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                             .participleTimes(Set.of(ParticipleTime.of("00:00:00-11:11:11")))
                                             .event(event)
                                             .user(user)
                                             .build();
    eventParticipantRepository.save(eventParticipleTime1);

    final var eventParticipleTime2 = EventParticipant
                                             .builder()
                                             .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                             .participleTimes(Set.of(ParticipleTime.of("00:00:00-11:11:11")))
                                             .event(event)
                                             .participant(participant)
                                             .build();
    eventParticipantRepository.save(eventParticipleTime2);

}

@Test
void find() {
    //given
    final var expectedEvent       = eventRepository.findAllByTitle("event").get(0);
    final var expectedUser        = userRepository.findByName("user").get(0);
    final var expectedParticipant = guestRepository.findByName("participant").get();

    final var expectedParticipleTime1 = eventParticipantRepository.findByGuestInEvent(expectedUser,
                                                                                      expectedEvent).get(0);
    final var expectedParticipleTime2 = eventParticipantRepository.findByGuestInEvent(expectedParticipant,
                                                                                      expectedEvent).get(0);

    //when
    final var givenParticipleTimes = eventParticipantRepository.findAllInEvent(expectedEvent);

    //then
    assertThat(givenParticipleTimes).asList().containsExactly(expectedParticipleTime1, expectedParticipleTime2);

}

@Test
void update() {
    //given
    final var updatedDayOfWeeks      = Set.of(DayOfWeek.values());
    final var updatedParticipleTimes = Set.of(ParticipleTime.of("00:00:00-12:12:12"));

    final var expectedEvent = eventRepository.findAllByTitle("event").get(0);
    final var expectedUser  = userRepository.findByName("user").get(0);
    final var expectedParticipleTime = eventParticipantRepository.findByGuestInEvent(expectedUser,
                                                                                     expectedEvent).get(0);
    //when
    expectedParticipleTime.update(updatedDayOfWeeks, updatedParticipleTimes);

    //then
    assertThat(expectedParticipleTime.getAbleDayOfWeeks()).isEqualTo(updatedDayOfWeeks);
    assertThat(expectedParticipleTime.getAbleTimes()).isEqualTo(updatedParticipleTimes);
}
}