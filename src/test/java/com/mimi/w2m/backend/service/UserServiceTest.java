package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
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
    private final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
    }

    @DisplayName("BDDMockito 사용법 테스트")
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
        final var testers = userRepository.findByName("tester");
        final var anotherTesters = userRepository.findByName("anotherTester");
        final var invalidTesters = userRepository.findByName("invalidTester");
        final var tester1 = userRepository.findByEmail("tester1@meezle.org");
        final var tester2 = userRepository.findByEmail("tester2@meezle.org");
        final var tester3 = userRepository.findByEmail("tester3@meezle.org");
        final var invalidTester = userRepository.findByEmail("invalidTester@meezle.org");

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
}