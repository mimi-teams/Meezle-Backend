package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.ParticipantRepository;
import com.mimi.w2m.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ParticipantServiceTest {
    //    @Mock
    @Autowired
    private ParticipantRepository participantRepository;
    //    @InjectMocks
    @Autowired
    private ParticipantService participantService;

    //    @Mock
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        var expectedHost = User.builder()
                .name("bear")
                .email("bear@test.com")
                .role(Role.USER)
                .build();
        userRepository.save(expectedHost);

        var expectedEvent = Event.builder()
                .title("testEvent")
                .user(expectedHost)
                .dDay(LocalDateTime.now().plusDays(10))
                .build();
        eventRepository.save(expectedEvent);
    }

    @Test
    void 참여자_생성_테스트() {
        //given
        var expectedName = "teddy";
        var expectedPassword = "1123";
        var event = eventRepository.findByTitle("testEvent").orElseThrow();

        //when
        var participantId = participantService.createParticipant(event.getId(), expectedName, expectedPassword);
        var participant = participantRepository.findById(participantId);

        //then
        assertThat(participant.isPresent());
        assertThat(participant.get().getName()).isEqualTo(expectedName);
        assertThat(participant.get().getPassword()).isEqualTo(expectedPassword);
        assertThat(participant.get().getRole()).isEqualTo(Role.PARTICIPANT);
    }
}