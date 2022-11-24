package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
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
 * EventParticipleTimeRepositoryTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventParticipleTimeRepositoryTest {
@Autowired private UserRepository                userRepository;
@Autowired private ParticipantRepository         participantRepository;
@Autowired private EventRepository               eventRepository;
@Autowired private EventParticipleTimeRepository eventParticipleTimeRepository;

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

    final var participant = Participant
                                    .builder()
                                    .name("participant")
                                    .event(event)
                                    .build();
    participantRepository.save(participant);

    final var eventParticipleTime1 = EventParticipleTime
                                             .builder()
                                             .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                             .participleTimes(Set.of(ParticipleTime.of("00:00:00-11:11:11")))
                                             .event(event)
                                             .user(user)
                                             .build();
    eventParticipleTimeRepository.save(eventParticipleTime1);

    final var eventParticipleTime2 = EventParticipleTime
                                             .builder()
                                             .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                             .participleTimes(Set.of(ParticipleTime.of("00:00:00-11:11:11")))
                                             .event(event)
                                             .participant(participant)
                                             .build();
    eventParticipleTimeRepository.save(eventParticipleTime2);

}

@Test
void find() {
    //given
    final var expectedEvent       = eventRepository.findByTitle("event").get(0);
    final var expectedUser        = userRepository.findByName("user").get(0);
    final var expectedParticipant = participantRepository.findByName("participant").get();

    final var expectedParticipleTime1 = eventParticipleTimeRepository.findAllByEntityAtEvent(expectedUser,
                                                                                             expectedEvent).get(0);
    final var expectedParticipleTime2 = eventParticipleTimeRepository.findAllByEntityAtEvent(expectedParticipant,
                                                                                             expectedEvent).get(0);

    //when
    final var givenParticipleTimes = eventParticipleTimeRepository.findAllByEvent(expectedEvent);

    //then
    assertThat(givenParticipleTimes).asList().containsExactly(expectedParticipleTime1, expectedParticipleTime2);

}

@Test
void update() {
    //given
    final var updatedDayOfWeeks      = Set.of(DayOfWeek.values());
    final var updatedParticipleTimes = Set.of(ParticipleTime.of("00:00:00-12:12:12"));

    final var expectedEvent = eventRepository.findByTitle("event").get(0);
    final var expectedUser  = userRepository.findByName("user").get(0);
    final var expectedParticipleTime = eventParticipleTimeRepository.findAllByEntityAtEvent(expectedUser,
                                                                                            expectedEvent).get(0);
    //when
    expectedParticipleTime.update(updatedDayOfWeeks, updatedParticipleTimes);

    //then
    assertThat(expectedParticipleTime.getAbleDayOfWeeks()).isEqualTo(updatedDayOfWeeks);
    assertThat(expectedParticipleTime.getParticipleTimes()).isEqualTo(updatedParticipleTimes);
}
}