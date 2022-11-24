package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.dto.event.ColorDto;
import com.mimi.w2m.backend.dto.event.EventRequestDto;
import com.mimi.w2m.backend.dto.participle.EventParticipleTimeRequestDto;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.repository.EventRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * EventServiceTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@ExtendWith(MockitoExtension.class)
class EventServiceTest {
private final        Logger                     logger = LogManager.getLogger(EventServiceTest.class);
@Mock private        EventParticipleTimeService eventParticipleTimeService;
@Mock private        EventRepository            eventRepository;
@Mock private        UserService                userService;
@InjectMocks private EventService               eventService;

@Test
void createEvent() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var validUserId   = 1L;
    final var invalidUserId = 0L;
    final var validRequestDto = EventRequestDto
                                        .builder()
                                        .title("valid")
                                        .userId(validUserId)
                                        .dDay(null)
                                        .color(ColorDto.of(Color.RED))
                                        .build();
    final var invalidRequestDtoByInvalidUserId = EventRequestDto
                                                         .builder()
                                                         .title("invalid")
                                                         .userId(invalidUserId)
                                                         .dDay(null)
                                                         .color(ColorDto.of(Color.RED))
                                                         .build();
    given(userService.getUser(validUserId)).willReturn(user);
    given(userService.getUser(invalidUserId)).willThrow(EntityNotFoundException.class);
    given(eventRepository.save(any(Event.class))).willAnswer(invoc -> invoc.getArgument(0));

    //when
    final var expectedEvent = eventService.createEvent(validRequestDto);
    assertThatThrownBy(() -> eventService.createEvent(invalidRequestDtoByInvalidUserId))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    assertThat(expectedEvent.toString()).isEqualTo(validRequestDto.to(user).toString());

    then(userService).should(times(2)).getUser(anyLong());
    then(eventRepository).should(times(1)).save(any(Event.class));

    logger.error(expectedEvent);
}

@Test
void modifyEvent() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var userId = 1L;
    final var event = Event
                              .builder()
                              .title("event")
                              .user(user)
                              .color(Color.RED)
                              .build();
    final var validEventId   = 1L;
    final var invalidEventId = 0L;
    final var validRequestDto = EventRequestDto
                                        .builder()
                                        .title("updated")
                                        .userId(userId)
                                        .description("updated")
                                        .dDay(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                                        .color(ColorDto.of(Color.BLACK))
                                        .build();
    given(eventRepository.findById(validEventId)).willReturn(Optional.of(event));
    given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());

    //when
    final var expectedEvent = eventService.modifyEvent(validEventId, validRequestDto);
    assertThatThrownBy(() -> eventService.modifyEvent(invalidEventId, validRequestDto))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    assertThat(expectedEvent.getTitle()).isEqualTo(validRequestDto.getTitle());
    assertThat(expectedEvent.getDescription()).isEqualTo(validRequestDto.getDescription());
    assertThat(expectedEvent.getColor()).isEqualTo(validRequestDto.getColor().to());
    assertThat(expectedEvent.getDDay()).isEqualTo(validRequestDto.getDDay());

    then(eventRepository).should(times(2)).findById(anyLong());
}

@Test
void calculateSharedTime() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var userId = 1L;
    final var event = Event
                              .builder()
                              .title("event")
                              .user(user)
                              .color(Color.BLACK)
                              .build();
    final var eventId = 1L;
    final var participleTime1 = EventParticipleTime
                                        .builder()
                                        .event(event)
                                        .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                        .participleTimes(Set.of(ParticipleTime.of("00:00:00-12:00:00")))
                                        .build();
    final var participleTime2 = EventParticipleTime
                                        .builder()
                                        .event(event)
                                        .ableDayOfWeeks(Set.of(DayOfWeek.MONDAY, DayOfWeek.SUNDAY))
                                        .participleTimes(Set.of(ParticipleTime.of("01:00:00-03:00:00")))
                                        .build();
    final var participleTime3 = EventParticipleTime
                                        .builder()
                                        .event(event)
                                        .ableDayOfWeeks(Set.of(DayOfWeek.MONDAY))
                                        .participleTimes(Set.of(ParticipleTime.of("02:00:00-04:00:00")))
                                        .build();
    final var validDayOfWeeks      = Set.of(DayOfWeek.MONDAY);
    final var validParticipleTimes = ParticipleTime.of("02:00:00-03:00:00");

    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
    given(eventParticipleTimeService.getEventParticipleTimes(eventId)).willReturn(List.of(participleTime1,
                                                                                          participleTime2,
                                                                                          participleTime3));
    //when
    final var expectedEvent = eventService.calculateSharedTime(eventId);

    //then
    assertThat(expectedEvent.getDayOfWeeks()).isEqualTo(validDayOfWeeks);
    assertThat(expectedEvent.getParticipleTime()).isEqualTo(validParticipleTimes);

    then(eventRepository).should(times(1)).findById(anyLong());
    then(eventParticipleTimeService).should(times(1)).getEventParticipleTimes(anyLong());
}

@Test
void setEventTimeDirectly() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var userId = 1L;
    final var event = Event
                              .builder()
                              .title("event")
                              .user(user)
                              .color(Color.BLACK)
                              .build();
    final var eventId = 1L;
    final var validRequestDto = new EventParticipleTimeRequestDto("MONDAY,THURSDAY,", "00:00:00-11:11:11", eventId,
                                                                  userId);
    final var validDayOfWeeks = Set.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY);
    final var validParticipleTime = ParticipleTime.of("00:00:00-11:11:11");
    final var invalidRequestDtoByInvalidDayOfWeek = new EventParticipleTimeRequestDto("Monday,", "00:00:00-11:11:11",
                                                                                      eventId, userId);
    final var invalidRequestDtoByInvalidParticipleTimes1 = new EventParticipleTimeRequestDto("MONDAY,", "11:11:11-00" +
                                                                                                        ":00:00", eventId, userId);
    final var invalidRequestDtoByInvalidParticipleTimes2 = new EventParticipleTimeRequestDto("MONDAY,",
                                                                                             "00:00:00-01:00:00," +
                                                                                             "01:00:00-02:00:00",
                                                                                             eventId, userId);
    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
    //when
    final var expectedEvent = eventService.setEventTimeDirectly(validRequestDto);
    assertThatThrownBy(() -> eventService.setEventTimeDirectly(invalidRequestDtoByInvalidDayOfWeek))
            .isInstanceOf(InvalidValueException.class);
    assertThatThrownBy(() -> eventService.setEventTimeDirectly(invalidRequestDtoByInvalidParticipleTimes1))
            .isInstanceOf(InvalidValueException.class);
    assertThatThrownBy(() -> eventService.setEventTimeDirectly(invalidRequestDtoByInvalidParticipleTimes2))
            .isInstanceOf(InvalidValueException.class);

    //then
    assertThat(expectedEvent.getDayOfWeeks()).isEqualTo(validDayOfWeeks);
    assertThat(expectedEvent.getParticipleTime()).isEqualTo(validParticipleTime);

    then(eventRepository).should(times(4)).findById(anyLong());
}

@Test
void deleteEvent() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var userId = 1L;
    final var event = Event
                              .builder()
                              .title("event")
                              .user(user)
                              .color(Color.BLACK)
                              .build();
    final var eventId = 1L;
    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));

    //when
    final var beforeDeleted = LocalDateTime.now();
    final var expectedEvent = eventService.deleteEvent(eventId);
    final var afterDeleted = LocalDateTime.now();

    //then
    assertThat(expectedEvent.getDeletedDate()).isAfter(beforeDeleted).isBefore(afterDeleted);

    then(eventRepository).should(times(1)).findById(anyLong());
}

@Test
void getEventByTitleValid() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var event1 = Event
                              .builder()
                              .title("event")
                              .user(user)
                              .color(Color.BLACK)
                              .build();
    final var event2 = Event
                               .builder()
                               .title("event")
                               .user(user)
                               .color(Color.BLACK)
                               .build();
    final var event2Id = 1L;
    given(eventRepository.findById(event2Id)).willReturn(Optional.of(event2));
    given(eventRepository.findByTitle("event")).willReturn(List.of(event1, event2));

    //when
    eventService.deleteEvent(event2Id);
    final var expectedEvents = eventService.getEventByTitle("event");

    //then
    assertThat(expectedEvents).asList().containsExactly(event1);

    then(eventRepository).should(times(1)).findById(anyLong());
    then(eventRepository).should(times(1)).findByTitle(anyString());
}

@Test
void getEventByTitleInvalid() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var event1 = Event
                               .builder()
                               .title("event")
                               .user(user)
                               .color(Color.BLACK)
                               .build();
    final var event1Id = 0L;
    final var event2 = Event
                               .builder()
                               .title("event")
                               .user(user)
                               .color(Color.BLACK)
                               .build();
    final var event2Id = 1L;
    given(eventRepository.findById(event1Id)).willReturn(Optional.of(event1));
    given(eventRepository.findById(event2Id)).willReturn(Optional.of(event2));
    given(eventRepository.findByTitle("event")).willReturn(List.of(event1, event2));

    //when
    eventService.deleteEvent(event1Id);
    eventService.deleteEvent(event2Id);
    assertThatThrownBy(() -> eventService.getEventByTitle("event"))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    then(eventRepository).should(times(2)).findById(anyLong());
    then(eventRepository).should(times(1)).findByTitle(anyString());
}

@Test
void getEventsCreatedByUser() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var invalidUser = User
                                    .builder()
                                    .name("invalidUser")
                                    .email("invalidUser@meezle.org")
                                    .build();
    final var userId = 1L;
    final var invalidUserId = 0L;
    final var event1 = Event
                               .builder()
                               .title("event")
                               .user(user)
                               .color(Color.BLACK)
                               .build();
    final var event2 = Event
                               .builder()
                               .title("event")
                               .user(user)
                               .color(Color.BLACK)
                               .build();
    given(userService.getUser(userId)).willReturn(user);
    given(userService.getUser(invalidUserId)).willReturn(invalidUser);
    given(eventRepository.findAllByUser(user)).willReturn(List.of(event1, event2));
    given(eventRepository.findAllByUser(invalidUser)).willReturn(List.of());

    //when
    final var expectedEvents = eventService.getEventsCreatedByUser(userId);
    assertThatThrownBy(() -> eventService.getEventsCreatedByUser(invalidUserId))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    assertThat(expectedEvents).asList().containsExactly(event1, event2);

    then(userService).should(times(2)).getUser(anyLong());
    then(eventRepository).should(times(2)).findAllByUser(any(User.class));
}
}