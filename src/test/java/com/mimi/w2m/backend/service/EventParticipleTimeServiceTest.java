package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.participle.EventParticipleTimeRequestDto;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.repository.EventParticipleTimeRepository;
import com.mimi.w2m.backend.repository.EventRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * EventParticipleTimeServiceTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@ExtendWith(MockitoExtension.class)
class EventParticipleTimeServiceTest {
private final        Logger                        logger = LogManager.getLogger(EventParticipleTimeServiceTest.class);
@Mock private        UserService                   userService;
@Mock private        ParticipantService            participantService;
@Mock private        EventRepository               eventRepository;
@Mock private        EventParticipleTimeRepository eventParticipleTimeRepository;
@InjectMocks private EventParticipleTimeService    eventParticipleTimeService;

@DisplayName("EventParticipleTime 은 새롭게 입력할 때, 삭제하고, 다시 생성")
@Test
void createOrUpdate() {
    //given
    final var validUser = User
                                  .builder()
                                  .name("host")
                                  .email("host@meezle.org")
                                  .build();
    final var validUserId   = 1L;
    final var invalidUserId = 0L;

    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(validUser)
                                   .color(Color.RED)
                                   .build();
    final var validEventId   = 1L;
    final var invalidEventId = 0L;

    final var validParticipant = Participant
                                         .builder()
                                         .name("valid")
                                         .event(validEvent)
                                         .build();
    final var validParticipantId   = 1L;
    final var invalidParticipantId = 0L;

    final var validAbleDayOfWeeksStr       = Strings.join(Arrays.asList(DayOfWeek.values())).with(",");
    final var invalidAbleDayOfWeeksStrList = List.of("MonDay", "MONDAY, FRIDAY", "monDay|FRIDAY", "aa,bb");

    final var validParticipleTimeStr = "00:00:00-01:01:01,02:02:02-03:03:03";
    final var invalidParticipleTimeStrList = List.of("00:00:00-11:11:11, 22:22:22-23:23:23", "25:25:25-",
                                                     "22:22:22-25:25:25");

    final var validUserRequestDto = new EventParticipleTimeRequestDto(validAbleDayOfWeeksStr, validParticipleTimeStr,
                                                                      validEventId, validUserId);
    final var validParticipantRequestDto = new EventParticipleTimeRequestDto(validAbleDayOfWeeksStr,
                                                                             validParticipleTimeStr, validEventId,
                                                                             validParticipantId);
    final var invalidRequestDtoList = new java.util.ArrayList<EventParticipleTimeRequestDto>();
    for(var invalidAbleDayOfWeeksStr :
            invalidAbleDayOfWeeksStrList) {
        for(var invalidParticipleTimeStr :
                invalidParticipleTimeStrList) {
            invalidRequestDtoList.add(new EventParticipleTimeRequestDto(invalidAbleDayOfWeeksStr,
                                                                        invalidParticipleTimeStr, validEventId,
                                                                        validUserId));
        }
    }
    final var invalidRequestDtoInvalidEventId = new EventParticipleTimeRequestDto(validAbleDayOfWeeksStr,
                                                                                  validParticipleTimeStr,
                                                                                  invalidEventId, validParticipantId);
    final var invalidRequestDtoInvalidUserId = new EventParticipleTimeRequestDto(validAbleDayOfWeeksStr,
                                                                                 validParticipleTimeStr,
                                                                                 invalidEventId, invalidUserId);
    final var invalidRequestDtoInvalidParticipantId = new EventParticipleTimeRequestDto(validAbleDayOfWeeksStr,
                                                                                        validParticipleTimeStr,
                                                                                        invalidEventId,
                                                                                        invalidParticipantId);
    given(eventRepository.findById(validEventId)).willReturn(Optional.of(validEvent));
    given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());
    given(userService.getUser(validUserId)).willReturn(validUser);
    given(participantService.getParticipant(validParticipantId)).willReturn(validParticipant);
    given(eventParticipleTimeRepository.save(any(EventParticipleTime.class))).willAnswer(invoc -> invoc.getArgument(0));

    //when
    final var expectedUserParticipleTime = eventParticipleTimeService.createOrUpdate(validUserRequestDto
            , Role.USER);
    final var expectedParticipantParticipleTime = eventParticipleTimeService.createOrUpdate(
            validParticipantRequestDto, Role.PARTICIPANT);
    for(var invalidRequestDto :
            invalidRequestDtoList) {
        assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(invalidRequestDto, Role.USER))
                .isInstanceOf(InvalidValueException.class);
    }
    assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(invalidRequestDtoInvalidEventId,
                                                                       Role.PARTICIPANT))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(invalidRequestDtoInvalidUserId, Role.USER))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(invalidRequestDtoInvalidParticipantId,
                                                                       Role.PARTICIPANT))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(validUserRequestDto, Role.NONE))
            .isInstanceOf(InvalidValueException.class);

    //then
    assertThat(expectedUserParticipleTime.toString()).isEqualTo(validUserRequestDto.to(validEvent, validUser).toString());
    assertThat(expectedParticipantParticipleTime.toString()).isEqualTo(validParticipantRequestDto.to(validEvent,
                                                                                                     validParticipant).toString());

    then(eventRepository).should(times(18)).findById(anyLong());
    then(userService).should(times(13)).getUser(anyLong());
    then(participantService).should(times(1)).getParticipant(anyLong());
    then(eventParticipleTimeRepository).should(times(2)).save(any(EventParticipleTime.class));

    logger.error(expectedUserParticipleTime);
    logger.error(expectedParticipantParticipleTime);
}

@Test
void getEventParticipleTimesByEventId() {
    final var user1 = User
                              .builder()
                              .name("user1")
                              .email("user1@meezle.org")
                              .build();
    final var user2 = User
                              .builder()
                              .name("user2")
                              .email("user2@meezle.org")
                              .build();
    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(user1)
                                   .color(Color.RED)
                                   .build();
    final var validEventId   = 1L;
    final var invalidEventId = 0L;

    final var participleTime1 = EventParticipleTime
                                        .builder()
                                        .user(user1)
                                        .participleTimes(Set.of(ParticipleTime.of("00:00:00-12:12:12")))
                                        .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                        .event(validEvent)
                                        .build();
    final var participleTime2 = EventParticipleTime
                                        .builder()
                                        .user(user2)
                                        .participleTimes(Set.of(ParticipleTime.of("00:00:00-12:12:12")))
                                        .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                        .event(validEvent)
                                        .build();
    given(eventRepository.findById(validEventId)).willReturn(Optional.of(validEvent));
    given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());
    given(eventParticipleTimeRepository.findAllByEvent(validEvent)).willReturn(List.of(participleTime1,
                                                                                       participleTime2));

    //when
    final var expectedParticipleTimes = eventParticipleTimeService.getEventParticipleTimes(validEventId);
    assertThatThrownBy(() -> eventParticipleTimeService.getEventParticipleTimes(invalidEventId))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    assertThat(expectedParticipleTimes).asList().containsExactly(participleTime1, participleTime2);

    then(eventRepository).should(times(2)).findById(anyLong());
    then(eventParticipleTimeRepository).should(times(1)).findAllByEvent(any(Event.class));
}

@Test
void getEventParticipleTimesByEventAndOwnerId() {
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var validUserId   = 1L;
    final var invalidUserId = 0L;
    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(user)
                                   .color(Color.RED)
                                   .build();
    final var validEventId   = 1L;
    final var invalidEventId = 0L;
    final var participant = Participant
                                    .builder()
                                    .name("participant")
                                    .event(validEvent)
                                    .build();
    final var validParticipantId   = 1L;
    final var invalidParticipantId = 0L;
    final var participleTime1 = EventParticipleTime
                                        .builder()
                                        .user(user)
                                        .participleTimes(Set.of(ParticipleTime.of("00:00:00-12:12:12")))
                                        .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                        .event(validEvent)
                                        .build();
    final var participleTime2 = EventParticipleTime
                                        .builder()
                                        .participant(participant)
                                        .participleTimes(Set.of(ParticipleTime.of("00:00:00-12:12:12")))
                                        .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                        .event(validEvent)
                                        .build();
    given(eventRepository.findById(validEventId)).willReturn(Optional.of(validEvent));
    given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());
    given(userService.getUser(validUserId)).willReturn(user);
    given(userService.getUser(invalidUserId)).willThrow(EntityNotFoundException.class);
    given(participantService.getParticipant(validParticipantId)).willReturn(participant);
    given(participantService.getParticipant(invalidParticipantId)).willThrow(EntityNotFoundException.class);
    given(eventParticipleTimeRepository.findAllByEntityAtEvent(user, validEvent)).willReturn(List.of(participleTime1));
    given(eventParticipleTimeRepository.findAllByEntityAtEvent(participant, validEvent)).willReturn(List.of(participleTime2));

    //when
    final var expectedParticipleTimesByUser = eventParticipleTimeService.getEventParticipleTimes(validEventId,
                                                                                                 validUserId,
                                                                                                 Role.USER);
    final var expectedParticipleTimesByParticipant = eventParticipleTimeService.getEventParticipleTimes(validEventId,
                                                                                                        validParticipantId, Role.PARTICIPANT);
    assertThatThrownBy(() -> eventParticipleTimeService.getEventParticipleTimes(invalidEventId, validUserId, Role.USER))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> eventParticipleTimeService.getEventParticipleTimes(validEventId, invalidUserId, Role.USER))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> eventParticipleTimeService.getEventParticipleTimes(validEventId, invalidParticipantId,
                                                                                Role.PARTICIPANT))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> eventParticipleTimeService.getEventParticipleTimes(validEventId, validUserId, Role.NONE))
            .isInstanceOf(InvalidValueException.class);

    //then
    assertThat(expectedParticipleTimesByUser).asList().containsExactly(participleTime1);
    assertThat(expectedParticipleTimesByParticipant).asList().containsExactly(participleTime2);

    then(eventRepository).should(times(6)).findById(anyLong());
    then(userService).should(times(2)).getUser(anyLong());
    then(participantService).should(times(2)).getParticipant(anyLong());
    then(eventParticipleTimeRepository).should(times(1))
                                       .findAllByEntityAtEvent(any(User.class), any(Event.class));
    then(eventParticipleTimeRepository).should(times(1))
                                       .findAllByEntityAtEvent(any(Participant.class), any(Event.class));
}
}