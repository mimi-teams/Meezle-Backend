package com.mimi.w2m.backend.domain.event;

import com.mimi.w2m.backend.domain.eventAbleTime.AbleTime;
import com.mimi.w2m.backend.domain.eventAbleTime.AbleTimeRepository;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTime;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTimeRepository;
import com.mimi.w2m.backend.domain.eventParticipant.Participant;
import com.mimi.w2m.backend.domain.eventParticipant.ParticipantRepository;
import com.mimi.w2m.backend.domain.user.User;
import com.mimi.w2m.backend.domain.user.UserRepository;
import net.bytebuddy.asm.Advice;
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
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private ParticipableTimeRepository participableTimeRepository;
    @Autowired
    private AbleTimeRepository ableTimeRepository;

    @Test
    void 이벤트_생성하기() {
        //given
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        var event = Event.builder()
                .name("teddyEvent")
                .user(user)
                .dDay(LocalDateTime.now())
                .build();

        //when
        userRepository.save(user);
        eventRepository.save(event);

        //then
        assertThat(eventRepository.findAll().get(0)).isEqualTo(event);
    }

    @Test
    void 이벤트_제거하기() {
        //given
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        userRepository.save(user);
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
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        userRepository.save(user);
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
        var user1 = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        userRepository.save(user1);
        var event1 = Event.builder()
                .name("teddyEvent")
                .user(user1)
                .dDay(LocalDateTime.now())
                .build();
        eventRepository.save(event1);

        var user2 = User.builder()
                .name("bear")
                .email("bear@super.com")
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
    void 이벤트_참여자_가져오기() {
        //given
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        userRepository.save(user);
        var event = Event.builder()
                .name("teddyEvent")
                .user(user)
                .dDay(LocalDateTime.now())
                .build();
        eventRepository.save(event);
        var participant1 = participantRepository.save(Participant.builder()
                .name("bear1")
                .event(event)
                .build());
        var participant2 = participantRepository.save(Participant.builder()
                .name("bear2")
                .event(event)
                .build());

        //when
        var participantList = eventRepository.findParticipantList(event);

        //then
        assertThat(participantList.size()).isEqualTo(2);
        assertThat(participantList.containsAll(List.of(participant1, participant2)));
    }

    @Test
    void 이벤트_가능한_시간_가져오기() {
        //given
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        userRepository.save(user);
        var event = Event.builder()
                .name("teddyEvent")
                .user(user)
                .dDay(LocalDateTime.now())
                .build();
        eventRepository.save(event);
        var ableTime1 = ableTimeRepository.save(AbleTime.builder()
                .event(event)
                .build());
        var ableTime2 = ableTimeRepository.save(AbleTime.builder()
                .event(event)
                .build());

        //when
        var ableTimeList = eventRepository.findAbleTimeList(event);

        //then
        assertThat(ableTimeList.size()).isEqualTo(2);
        assertThat(ableTimeList.containsAll(List.of(ableTime1, ableTime2)));
    }

    @Test
    void 이벤트_참여가능한_시간_가져오기() {
        //given
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        userRepository.save(user);
        var event = Event.builder()
                .name("teddyEvent")
                .user(user)
                .dDay(LocalDateTime.now())
                .build();
        eventRepository.save(event);
        var participableTime1 = participableTimeRepository.save(ParticipableTime.builder()
                .event(event)
                .build());
        var participableTime2 = participableTimeRepository.save(ParticipableTime.builder()
                .event(event)
                .build());

        //when
        var participableTimeList = eventRepository.findParticipableTimeList(event);

        //then
        assertThat(participableTimeList.size()).isEqualTo(2);
        assertThat(participableTimeList.containsAll(List.of(participableTime1, participableTime2)));
    }
}