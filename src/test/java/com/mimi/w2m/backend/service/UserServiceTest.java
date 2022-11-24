package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.dto.security.OAuthAttributes;
import com.mimi.w2m.backend.dto.security.UserSession;
import com.mimi.w2m.backend.error.EntityDuplicatedException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * UserServiceTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
private final        Logger         logger = LogManager.getLogger(UserServiceTest.class);
@Mock private        HttpSession    httpSession;
@Mock private        UserRepository userRepository;
@InjectMocks private UserService    userService;

@BeforeEach
void setup() {
}

@Test
void mockito() {
    //given
    var user1 = User
                        .builder()
                        .name("tester")
                        .email("tester1@meezle.org")
                        .build();
    var user2 = User
                        .builder()
                        .name("tester")
                        .email("tester2@meezle.org")
                        .build();
    var user3 = User
                        .builder()
                        .name("anotherTester")
                        .email("tester3@meezle.org")
                        .build();
    given(userRepository.findByName(anyString())).willReturn(List.of());
    given(userRepository.findByName("tester")).willReturn(List.of(user1, user2));
    given(userRepository.findByName("anotherTester")).willReturn(List.of(user3));

    given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
    given(userRepository.findByEmail("tester1@meezle.org")).willReturn(Optional.of(user1));
    given(userRepository.findByEmail("tester2@meezle.org")).willReturn(Optional.of(user2));
    given(userRepository.findByEmail("tester3@meezle.org")).willReturn(Optional.of(user3));
    //when
    final var testers        = userRepository.findByName("tester");
    final var anotherTesters = userRepository.findByName("anotherTester");
    final var invalidTesters = userRepository.findByName("invalidTester");
    final var tester1        = userRepository.findByEmail("tester1@meezle.org");
    final var tester2        = userRepository.findByEmail("tester2@meezle.org");
    final var tester3        = userRepository.findByEmail("tester3@meezle.org");
    final var invalidTester  = userRepository.findByEmail("invalidTester@meezle.org");

    then(userRepository).should(times(3)).findByName(anyString());
    then(userRepository).should(times(4)).findByEmail(anyString());

    assertThat(testers.size()).isEqualTo(2);
    assertThat(anotherTesters.size()).isEqualTo(1);
    assertThat(invalidTesters.size()).isEqualTo(0);
    assertThat(tester1).isPresent();
    assertThat(tester2).isPresent();
    assertThat(tester3).isPresent();
    assertThat(invalidTester).isEmpty();
}

@Test
void signUpOrLoad() {
    //given
    final var signedAttributes = OAuthAttributes
                                         .builder()
                                         .name("signed")
                                         .email("signed@meezle.org")
                                         .build();

    final var unsignedAttributes = OAuthAttributes
                                           .builder()
                                           .name("unsigned")
                                           .email("unsigned@meezle.org")
                                           .build();
    given(userRepository.findByEmail(signedAttributes.getEmail())).willReturn(Optional.of(signedAttributes.toEntity()));
    given(userRepository.findByEmail(unsignedAttributes.getEmail())).willReturn(Optional.empty());
    given(userRepository.save(any(User.class))).willReturn(unsignedAttributes.toEntity());

    //when
    final var expectedLoad   = userService.signUpOrLoad(signedAttributes);
    final var expectedSignUp = userService.signUpOrLoad(unsignedAttributes);

    //then
    then(userRepository).should(times(2)).findByEmail(anyString());
    then(userRepository).should(times(1)).save(any(User.class));

    assertThat(expectedLoad.toString()).isEqualTo(signedAttributes.toEntity().toString());
    assertThat(expectedSignUp.toString()).isEqualTo(unsignedAttributes.toEntity().toString());
}

@Test
void getCurrentUserValid() {
    //given
    final var loginUser = User
                                  .builder()
                                  .name("logined")
                                  .email("logined@meezle.org")
                                  .build();
    final var userSession = new UserSession(loginUser);

    given(httpSession.getAttribute("user")).willReturn(userSession);
    given(userRepository.findById(loginUser.getId())).willReturn(Optional.of(loginUser));

    //when
    final var expectedUser = userService.getCurrentUser();

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
    then(userRepository).should(times(1)).findById(any());

    assertThat(expectedUser.toString()).isEqualTo(loginUser.toString());
}

@Test
void getCurrentUserNotLogin() {
    //given
    given(httpSession.getAttribute("user")).willReturn(null);

    //when
    assertThatThrownBy(() -> userService.getCurrentUser()).isInstanceOf(UnauthorizedException.class);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
}

@Test
void getCurrentUserLoginButNotExist() {
    //given
    final var invalidUser = User
                                    .builder()
                                    .name("invalid")
                                    .email("invalid@meezle.org")
                                    .build();
    final var userSession = new UserSession(invalidUser);

    given(httpSession.getAttribute("user")).willReturn(userSession);
    given(userRepository.findById(invalidUser.getId())).willReturn(Optional.empty());

    //when
    assertThatThrownBy(() -> userService.getCurrentUser()).isInstanceOf(UnauthorizedException.class);

    //then
    then(httpSession).should(times(1)).getAttribute(anyString());
    then(userRepository).should(times(1)).findById(any());
}

@Test
void getUserValid() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

    //when
    final var expectedUser = userService.getUser(user.getId());

    //then
    assertThat(expectedUser.toString()).isEqualTo(user.toString());

    then(userRepository).should(times(1)).findById(any());
}

@Test
void getUserInValid() {
    //given
    given(userRepository.findById(anyLong())).willReturn(Optional.empty());

    //when
    assertThatThrownBy(() -> userService.getUser(0L)).isInstanceOf(EntityNotFoundException.class);

    //then
    then(userRepository).should(times(1)).findById(any());
}

@Test
void removeUser() {
    //given
    final var validUserId = 1L;
    final var validUser = User
                                  .builder()
                                  .name("user")
                                  .email("user@meezle.org")
                                  .build();
    final var invalidUserId = 2L;
    given(userRepository.findById(validUserId)).willReturn(Optional.of(validUser));
    given(userRepository.findById(invalidUserId)).willReturn(Optional.empty());

    //when
    userService.removeUser(validUserId);
    assertThatThrownBy(() -> userService.removeUser(invalidUserId)).isInstanceOf(EntityNotFoundException.class);

    //then
    then(userRepository).should(times(2)).findById(anyLong());
    then(userRepository).should(times(1)).delete(any(User.class));
}

@Test
void updateUser() {
    //given
    final var user = User
                             .builder()
                             .name("user")
                             .email("user@meezle.org")
                             .build();
    final var updatedName     = "updatedUser";
    final var updatedEmail    = "updatedUser@meezle.org";
    final var duplicatedEmail = "user@meezle.org";
    final var notExistUserId  = 0L;

    given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    given(userRepository.findById(notExistUserId)).willReturn(Optional.empty());
    given(userRepository.findByEmail(updatedEmail)).willReturn(Optional.empty());
    given(userRepository.findByEmail(duplicatedEmail)).willReturn(Optional.of(user));

    //when
    final var expectedUser = userService.updateUser(user.getId(), updatedName, updatedEmail);
    assertThatThrownBy(() -> userService.updateUser(notExistUserId, updatedName, updatedEmail)).isInstanceOf(EntityNotFoundException.class);
    assertThatThrownBy(() -> userService.updateUser(user.getId(), updatedName, duplicatedEmail)).isInstanceOf(EntityDuplicatedException.class);

    //then
    assertThat(expectedUser.toString()).isEqualTo(user.toString());

    then(userRepository).should(times(2)).findByEmail(anyString());
    then(userRepository).should(times(3)).findById(any());
}
}