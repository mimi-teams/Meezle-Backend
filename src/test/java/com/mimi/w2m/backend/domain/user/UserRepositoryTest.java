package com.mimi.w2m.backend.domain.user;

import com.mimi.w2m.backend.domain.event.Event;
import com.mimi.w2m.backend.domain.event.EventRepository;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTime;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTimeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ParticipableTimeRepository participableTimeRepository;

    @Test
    void 사용자_생성하기(){
        //given
        var expectedName = "teddy";
        var expectedEmail = "teddy@super.com";

        //when
        userRepository.save(User.builder()
                .name(expectedName)
                .email(expectedEmail)
                .build());
        var user = userRepository.findAll().get(0);

        //then
        assertThat(user.getName()).isEqualTo(expectedName);
        assertThat(user.getEmail()).isEqualTo(expectedEmail);
    }

    @Test
    void 사용자_수정하기() {
        //given
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        var expectedName = "bear";
        var expectedEmail = "bear@super.com";
        userRepository.save(user);

        //when
        user.update(expectedName, expectedEmail);
        var expectedUser = userRepository.findAll().get(0);

        //then
        assertThat(expectedUser.getName()).isEqualTo(expectedName);
        assertThat(expectedUser.getEmail()).isEqualTo(expectedEmail);
    }

    @Test
    void 사용자_제거하기() {
        //given
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        userRepository.save(user);

        //when
        userRepository.delete(user);
        var userList = userRepository.findAll();

        //then
        assertThat(userList.isEmpty());
    }

    @Test
    void 사용자_가져오기() {
        //given
        var user1 = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        var user2 = User.builder()
                .name("bear")
                .email("bear@super.com")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        //when
        var expectedUser1 = userRepository.findByName("teddy");
        var expectedUser2 = userRepository.findByEmail("bear@super.com");

        //then
        assertThat(expectedUser1.get()).isEqualTo(user1);
        assertThat(expectedUser2.get()).isEqualTo(user2);
    }

    @Test
    void 사용자_이벤트_가져오기() {
        //given
        var user = userRepository.save(User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build());
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
        var eventList = userRepository.findEventList(user);

        //then
        assertThat(eventList.size()).isEqualTo(2);
        assertThat(eventList.containsAll(List.of(event1, event2)));
    }

    @Test
    void 사용자_이벤트_참여가능시간_가져오기() {
        //given
        var user = userRepository.save(User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build());
        var event = eventRepository.save(Event.builder()
                .name("teddyEvent")
                .user(user)
                .dDay(LocalDateTime.now())
                .build());
        var participableTime1 = participableTimeRepository.save(ParticipableTime.builder()
                .event(event)
                .user(user)
                .build());
        var participableTime2 = participableTimeRepository.save(ParticipableTime.builder()
                .event(event)
                .user(user)
                .build());

        //when
        var participableTimeList = userRepository.findParticipableTimeList(user);

        //then
        assertThat(participableTimeList.size()).isEqualTo(2);
        assertThat(participableTimeList.containsAll(List.of(participableTime1, participableTime2)));
    }
}