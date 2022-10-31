package com.mimi.w2m.backend.domain.event;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .role(Role.Tester)
                .build();
        userRepository.save(user);
    }
    @Test
    void 이벤트_생성하기() {
        //given
        var user = userRepository.findByName("teddy").get();
        var event = Event.builder()
                .name("teddyEvent")
                .user(user)
                .dDay(LocalDateTime.now())
                .build();

        //when
        eventRepository.save(event);

        //then
        assertThat(eventRepository.findAll().get(0)).isEqualTo(event);
    }
    @Test
    void 이벤트_제거하기() {
        //given
        var user = userRepository.findByName("teddy").get();
        var event = Event.builder()
                .name("teddyEvent")
                .user(user)
                .dDay(LocalDateTime.now())
                .build();
        eventRepository.save(event);

        //when
        eventRepository.delete(event);

        //then
        assertThat(eventRepository.findAll().isEmpty());
    }
    @Test
    void 이벤트_수정하기() {
        //given
        var user = userRepository.findByName("teddy").get();
        var event = Event.builder()
                .name("teddyEvent")
                .user(user)
                .dDay(LocalDateTime.now())
                .build();
        eventRepository.save(event);
        var expectedName = "bearEvent";
        var expectedDeletedDate = LocalDateTime.now();
        var expectedDday = LocalDateTime.of(1999, 3, 8, 0, 0);

        //when
        event.update(expectedName, expectedDeletedDate, expectedDday);
        var expectedEvent = eventRepository.findAll().get(0);

        //then
        assertThat(expectedEvent.getName()).isEqualTo(expectedName);
        assertThat(expectedEvent.getDeletedDate()).isEqualTo(expectedDeletedDate);
        assertThat(expectedEvent.getDDay()).isEqualTo(expectedDday);

    }
    @Test
    void 이벤트_가져오기() {
        //given
        var user1 = userRepository.findByName("teddy").get();
        var event1 = Event.builder()
                .name("teddyEvent")
                .user(user1)
                .dDay(LocalDateTime.now())
                .build();
        eventRepository.save(event1);

        var user2 = User.builder()
                .name("bear")
                .email("bear@super.com")
                .role(Role.Tester)
                .build();
        userRepository.save(user2);
        var event2 = Event.builder()
                .name("bearEvent")
                .user(user2)
                .dDay(LocalDateTime.now())
                .build();
        eventRepository.save(event2);

        //when
        var expectedEvent1 = eventRepository.findByName("teddyEvent");

        //then
        assertThat(expectedEvent1.get()).isEqualTo(event1);
    }
    @Test
    void 사용자_이벤트_가져오기() {
        //given
        var user = userRepository.findByName("teddy").get();
        var event1 = eventRepository.save(Event.builder()
                .name("teddyEvent1")
                .user(user)
                .dDay(LocalDateTime.now())
                .build());
        var event2 = eventRepository.save(Event.builder()
                .name("teddyEvent2")
                .user(user)
                .dDay(LocalDateTime.now())
                .build());

        //when
        var eventList = eventRepository.findAllByUser(user);

        //then
        assertThat(eventList.size()).isEqualTo(2);
        assertThat(eventList.containsAll(List.of(event1, event2)));
    }
}