//package com.mimi.w2m.backend.service;
//
//import com.mimi.w2m.backend.type.common.Role;
//import com.mimi.w2m.backend.type.domain.Event;
//import com.mimi.w2m.backend.type.domain.Guest;
//import com.mimi.w2m.backend.type.domain.User;
//import com.mimi.w2m.backend.type.dto.security.LoginInfo;
//import com.mimi.w2m.backend.type.response.exception.EntityNotFoundException;
//import com.mimi.w2m.backend.type.response.exception.IllegalAccessException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.servlet.http.HttpSession;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.times;
//
///**
// * AuthServiceTest
// *
// * @author teddy
// * @version 1.0.0
// * @since 2022/11/25
// **/
//@ExtendWith(MockitoExtension.class)
//class AuthServiceTest {
//@Mock private        HttpSession        httpSession;
//@Mock private        UserService        userService;
//@Mock private        EventService eventService;
//@Mock private        GuestService guestService;
//@InjectMocks private AuthService  authService;
//
//@Test
//void isCurrentLoginValidUser() {
//    //given
//    final var user = User
//                             .builder()
//                             .name("user")
//                             .email("user@meezle.org")
//                             .build();
//    final var userId    = 1L;
//    final var validId   = userId;
//    final var validRole = Role.USER;
//    final var validInfo = new LoginInfo(validId, validRole);
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(validInfo);
//    given(userService.get(userId)).willReturn(user);
//
//    //when
//    authService.isValidLogin(validId, validRole, httpSession);
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//    then(userService).should(times(1)).get(anyLong());
//}
//
//@Test
//void isCurrentLoginValidParticipant() {
//    //given
//    final var participant = Guest
//                                    .builder()
//                                    .name("user")
//                                    .build();
//    final var participantId = 1L;
//    final var validRole     = Role.GUEST;
//    final var validInfo     = new LoginInfo(participantId, validRole);
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(validInfo);
//    given(guestService.get(participantId)).willReturn(participant);
//
//    //when
//    authService.isValidLogin(participantId, validRole, httpSession);
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//    then(guestService).should(times(1)).get(anyLong());
//}
//
//@Test
//void isCurrentLoginInValidByMismatchingRole() {
//    //given
//    final var validId     = 1L;
//    final var validRole   = Role.GUEST;
//    final var invalidRole = Role.USER;
//    final var validInfo   = new LoginInfo(validId, validRole);
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(validInfo);
//
//    //when
//    assertThatThrownBy(() -> authService.isValidLogin(validId, invalidRole, httpSession))
//            .isInstanceOf(IllegalAccessException.class);
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//}
//
//@Test
//void isCurrentLoginInValidByMismatchingId() {
//    //given
//    final var validId   = 1L;
//    final var invalidId = 0L;
//    final var validRole = Role.GUEST;
//    final var validInfo = new LoginInfo(validId, validRole);
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(validInfo);
//
//    //when
//    assertThatThrownBy(() -> authService.isValidLogin(invalidId, validRole, httpSession))
//            .isInstanceOf(IllegalAccessException.class);
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//}
//
//@Test
//void isCurrentLoginInValidByNullInfo() {
//    //given
//    final var validId   = 1L;
//    final var validRole = Role.GUEST;
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(null);
//
//    //when
//    assertThatThrownBy(() -> authService.isValidLogin(validId, validRole, httpSession))
//            .isInstanceOf(IllegalAccessException.class);
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//}
//
//@Test
//void isCurrentLoginInValidByNotExistedUser() {
//    //given
//    final var validId   = 1L;
//    final var validRole = Role.USER;
//    final var validInfo = new LoginInfo(validId, validRole);
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(validInfo);
//    given(userService.get(validId)).willThrow(EntityNotFoundException.class);
//
//    //when
//    assertThatThrownBy(() -> authService.isValidLogin(validId, validRole, httpSession))
//            .isInstanceOf(EntityNotFoundException.class);
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//    then(userService).should(times(1)).get(anyLong());
//}
//
//@Test
//void isCurrentLoginInValidByNotExistedParticipant() {
//    //given
//    final var validId   = 1L;
//    final var validRole = Role.GUEST;
//    final var validInfo = new LoginInfo(validId, validRole);
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(validInfo);
//    given(guestService.get(validId)).willThrow(EntityNotFoundException.class);
//
//    //when
//    assertThatThrownBy(() -> authService.isValidLogin(validId, validRole, httpSession))
//            .isInstanceOf(EntityNotFoundException.class);
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//    then(guestService).should(times(1)).get(anyLong());
//}
//
//@Test
//void isCurrentLoginInValidByNoneRole() {
//    //given
//    final var validId   = 1L;
//    final var validRole = Role.NONE;
//    final var validInfo = new LoginInfo(validId, validRole);
//    given(httpSession.getAttribute(LoginInfo.key)).willReturn(validInfo);
//
//    //when
//    assertThatThrownBy(() -> authService.isValidLogin(validId, validRole, httpSession))
//            .isInstanceOf(IllegalAccessException.class);
//
//    //then
//    then(httpSession).should(times(1)).getAttribute(anyString());
//}
//
//@Test
//void isHostValid() {
//    //given
//    final var host = User
//                             .builder()
//                             .name("host")
//                             .email("host@meezle.org")
//                             .build();
//    final var hostId = 1L;
//    final var event = Event
//                              .builder()
//                              .title("event")
//                              .user(host)
//                              .build();
//    final var eventId = 1L;
//    given(eventService.get(eventId)).willReturn(event);
//    given(userService.get(hostId)).willReturn(host);
//
//    //when
//    authService.isHost(new LoginInfo(hostId, Role.USER), eventId);
//
//    //then
//    then(eventService).should(times(1)).get(anyLong());
//    then(userService).should(times(1)).get(anyLong());
//}
//
//@Test
//void isHostInValidByNotHost() {
//    //given
//    final var host = User
//                             .builder()
//                             .name("host")
//                             .email("host@meezle.org")
//                             .build();
//    final var event = Event
//                              .builder()
//                              .title("event")
//                              .user(host)
//                              .build();
//    final var eventId = 0L;
//    final var invalidUser = User
//                                    .builder()
//                                    .name("invalid")
//                                    .email("invalid@meezle.org")
//                                    .build();
//    final var invalidUserId = 0L;
//    given(eventService.get(eventId)).willReturn(event);
//    given(userService.get(invalidUserId)).willReturn(invalidUser);
//
//    //when
//    assertThatThrownBy(() -> authService.isHost(new LoginInfo(invalidUserId, Role.USER), eventId))
//            .isInstanceOf(IllegalAccessException.class);
//
//    //then
//    then(eventService).should(times(1)).get(anyLong());
//    then(userService).should(times(1)).get(anyLong());
//}
//}