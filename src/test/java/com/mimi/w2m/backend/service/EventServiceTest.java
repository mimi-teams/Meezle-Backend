//package com.mimi.w2m.backend.service;
//
//import com.mimi.w2m.backend.repository.EventRepository;
//import com.mimi.w2m.backend.type.common.ParticipleTime;
//import com.mimi.w2m.backend.type.domain.Event;
//import com.mimi.w2m.backend.type.domain.EventParticipant;
//import com.mimi.w2m.backend.type.domain.User;
//import com.mimi.w2m.backend.type.dto.event.ColorDto;
//import com.mimi.w2m.backend.type.dto.event.EventRequestDto;
//import com.mimi.w2m.backend.type.dto.participant.EventParticipantRequestDto;
//import com.mimi.w2m.backend.type.response.exception.EntityNotFoundException;
//import com.mimi.w2m.backend.type.response.exception.InvalidValueException;
//import org.apache.logging.log4j.LoggerFactory;
//import org.apache.logging.log4j.Logger;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.awt.*;
//import java.time.DayOfWeek;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.times;
//
///**
// * EventServiceTest
// *
// * @author teddy
// * @version 1.0.0
// * @since 2022/11/24
// **/
//@ExtendWith(MockitoExtension.class)
//class EventServiceTest {
//private final Logger                  logger = LoggerFactory.getLogger(EventServiceTest.class);
//@Mock private EventParticipantService eventParticipantService;
//@Mock private EventRepository         eventRepository;
//@Mock private        UserService                userService;
//@InjectMocks private EventService               eventService;
//
//@Test
//void createEvent() {
//    //given
//    final var user = User
//                             .builder()
//                             .name("user")
//                             .email("user@meezle.org")
//                             .build();
//    final var validUserId   = 1L;
//    final var invalidUserId = 0L;
//    final var requestDto = EventRequestDto
//                                        .builder()
//                                        .title("event")
//                                        .dDay(null)
//                                        .color(ColorDto.of(Color.RED))
//                                        .build();
//    given(userService.get(validUserId)).willReturn(user);
//    given(userService.get(invalidUserId)).willThrow(EntityNotFoundException.class);
//    given(eventRepository.save(any(Event.class))).willAnswer(invoc -> invoc.getArgument(0));
//
//    //when
//    final var expectedEvent = eventService.createEvent(validUserId, requestDto);
//    assertThatThrownBy(() -> eventService.createEvent(invalidUserId, requestDto))
//            .isInstanceOf(EntityNotFoundException.class);
//
//    //then
//    assertThat(expectedEvent.toString()).isEqualTo(requestDto.to(user).toString());
//
//    then(userService).should(times(2)).get(anyLong());
//    then(eventRepository).should(times(1)).save(any(Event.class));
//
//    logger.error(expectedEvent);
//}
//
//@DisplayName("dayOfWeeks 와 participleTime 을 제외한 나머지 정보 수정")
//@Test
//void modifyEvent() {
//    //given
//    final var user = User
//                             .builder()
//                             .name("user")
//                             .email("user@meezle.org")
//                             .build();
//    final var userId = 1L;
//    final var event = Event
//                              .builder()
//                              .title("event")
//                              .user(user)
//                              .color(Color.RED)
//                              .build();
//    final var validEventId   = 1L;
//    final var invalidEventId = 0L;
//    final var validRequestDto = EventRequestDto
//                                        .builder()
//                                        .title("updated")
//                                        .description("updated")
//                                        .dDay(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
//                                        .color(ColorDto.of(Color.BLACK))
//                                        .build();
//    given(eventRepository.findById(validEventId)).willReturn(Optional.of(event));
//    given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());
//
//    //when
//    final var expectedEvent = eventService.modifyEvent(validEventId, validRequestDto);
//    assertThatThrownBy(() -> eventService.modifyEvent(invalidEventId, validRequestDto))
//            .isInstanceOf(EntityNotFoundException.class);
//
//    //then
//    assertThat(expectedEvent.getTitle()).isEqualTo(validRequestDto.getTitle());
//    assertThat(expectedEvent.getDescription()).isEqualTo(validRequestDto.getDescription());
//    assertThat(expectedEvent.getColor()).isEqualTo(validRequestDto.getColor().to());
//    assertThat(expectedEvent.getDDay()).isEqualTo(validRequestDto.getDDay());
//
//    then(eventRepository).should(times(2)).findById(anyLong());
//}
//
//@DisplayName("EventParticipant 의 공통 부분 계산")
//@Test
//void calculateSharedTime() {
//    //given
//    final var user = User
//                             .builder()
//                             .name("user")
//                             .email("user@meezle.org")
//                             .build();
//    final var event = Event
//                              .builder()
//                              .title("event")
//                              .user(user)
//                              .color(Color.BLACK)
//                              .build();
//    final var eventId = 1L;
//    final var participleTime1 = EventParticipant
//                                        .builder()
//                                        .event(event)
//                                        .ableDayOfWeeks(Set.of(DayOfWeek.values()))
//                                        .participleTimes(Set.of(ParticipleTime.of("00:00:00-12:00:00")))
//                                        .build();
//    final var participleTime2 = EventParticipant
//                                        .builder()
//                                        .event(event)
//                                        .ableDayOfWeeks(Set.of(DayOfWeek.MONDAY, DayOfWeek.SUNDAY))
//                                        .participleTimes(Set.of(ParticipleTime.of("01:00:00-03:00:00")))
//                                        .build();
//    final var participleTime3 = EventParticipant
//                                        .builder()
//                                        .event(event)
//                                        .ableDayOfWeeks(Set.of(DayOfWeek.MONDAY))
//                                        .participleTimes(Set.of(ParticipleTime.of("02:00:00-04:00:00")))
//                                        .build();
//    final var validDayOfWeeks      = Set.of(DayOfWeek.MONDAY);
//    final var validParticipleTimes = ParticipleTime.of("02:00:00-03:00:00");
//
//    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
//    given(eventParticipantService.getAllParticipantInfo(eventId)).willReturn(List.of(participleTime1,
//                                                                                     participleTime2,
//                                                                                     participleTime3));
//    //when
//    final var expectedEvent = eventService.calculateSharedTime(eventId);
//
//    //then
//    assertThat(expectedEvent.getDayOfWeeks()).isEqualTo(validDayOfWeeks);
//    assertThat(expectedEvent.getParticipleTime()).isEqualTo(validParticipleTimes);
//
//    then(eventRepository).should(times(1)).findById(anyLong());
//    then(eventParticipantService).should(times(1)).getAllParticipantInfo(anyLong());
//}
//
//@DisplayName("DayOfWeek 와 ParticipleTime 을 직접 설정")
//@Test
//void setEventTimeDirectly() {
//    //given
//    final var user = User
//                             .builder()
//                             .name("user")
//                             .email("user@meezle.org")
//                             .build();
//    final var userId = 1L;
//    final var event = Event
//                              .builder()
//                              .title("event")
//                              .user(user)
//                              .color(Color.BLACK)
//                              .build();
//    final var eventId = 1L;
//    final var validRequestDto = new EventParticipantRequestDto("MONDAY,THURSDAY,", "00:00:00-11:11:11");
//    final var validDayOfWeeks = Set.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY);
//    final var validParticipleTime = ParticipleTime.of("00:00:00-11:11:11");
//    final var invalidRequestDtoByInvalidDayOfWeek = new EventParticipantRequestDto("Monday,", "00:00:00-11:11:11");
//    final var invalidRequestDtoByInvalidParticipleTimes1 = new EventParticipantRequestDto("MONDAY,", "11:11:11-00" +
//                                                                                                     ":00:00");
//    final var invalidRequestDtoByInvalidParticipleTimes2 = new EventParticipantRequestDto("MONDAY,",
//                                                                                             "00:00:00-01:00:00," +
//                                                                                             "01:00:00-02:00:00");
//    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
//    //when
//    final var expectedEvent = eventService.modifySelectedDaysAndTimesDirectly(eventId, validRequestDto);
//    assertThatThrownBy(() -> eventService.modifySelectedDaysAndTimesDirectly(eventId, invalidRequestDtoByInvalidDayOfWeek))
//            .isInstanceOf(InvalidValueException.class);
//    assertThatThrownBy(() -> eventService.modifySelectedDaysAndTimesDirectly(eventId, invalidRequestDtoByInvalidParticipleTimes1))
//            .isInstanceOf(InvalidValueException.class);
//    assertThatThrownBy(() -> eventService.modifySelectedDaysAndTimesDirectly(eventId, invalidRequestDtoByInvalidParticipleTimes2))
//            .isInstanceOf(InvalidValueException.class);
//
//    //then
//    assertThat(expectedEvent.getDayOfWeeks()).isEqualTo(validDayOfWeeks);
//    assertThat(expectedEvent.getParticipleTime()).isEqualTo(validParticipleTime);
//
//    then(eventRepository).should(times(4)).findById(anyLong());
//}
//
//@Test
//void deleteEvent() {
//    //given
//    final var user = User
//                             .builder()
//                             .name("user")
//                             .email("user@meezle.org")
//                             .build();
//    final var userId = 1L;
//    final var event = Event
//                              .builder()
//                              .title("event")
//                              .user(user)
//                              .color(Color.BLACK)
//                              .build();
//    final var eventId = 1L;
//    given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
//
//    //when
//    final var beforeDeleted = LocalDateTime.now();
//    final var expectedEvent = eventService.deleteNotReal(eventId);
//    final var afterDeleted = LocalDateTime.now();
//
//    //then
//    assertThat(expectedEvent.getDeletedAt()).isAfter(beforeDeleted).isBefore(afterDeleted);
//
//    then(eventRepository).should(times(1)).findById(anyLong());
//}
//
//@Test
//void getEventByTitleValid() {
//    //given
//    final var user = User
//                             .builder()
//                             .name("user")
//                             .email("user@meezle.org")
//                             .build();
//    final var event1 = Event
//                              .builder()
//                              .title("event")
//                              .user(user)
//                              .color(Color.BLACK)
//                              .build();
//    final var event2 = Event
//                               .builder()
//                               .title("event")
//                               .user(user)
//                               .color(Color.BLACK)
//                               .build();
//    final var event2Id = 1L;
//    given(eventRepository.findById(event2Id)).willReturn(Optional.of(event2));
//    given(eventRepository.findAllByTitle("event")).willReturn(List.of(event1, event2));
//
//    //when
//    eventService.deleteNotReal(event2Id);
//    final var expectedEvents = eventService.getAllByTitle("event");
//
//    //then
//    assertThat(expectedEvents).asList().containsExactly(event1);
//
//    then(eventRepository).should(times(1)).findById(anyLong());
//    then(eventRepository).should(times(1)).findAllByTitle(anyString());
//}
//
//@Test
//void getEventByTitleInvalid() {
//    //given
//    final var user = User
//                             .builder()
//                             .name("user")
//                             .email("user@meezle.org")
//                             .build();
//    final var event1 = Event
//                               .builder()
//                               .title("event")
//                               .user(user)
//                               .color(Color.BLACK)
//                               .build();
//    final var event1Id = 0L;
//    final var event2 = Event
//                               .builder()
//                               .title("event")
//                               .user(user)
//                               .color(Color.BLACK)
//                               .build();
//    final var event2Id = 1L;
//    given(eventRepository.findById(event1Id)).willReturn(Optional.of(event1));
//    given(eventRepository.findById(event2Id)).willReturn(Optional.of(event2));
//    given(eventRepository.findAllByTitle("event")).willReturn(List.of(event1, event2));
//
//    //when
//    eventService.deleteNotReal(event1Id);
//    eventService.deleteNotReal(event2Id);
//    assertThatThrownBy(() -> eventService.getAllByTitle("event"))
//            .isInstanceOf(EntityNotFoundException.class);
//
//    //then
//    then(eventRepository).should(times(2)).findById(anyLong());
//    then(eventRepository).should(times(1)).findAllByTitle(anyString());
//}
//
//@Test
//void getEventsCreatedByUser() {
//    //given
//    final var user = User
//                             .builder()
//                             .name("user")
//                             .email("user@meezle.org")
//                             .build();
//    final var invalidUser = User
//                                    .builder()
//                                    .name("invalidUser")
//                                    .email("invalidUser@meezle.org")
//                                    .build();
//    final var userId = 1L;
//    final var invalidUserId = 0L;
//    final var event1 = Event
//                               .builder()
//                               .title("event")
//                               .user(user)
//                               .color(Color.BLACK)
//                               .build();
//    final var event2 = Event
//                               .builder()
//                               .title("event")
//                               .user(user)
//                               .color(Color.BLACK)
//                               .build();
//    given(userService.get(userId)).willReturn(user);
//    given(userService.get(invalidUserId)).willReturn(invalidUser);
//    given(eventRepository.findAllByHost(user)).willReturn(List.of(event1, event2));
//    given(eventRepository.findAllByHost(invalidUser)).willReturn(List.of());
//
//    //when
//    final var expectedEvents = eventService.getAllByHost(userId);
//    assertThatThrownBy(() -> eventService.getAllByHost(invalidUserId))
//            .isInstanceOf(EntityNotFoundException.class);
//
//    //then
//    assertThat(expectedEvents).asList().containsExactly(event1, event2);
//
//    then(userService).should(times(2)).get(anyLong());
//    then(eventRepository).should(times(2)).findAllByHost(any(User.class));
//}
//}