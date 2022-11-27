package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.dto.participant.ParticipantRequestDto;
import com.mimi.w2m.backend.error.EntityDuplicatedException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.ParticipantRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * ParticipantServiceTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {
private final        Logger                logger = LogManager.getLogger(ParticipantServiceTest.class);
@Mock private        ParticipantRepository participantRepository;
@Mock private        EventRepository       eventRepository;
@Mock private        HttpSession           httpSession;
@InjectMocks private ParticipantService    participantService;

@Test
void createParticipantValid() {
    //given
    final var host = User
                             .builder()
                             .name("host")
                             .email("host@meezle.org")
                             .build();
    final var validEventId = 1L;
    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(host)
                                   .color(Color.RED)
                                   .build();

    final var validRequestDto1 = new ParticipantRequestDto("participant", null, validEventId);
    final var validRequestDto2 = new ParticipantRequestDto("participant2", "password", validEventId);
    given(eventRepository.findById(validEventId)).willReturn(Optional.of(validEvent));
    given(participantRepository.findByName(validRequestDto1.getName())).willReturn(Optional.empty());
    given(participantRepository.findByName(validRequestDto2.getName())).willReturn(Optional.empty());
    given(participantRepository.save(any(Participant.class))).willAnswer(invoc -> invoc.getArgument(0));
    //when
    final var expectedParticipant1 = participantService.createParticipant(validRequestDto1);
    final var expectedParticipant2 = participantService.createParticipant(validRequestDto2);
    //then
    assertThat(expectedParticipant1.getName()).isEqualTo(validRequestDto1.getName());
    assertThat(expectedParticipant2.getName()).isEqualTo(validRequestDto2.getName());

    then(eventRepository).should(times(2)).findById(anyLong());
    then(participantRepository).should(times(2)).findByName(anyString());
    then(participantRepository).should(times(2)).save(any(Participant.class));

    logger.error(expectedParticipant1.toString());
    logger.error(expectedParticipant2.toString());
}

@Test
void createParticipantInValid() {
    //given
    final var host = User
                             .builder()
                             .name("host")
                             .email("host@meezle.org")
                             .build();
    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(host)
                                   .color(Color.RED)
                                   .build();

    final var validEventId             = 1L;
    final var notExistEventId          = 0L;
    final var duplicatedNameRequestDto = new ParticipantRequestDto("duplicated", null, validEventId);
    final var notExistEventRequestDto  = new ParticipantRequestDto("invalid", null, notExistEventId);
    given(eventRepository.findById(notExistEventId)).willReturn(Optional.empty());
    given(participantRepository.findByName(duplicatedNameRequestDto.getName()))
            .willReturn(Optional.of(duplicatedNameRequestDto.to(validEvent, "salt", "password")));
    //when
    assertThatThrownBy(() -> participantService.createParticipant(duplicatedNameRequestDto))
            .isInstanceOf(EntityDuplicatedException.class);
    assertThatThrownBy(() -> participantService.createParticipant(notExistEventRequestDto))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    then(eventRepository).should(times(1)).findById(anyLong());
    then(participantRepository).should(times(2)).findByName(anyString());
}

@Test
void getAllParticipantInEvent() {
    //given
    final var host = User
                             .builder()
                             .name("host")
                             .email("host@meezle.org")
                             .build();
    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(host)
                                   .color(Color.RED)
                                   .build();
    final var validEventId   = 1L;
    final var invalidEventId = 0L;

    final var participant1 = Participant
                                     .builder()
                                     .name("participant1")
                                     .event(validEvent)
                                     .build();
    final var participant2 = Participant
                                     .builder()
                                     .name("participant2")
                                     .event(validEvent)
                                     .build();
    given(eventRepository.findById(validEventId)).willReturn(Optional.of(validEvent));
    given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());
    given(participantRepository.findAllByEvent(validEvent)).willReturn(List.of(participant1, participant2));

    //when
    final var expectedParticipants = participantService.getAllParticipantInEvent(validEventId);
    assertThatThrownBy(() -> participantService.getAllParticipantInEvent(invalidEventId))
            .isInstanceOf(EntityNotFoundException.class);
    //then
    assertThat(expectedParticipants).asList().containsExactly(participant1, participant2);

    then(eventRepository).should(times(2)).findById(anyLong());
    then(participantRepository).should(times(1)).findAllByEvent(any(Event.class));
}

@Test
void updateParticipantName() {
    //given
    final var host = User
                             .builder()
                             .name("host")
                             .email("host@meezle.org")
                             .build();
    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(host)
                                   .color(Color.RED)
                                   .build();
    final var validParticipant = Participant
                                         .builder()
                                         .name("valid")
                                         .event(validEvent)
                                         .build();
    final var duplicatedParticipant = Participant
                                              .builder()
                                              .name("duplicated")
                                              .event(validEvent)
                                              .build();
    final var validParticipantId   = 1L;
    final var invalidParticipantId = 0L;
    final var updatedName          = "updated";
    final var duplicatedName       = "duplicated";

    given(participantRepository.findById(validParticipantId)).willReturn(Optional.of(validParticipant));
    given(participantRepository.findById(invalidParticipantId)).willReturn(Optional.empty());
    given(participantRepository.findByName(updatedName)).willReturn(Optional.empty());
    given(participantRepository.findByName(duplicatedName)).willReturn(Optional.of(duplicatedParticipant));

    //when
    final var expectedParticipant = participantService.updateParticipantName(validParticipantId, updatedName);
    assertThatThrownBy(() -> participantService.updateParticipantName(validParticipantId, duplicatedName))
            .isInstanceOf(EntityDuplicatedException.class);
    assertThatThrownBy(() -> participantService.updateParticipantName(invalidParticipantId, updatedName))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    assertThat(expectedParticipant.getName()).isEqualTo(updatedName);

    then(participantRepository).should(times(3)).findById(anyLong());
    then(participantRepository).should(times(2)).findByName(anyString());
}

@Test
void updateParticipantPassword() {
    //given
    final var host = User
                             .builder()
                             .name("host")
                             .email("host@meezle.org")
                             .build();
    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(host)
                                   .color(Color.RED)
                                   .build();
    final var validParticipant = Participant
                                         .builder()
                                         .name("valid")
                                         .event(validEvent)
                                         .build();
    final var validParticipantId   = 1L;
    final var invalidParticipantId = 0L;
    final var password             = "updated";
    given(participantRepository.findById(validParticipantId)).willReturn(Optional.of(validParticipant));
    given(participantRepository.findById(invalidParticipantId)).willReturn(Optional.empty());

    //when
    final var expectedParticipant = participantService.updateParticipantPassword(validParticipantId, password);
    assertThatThrownBy(() -> participantService.updateParticipantPassword(invalidParticipantId, password))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    assertThat(expectedParticipant.getPassword()).isNotNull();
    assertThat(expectedParticipant.getSalt()).isNotNull();

    then(participantRepository).should(times(2)).findById(anyLong());
}

@Test
void removeParticipant() {
    //given
    final var host = User
                             .builder()
                             .name("host")
                             .email("host@meezle.org")
                             .build();
    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(host)
                                   .color(Color.RED)
                                   .build();
    final var validParticipant = Participant
                                         .builder()
                                         .name("valid")
                                         .event(validEvent)
                                         .build();
    final var validParticipantId   = 1L;
    final var invalidParticipantId = 0L;
    given(participantRepository.findById(validParticipantId)).willReturn(Optional.of(validParticipant));
    given(participantRepository.findById(invalidParticipantId)).willReturn(Optional.empty());

    //when
    participantService.removeParticipant(validParticipantId);
    assertThatThrownBy(() -> participantService.removeParticipant(invalidParticipantId))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    then(participantRepository).should(times(2)).findById(anyLong());
}

@Test
void login() {
    //given
    final var host = User
                             .builder()
                             .name("host")
                             .email("host@meezle.org")
                             .build();
    final var validEvent = Event
                                   .builder()
                                   .title("event")
                                   .user(host)
                                   .color(Color.RED)
                                   .build();
    final var validRequestDto       = new ParticipantRequestDto("valid", "valid", 0L);
    final var invalidNameRequestDto = new ParticipantRequestDto("invalid", null, 0L);
    final var invalidPwRequestDto   = new ParticipantRequestDto("valid", "invalid", 0L);

    // hashedPw를 만들기 위해, createParticipant 를 사용한다
    given(eventRepository.findById(any())).willReturn(Optional.of(validEvent));
    given(participantRepository.save(any(Participant.class))).willAnswer(invoc -> invoc.getArgument(0));
    final var validParticipant = participantService.createParticipant(validRequestDto);
    then(eventRepository).should(times(1)).findById(any());
    then(participantRepository).should(times(1)).save(any(Participant.class));
    // createdParticipant Done

    given(participantRepository.findByName(validRequestDto.getName())).willReturn(Optional.of(validParticipant));
    given(participantRepository.findByName(invalidNameRequestDto.getName())).willReturn(Optional.empty());

    //when
    final var expectedParticipant = participantService.login(validRequestDto);
    assertThatThrownBy(() -> participantService.login(invalidNameRequestDto))
            .isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> participantService.login(invalidPwRequestDto))
            .isInstanceOf(InvalidValueException.class);

    //then
    assertThat(expectedParticipant.toString()).isEqualTo(validParticipant.toString());

    then(participantRepository).should(times(4)).findByName(anyString()); // created + login
}

//@Test
//void logoutValid() {
//    //given
//    final var host = User
//                             .builder()
//                             .name("host")
//                             .email("host@meezle.org")
//                             .build();
//    final var validEvent = Event
//                                   .builder()
//                                   .title("event")
//                                   .user(host)
//                                   .color(Color.RED)
//                                   .build();
//    final var validParticipant = Participant
//                                         .builder()
//                                         .name("valid")
//                                         .event(validEvent)
//                                         .build();
//    final var info = new LoginInfo(validParticipant.getId(), Role.PARTICIPANT);
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(info);
//
//    //when
//    participantService.logout();
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//}
//
//@Test
//void logoutInValid() {
//    //given
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(null);
//
//    //when
//    assertThatThrownBy(() -> participantService.logout()).isInstanceOf(EntityNotFoundException.class);
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//}
}