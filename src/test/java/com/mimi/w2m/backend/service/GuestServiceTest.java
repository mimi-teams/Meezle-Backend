//package com.mimi.w2m.backend.service;
//
//import com.mimi.w2m.backend.repository.EventRepository;
//import com.mimi.w2m.backend.repository.GuestRepository;
//import com.mimi.w2m.backend.type.domain.Event;
//import com.mimi.w2m.backend.type.domain.Guest;
//import com.mimi.w2m.backend.type.domain.User;
//import com.mimi.w2m.backend.type.dto.guest.GuestRequestDto;
//import com.mimi.w2m.backend.type.response.exception.EntityDuplicatedException;
//import com.mimi.w2m.backend.type.response.exception.EntityNotFoundException;
//import com.mimi.w2m.backend.type.response.exception.InvalidValueException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.servlet.http.HttpSession;
//import java.awt.*;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.times;
//
///**
// * GuestServiceTest
// *
// * @author teddy
// * @version 1.0.0
// * @since 2022/11/24
// **/
//@ExtendWith(MockitoExtension.class)
//class GuestServiceTest {
//@Mock private GuestRepository guestRepository;
//@Mock private EventRepository eventRepository;
//@Mock private        HttpSession  httpSession;
//@InjectMocks private GuestService guestService;
//
//@Test
//void createParticipantValid() {
////    //given
////    final var host = User
////                             .builder()
////                             .name("host")
////                             .email("host@meezle.org")
////                             .build();
////    final var validEventId = 1L;
////    final var validEvent = Event
////                                   .builder()
////                                   .title("event")
////                                   .user(host)
////                                   .color(Color.RED)
////                                   .build();
////
////    final var validRequestDto1 = new GuestRequestDto("participant", null, validEventId);
////    final var validRequestDto2 = new GuestRequestDto("participant2", "password", validEventId);
////    given(eventRepository.findById(validEventId)).willReturn(Optional.of(validEvent));
////    given(guestRepository.findByName(validRequestDto1.getName())).willReturn(Optional.empty());
////    given(guestRepository.findByName(validRequestDto2.getName())).willReturn(Optional.empty());
////    given(guestRepository.save(any(Guest.class))).willAnswer(invoc -> invoc.getArgument(0));
////    //when
////    final var expectedParticipant1 = guestService.create(validRequestDto1);
////    final var expectedParticipant2 = guestService.create(validRequestDto2);
////    //then
////    assertThat(expectedParticipant1.getName()).isEqualTo(validRequestDto1.getName());
////    assertThat(expectedParticipant2.getName()).isEqualTo(validRequestDto2.getName());
////
////    then(eventRepository).should(times(2)).findById(anyLong());
////    then(guestRepository).should(times(2)).findByName(anyString());
////    then(guestRepository).should(times(2)).save(any(Guest.class));
////
////}
////
////@Test
////void createParticipantInValid() {
////    //given
////    final var host = User
////                             .builder()
////                             .name("host")
////                             .email("host@meezle.org")
////                             .build();
////    final var validEvent = Event
////                                   .builder()
////                                   .title("event")
////                                   .user(host)
////                                   .color(Color.RED)
////                                   .build();
////
////    final var validEventId             = 1L;
////    final var notExistEventId          = 0L;
////    final var duplicatedNameRequestDto = new GuestRequestDto("duplicated", null, validEventId);
////    final var notExistEventRequestDto  = new GuestRequestDto("invalid", null, notExistEventId);
////    given(eventRepository.findById(notExistEventId)).willReturn(Optional.empty());
////    given(guestRepository.findByName(duplicatedNameRequestDto.getName()))
////            .willReturn(Optional.of(duplicatedNameRequestDto.to(validEvent, "salt", "password")));
////    //when
////    assertThatThrownBy(() -> guestService.create(duplicatedNameRequestDto))
////            .isInstanceOf(EntityDuplicatedException.class);
////    assertThatThrownBy(() -> guestService.create(notExistEventRequestDto))
////            .isInstanceOf(EntityNotFoundException.class);
////
////    //then
////    then(eventRepository).should(times(1)).findById(anyLong());
////    then(guestRepository).should(times(2)).findByName(anyString());
////}
////
////@Test
////void getAllParticipantInEvent() {
////    //given
////    final var host = User
////                             .builder()
////                             .name("host")
////                             .email("host@meezle.org")
////                             .build();
////    final var validEvent = Event
////                                   .builder()
////                                   .title("event")
////                                   .user(host)
////                                   .color(Color.RED)
////                                   .build();
////    final var validEventId   = 1L;
////    final var invalidEventId = 0L;
////
////    final var participant1 = Guest
////                                     .builder()
////                                     .name("participant1")
////                                     .event(validEvent)
////                                     .build();
////    final var participant2 = Guest
////                                     .builder()
////                                     .name("participant2")
////                                     .event(validEvent)
////                                     .build();
////    given(eventRepository.findById(validEventId)).willReturn(Optional.of(validEvent));
////    given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());
////    given(guestRepository.findAllByEvent(validEvent)).willReturn(List.of(participant1, participant2));
////
////    //when
////    final var expectedParticipants = guestService.getAllInEvent(validEventId);
////    assertThatThrownBy(() -> guestService.getAllInEvent(invalidEventId))
////            .isInstanceOf(EntityNotFoundException.class);
////    //then
////    assertThat(expectedParticipants).asList().containsExactly(participant1, participant2);
////
////    then(eventRepository).should(times(2)).findById(anyLong());
////    then(guestRepository).should(times(1)).findAllByEvent(any(Event.class));
////}
////
////@Test
////void updateParticipantName() {
////    //given
////    final var host = User
////                             .builder()
////                             .name("host")
////                             .email("host@meezle.org")
////                             .build();
////    final var validEvent = Event
////                                   .builder()
////                                   .title("event")
////                                   .user(host)
////                                   .color(Color.RED)
////                                   .build();
////    final var validParticipant = Guest
////                                         .builder()
////                                         .name("valid")
////                                         .event(validEvent)
////                                         .build();
////    final var duplicatedParticipant = Guest
////                                              .builder()
////                                              .name("duplicated")
////                                              .event(validEvent)
////                                              .build();
////    final var validParticipantId   = 1L;
////    final var invalidParticipantId = 0L;
////    final var updatedName          = "updated";
////    final var duplicatedName       = "duplicated";
////
////    given(guestRepository.findById(validParticipantId)).willReturn(Optional.of(validParticipant));
////    given(guestRepository.findById(invalidParticipantId)).willReturn(Optional.empty());
////    given(guestRepository.findByName(updatedName)).willReturn(Optional.empty());
////    given(guestRepository.findByName(duplicatedName)).willReturn(Optional.of(duplicatedParticipant));
////
////    //when
////    final var expectedParticipant = guestService.updateName(validParticipantId, updatedName);
////    assertThatThrownBy(() -> guestService.updateName(validParticipantId, duplicatedName))
////            .isInstanceOf(EntityDuplicatedException.class);
////    assertThatThrownBy(() -> guestService.updateName(invalidParticipantId, updatedName))
////            .isInstanceOf(EntityNotFoundException.class);
////
////    //then
////    assertThat(expectedParticipant.getName()).isEqualTo(updatedName);
////
////    then(guestRepository).should(times(3)).findById(anyLong());
////    then(guestRepository).should(times(2)).findByName(anyString());
////}
////
////@Test
////void updateParticipantPassword() {
////    //given
////    final var host = User
////                             .builder()
////                             .name("host")
////                             .email("host@meezle.org")
////                             .build();
////    final var validEvent = Event
////                                   .builder()
////                                   .title("event")
////                                   .user(host)
////                                   .color(Color.RED)
////                                   .build();
////    final var validParticipant = Guest
////                                         .builder()
////                                         .name("valid")
////                                         .event(validEvent)
////                                         .build();
////    final var validParticipantId   = 1L;
////    final var invalidParticipantId = 0L;
////    final var password             = "updated";
////    given(guestRepository.findById(validParticipantId)).willReturn(Optional.of(validParticipant));
////    given(guestRepository.findById(invalidParticipantId)).willReturn(Optional.empty());
////
////    //when
////    final var expectedParticipant = guestService.updatePassword(validParticipantId, password);
////    assertThatThrownBy(() -> guestService.updatePassword(invalidParticipantId, password))
////            .isInstanceOf(EntityNotFoundException.class);
////
////    //then
////    assertThat(expectedParticipant.getPassword()).isNotNull();
////    assertThat(expectedParticipant.getSalt()).isNotNull();
////
////    then(guestRepository).should(times(2)).findById(anyLong());
////}
////
////@Test
////void removeParticipant() {
////    //given
////    final var host = User
////                             .builder()
////                             .name("host")
////                             .email("host@meezle.org")
////                             .build();
////    final var validEvent = Event
////                                   .builder()
////                                   .title("event")
////                                   .user(host)
////                                   .color(Color.RED)
////                                   .build();
////    final var validParticipant = Guest
////                                         .builder()
////                                         .name("valid")
////                                         .event(validEvent)
////                                         .build();
////    final var validParticipantId   = 1L;
////    final var invalidParticipantId = 0L;
////    given(guestRepository.findById(validParticipantId)).willReturn(Optional.of(validParticipant));
////    given(guestRepository.findById(invalidParticipantId)).willReturn(Optional.empty());
////
////    //when
////    guestService.delete(validParticipantId);
////    assertThatThrownBy(() -> guestService.delete(invalidParticipantId))
////            .isInstanceOf(EntityNotFoundException.class);
////
////    //then
////    then(guestRepository).should(times(2)).findById(anyLong());
////}
////
////@Test
////void login() {
////    //given
////    final var host = User
////                             .builder()
////                             .name("host")
////                             .email("host@meezle.org")
////                             .build();
////    final var validEvent = Event
////                                   .builder()
////                                   .title("event")
////                                   .user(host)
////                                   .color(Color.RED)
////                                   .build();
////    final var validRequestDto       = new GuestRequestDto("valid", "valid", 0L);
////    final var invalidNameRequestDto = new GuestRequestDto("invalid", null, 0L);
////    final var invalidPwRequestDto   = new GuestRequestDto("valid", "invalid", 0L);
////
////    // hashedPw를 만들기 위해, createParticipant 를 사용한다
////    given(eventRepository.findById(any())).willReturn(Optional.of(validEvent));
////    given(guestRepository.save(any(Guest.class))).willAnswer(invoc -> invoc.getArgument(0));
////    final var validParticipant = guestService.create(validRequestDto);
////    then(eventRepository).should(times(1)).findById(any());
////    then(guestRepository).should(times(1)).save(any(Guest.class));
////    // createdParticipant Done
////
////    given(guestRepository.findByName(validRequestDto.getName())).willReturn(Optional.of(validParticipant));
////    given(guestRepository.findByName(invalidNameRequestDto.getName())).willReturn(Optional.empty());
////
////    //when
////    final var expectedParticipant = guestService.login(validRequestDto);
////    assertThatThrownBy(() -> guestService.login(invalidNameRequestDto))
////            .isInstanceOf(EntityNotFoundException.class);
////    assertThatThrownBy(() -> guestService.login(invalidPwRequestDto))
////            .isInstanceOf(InvalidValueException.class);
////
////    //then
////    assertThat(expectedParticipant.toString()).isEqualTo(validParticipant.toString());
////
////    then(guestRepository).should(times(4)).findByName(anyString()); // created + login
////}
//
////@Test
////void logoutValid() {
////    //given
////    final var host = User
////                             .builder()
////                             .name("host")
////                             .email("host@meezle.org")
////                             .build();
////    final var validEvent = Event
////                                   .builder()
////                                   .title("event")
////                                   .user(host)
////                                   .color(Color.RED)
////                                   .build();
////    final var validParticipant = Guest
////                                         .builder()
////                                         .name("valid")
////                                         .event(validEvent)
////                                         .build();
////    final var info = new LoginInfo(validParticipant.getId(), Role.PARTICIPANT);
////    given(httpSession.getAttribute(LoginInfo.key)).willReturn(info);
////
////    //when
////    participantService.logout();
////
////    //then
////    then(httpSession).should(times(1)).getAttribute(anyString());
////}
////
////@Test
////void logoutInValid() {
////    //given
////    given(httpSession.getAttribute(LoginInfo.key)).willReturn(null);
////
////    //when
////    assertThatThrownBy(() -> participantService.logout()).isInstanceOf(EntityNotFoundException.class);
////
////    //then
////    then(httpSession).should(times(1)).getAttribute(anyString());
////}
//}