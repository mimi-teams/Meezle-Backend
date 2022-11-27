package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.security.SessionInfo;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * AuthServiceTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/25
 **/
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
@Mock private        HttpSession        httpSession;
@Mock private        UserService        userService;
@Mock private        EventService       eventService;
@Mock private        ParticipantService participantService;
@InjectMocks private AuthService        authService;

@Test
void isCurrentLoginValidUser() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var userId    = 1L;
    final var validId   = userId;
    final var validRole = Role.USER;
    final var validInfo = new SessionInfo(validId, validRole);
    given(httpSession.getAttribute(SessionInfo.key)).willReturn(validInfo);
    given(userService.getUser(userId)).willReturn(user);

    //when
    authService.isCurrentLogin(validId, validRole, httpSession);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
    then(userService).should(times(1)).getUser(anyLong());
}

@Test
void isCurrentLoginValidParticipant() {
    //given
    final var participant = Participant
                                    .builder()
                                    .name("user")
                                    .build();
    final var participantId = 1L;
    final var validRole     = Role.PARTICIPANT;
    final var validInfo     = new SessionInfo(participantId, validRole);
    given(httpSession.getAttribute(SessionInfo.key)).willReturn(validInfo);
    given(participantService.getParticipant(participantId)).willReturn(participant);

    //when
    authService.isCurrentLogin(participantId, validRole, httpSession);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
    then(participantService).should(times(1)).getParticipant(anyLong());
}

@Test
void isCurrentLoginInValidByMismatchingRole() {
    //given
    final var validId     = 1L;
    final var validRole   = Role.PARTICIPANT;
    final var invalidRole = Role.USER;
    final var validInfo   = new SessionInfo(validId, validRole);
    given(httpSession.getAttribute(SessionInfo.key)).willReturn(validInfo);

    //when
    assertThatThrownBy(() -> authService.isCurrentLogin(validId, invalidRole, httpSession))
            .isInstanceOf(UnauthorizedException.class);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
}

@Test
void isCurrentLoginInValidByMismatchingId() {
    //given
    final var validId   = 1L;
    final var invalidId = 0L;
    final var validRole = Role.PARTICIPANT;
    final var validInfo = new SessionInfo(validId, validRole);
    given(httpSession.getAttribute(SessionInfo.key)).willReturn(validInfo);

    //when
    assertThatThrownBy(() -> authService.isCurrentLogin(invalidId, validRole, httpSession))
            .isInstanceOf(UnauthorizedException.class);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
}

@Test
void isCurrentLoginInValidByNullInfo() {
    //given
    final var validId   = 1L;
    final var validRole = Role.PARTICIPANT;
    given(httpSession.getAttribute(SessionInfo.key)).willReturn(null);

    //when
    assertThatThrownBy(() -> authService.isCurrentLogin(validId, validRole, httpSession))
            .isInstanceOf(UnauthorizedException.class);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
}

@Test
void isCurrentLoginInValidByNotExistedUser() {
    //given
    final var validId   = 1L;
    final var validRole = Role.USER;
    final var validInfo = new SessionInfo(validId, validRole);
    given(httpSession.getAttribute(SessionInfo.key)).willReturn(validInfo);
    given(userService.getUser(validId)).willThrow(EntityNotFoundException.class);

    //when
    assertThatThrownBy(() -> authService.isCurrentLogin(validId, validRole, httpSession))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
    then(userService).should(times(1)).getUser(anyLong());
}

@Test
void isCurrentLoginInValidByNotExistedParticipant() {
    //given
    final var validId   = 1L;
    final var validRole = Role.PARTICIPANT;
    final var validInfo = new SessionInfo(validId, validRole);
    given(httpSession.getAttribute(SessionInfo.key)).willReturn(validInfo);
    given(participantService.getParticipant(validId)).willThrow(EntityNotFoundException.class);

    //when
    assertThatThrownBy(() -> authService.isCurrentLogin(validId, validRole, httpSession))
            .isInstanceOf(EntityNotFoundException.class);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
    then(participantService).should(times(1)).getParticipant(anyLong());
}

@Test
void isCurrentLoginInValidByNoneRole() {
    //given
    final var validId   = 1L;
    final var validRole = Role.NONE;
    final var validInfo = new SessionInfo(validId, validRole);
    given(httpSession.getAttribute(SessionInfo.key)).willReturn(validInfo);

    //when
    assertThatThrownBy(() -> authService.isCurrentLogin(validId, validRole, httpSession))
            .isInstanceOf(UnauthorizedException.class);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
}

@Test
void isHostValid() {
    //given
    final var host = User
                             .builder()
                             .name("host")
                             .email("host@meezle.org")
                             .build();
    final var hostId = 1L;
    final var event = Event
                              .builder()
                              .title("event")
                              .user(host)
                              .build();
    final var eventId = 1L;
    given(eventService.getEvent(eventId)).willReturn(event);
    given(userService.getUser(hostId)).willReturn(host);

    //when
    authService.isHost(hostId, eventId);

    //then
    then(eventService).should(times(1)).getEvent(anyLong());
    then(userService).should(times(1)).getUser(anyLong());
}

@Test
void isHostInValidByNotHost() {
    //given
    final var host = User
                             .builder()
                             .name("host")
                             .email("host@meezle.org")
                             .build();
    final var event = Event
                              .builder()
                              .title("event")
                              .user(host)
                              .build();
    final var eventId = 0L;
    final var invalidUser = User
                                    .builder()
                                    .name("invalid")
                                    .email("invalid@meezle.org")
                                    .build();
    final var invalidUserId = 0L;
    given(eventService.getEvent(eventId)).willReturn(event);
    given(userService.getUser(invalidUserId)).willReturn(invalidUser);

    //when
    assertThatThrownBy(() -> authService.isHost(invalidUserId, eventId))
            .isInstanceOf(UnauthorizedException.class);

    //then
    then(eventService).should(times(1)).getEvent(anyLong());
    then(userService).should(times(1)).getUser(anyLong());
}
}