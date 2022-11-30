package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.repository.EventParticipantRepository;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.domain.Event;
import com.mimi.w2m.backend.type.domain.EventParticipant;
import com.mimi.w2m.backend.type.domain.Guest;
import com.mimi.w2m.backend.type.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * EventParticipantServiceTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@ExtendWith(MockitoExtension.class)
class EventParticipantServiceTest {
private final        Logger                        logger = LogManager.getLogger(EventParticipantServiceTest.class);
@Mock private UserService     userService;
@Mock private GuestService    guestService;
@Mock private        EventRepository            eventRepository;
@Mock private        EventParticipantRepository eventParticipantRepository;
@InjectMocks private EventParticipantService    eventParticipantService;

//@DisplayName("EventParticipant 은 새롭게 입력할 때, 삭제하고, 다시 생성")
//@Test
//void createOrUpdate() {
//    //given
//    final var validUser = User
//                                  .builder()
//                                  .name("host")
//                                  .email("host@meezle.org")
//                                  .build();
//    final var validUserId   = 1L;
//    final var invalidUserId = 0L;
//
//    final var validEvent = Event
//                                   .builder()
//                                   .title("event")
//                                   .user(validUser)
//                                   .color(Color.RED)
//                                   .build();
//    final var validEventId   = 1L;
//    final var invalidEventId = 0L;
//
//    final var validParticipant = Guest
//                                         .builder()
//                                         .name("valid")
//                                         .event(validEvent)
//                                         .build();
//    final var validParticipantId   = 1L;
//    final var invalidParticipantId = 0L;
//
//    final var validAbleDayOfWeeksStr       = Strings.join(Arrays.asList(DayOfWeek.values())).with(",");
//    final var invalidAbleDayOfWeeksStrList = List.of("MonDay", "MONDAY, FRIDAY", "monDay|FRIDAY", "aa,bb");
//
//    final var validParticipleTimeStr = "00:00:00-01:01:01,02:02:02-03:03:03";
//    final var invalidParticipleTimeStrList = List.of("00:00:00-11:11:11, 22:22:22-23:23:23", "25:25:25-",
//                                                     "22:22:22-25:25:25");
//
//    final var validUserRequestDto = new EventParticipantRequestDto(validAbleDayOfWeeksStr, validParticipleTimeStr);
//    final var validParticipantRequestDto = new EventParticipantRequestDto(validAbleDayOfWeeksStr,
//                                                                             validParticipleTimeStr);
//    final var invalidRequestDtoList = new java.util.ArrayList<EventParticipantRequestDto>();
//    for(var invalidAbleDayOfWeeksStr :
//            invalidAbleDayOfWeeksStrList) {
//        for(var invalidParticipleTimeStr :
//                invalidParticipleTimeStrList) {
//            invalidRequestDtoList.add(new EventParticipantRequestDto(invalidAbleDayOfWeeksStr,
//                                                                        invalidParticipleTimeStr));
//        }
//    }
//    final var invalidRequestDtoInvalidEventId = new EventParticipantRequestDto(validAbleDayOfWeeksStr,
//                                                                                  validParticipleTimeStr);
//    final var invalidRequestDtoInvalidUserId = new EventParticipantRequestDto(validAbleDayOfWeeksStr,
//                                                                                 validParticipleTimeStr);
//    final var invalidRequestDtoInvalidParticipantId = new EventParticipantRequestDto(validAbleDayOfWeeksStr,
//                                                                                        validParticipleTimeStr);
//    given(eventRepository.findById(validEventId)).willReturn(Optional.of(validEvent));
//    given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());
//    given(userService.getUser(validUserId)).willReturn(validUser);
//    given(participantService.getGuest(validParticipantId)).willReturn(validParticipant);
//    given(eventParticipantRepository.save(any(EventParticipant.class))).willAnswer(invoc -> invoc.getArgument(0));
//
//    //when
//    final var expectedUserParticipleTime = eventParticipleTimeService.createOrUpdate(validEventId, validUserRequestDto, validUserId
//            , Role.USER);
//    final var expectedParticipantParticipleTime = eventParticipleTimeService.createOrUpdate(validUserId,
//            validParticipantRequestDto, validParticipantId, Role.PARTICIPANT);
////    for(var invalidRequestDto :
////            invalidRequestDtoList) {
////        assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(invalidRequestDto, Role.USER))
////                .isInstanceOf(InvalidValueException.class);
////    }
//    assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(invalidEventId, invalidRequestDtoInvalidEventId, validParticipantId,
//                                                                       Role.PARTICIPANT))
//            .isInstanceOf(EntityNotFoundException.class);
//    assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(validEventId, invalidRequestDtoInvalidUserId, invalidUserId, Role.USER))
//            .isInstanceOf(EntityNotFoundException.class);
//    assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(validEventId, invalidRequestDtoInvalidParticipantId, invalidParticipantId,
//                                                                       Role.PARTICIPANT))
//            .isInstanceOf(EntityNotFoundException.class);
//    assertThatThrownBy(() -> eventParticipleTimeService.createOrUpdate(validEventId, validUserRequestDto, validUserId, Role.NONE))
//            .isInstanceOf(InvalidValueException.class);
//
//    //then
//    assertThat(expectedUserParticipleTime.toString()).isEqualTo(validUserRequestDto.to(validEvent, validUser).toString());
//    assertThat(expectedParticipantParticipleTime.toString()).isEqualTo(validParticipantRequestDto.to(validEvent,
//                                                                                                     validParticipant).toString());
//
//    then(eventRepository).should(times(18)).findById(anyLong());
//    then(userService).should(times(13)).getUser(anyLong());
//    then(participantService).should(times(1)).getGuest(anyLong());
//    then(eventParticipantRepository).should(times(2)).save(any(EventParticipant.class));
//
//    logger.error(expectedUserParticipleTime);
//    logger.error(expectedParticipantParticipleTime);
//}

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

    final var participleTime1 = EventParticipant
                                        .builder()
                                        .user(user1)
                                        .participleTimes(Set.of(ParticipleTime.of("00:00:00-12:12:12")))
                                        .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                        .event(validEvent)
                                        .build();
    final var participleTime2 = EventParticipant
                                        .builder()
                                        .user(user2)
                                        .participleTimes(Set.of(ParticipleTime.of("00:00:00-12:12:12")))
                                        .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                        .event(validEvent)
                                        .build();
    given(eventRepository.findById(validEventId)).willReturn(Optional.of(validEvent));
    given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());
    given(eventParticipantRepository.findAllInEvent(validEvent)).willReturn(List.of(participleTime1,
                                                                                    participleTime2));

    //when
    final var expectedParticipleTimes = eventParticipantService.getAllParticipantInfo(validEventId);
    assertThatThrownBy(() -> eventParticipantService.getAllParticipantInfo(invalidEventId))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    assertThat(expectedParticipleTimes).asList().containsExactly(participleTime1, participleTime2);

    then(eventRepository).should(times(2)).findById(anyLong());
    then(eventParticipantRepository).should(times(1)).findAllInEvent(any(Event.class));
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
    final var participant = Guest
                                    .builder()
                                    .name("participant")
                                    .event(validEvent)
                                    .build();
    final var validParticipantId   = 1L;
    final var invalidParticipantId = 0L;
    final var participleTime1 = EventParticipant
                                        .builder()
                                        .user(user)
                                        .participleTimes(Set.of(ParticipleTime.of("00:00:00-12:12:12")))
                                        .ableDayOfWeeks(Set.of(DayOfWeek.values()))
                                        .event(validEvent)
                                        .build();
    final var participleTime2 = EventParticipant
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
    given(guestService.get(validParticipantId)).willReturn(participant);
    given(guestService.get(invalidParticipantId)).willThrow(EntityNotFoundException.class);
    given(eventParticipantRepository.findByGuestInEvent(user, validEvent)).willReturn(List.of(participleTime1));
    given(eventParticipantRepository.findByGuestInEvent(participant, validEvent)).willReturn(List.of(participleTime2));

    //when
    final var expectedParticipleTimesByUser = eventParticipantService.getAllParticipantInfo(validEventId,
                                                                                            validUserId,
                                                                                            Role.USER);
    final var expectedParticipleTimesByParticipant = eventParticipantService.getAllParticipantInfo(validEventId,
                                                                                                   validParticipantId, Role.GUEST);
    assertThatThrownBy(() -> eventParticipantService.getAllParticipantInfo(invalidEventId, validUserId, Role.USER))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> eventParticipantService.getAllParticipantInfo(validEventId, invalidUserId, Role.USER))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> eventParticipantService.getAllParticipantInfo(validEventId, invalidParticipantId,
                                                                           Role.GUEST))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> eventParticipantService.getAllParticipantInfo(validEventId, validUserId, Role.NONE))
            .isInstanceOf(InvalidValueException.class);

    //then
    assertThat(expectedParticipleTimesByUser).asList().containsExactly(participleTime1);
    assertThat(expectedParticipleTimesByParticipant).asList().containsExactly(participleTime2);

    then(eventRepository).should(times(6)).findById(anyLong());
    then(userService).should(times(2)).getUser(anyLong());
    then(guestService).should(times(2)).get(anyLong());
    then(eventParticipantRepository).should(times(1))
                                    .findByGuestInEvent(any(User.class), any(Event.class));
    then(eventParticipantRepository).should(times(1))
                                    .findByGuestInEvent(any(Guest.class), any(Event.class));
}
}