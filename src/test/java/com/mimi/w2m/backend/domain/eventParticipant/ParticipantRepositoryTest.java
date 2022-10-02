package com.mimi.w2m.backend.domain.eventParticipant;

import com.mimi.w2m.backend.domain.event.Event;
import com.mimi.w2m.backend.domain.event.EventRepository;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTime;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTimeRepository;
import com.mimi.w2m.backend.domain.user.User;
import com.mimi.w2m.backend.domain.user.UserRepository;
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
class ParticipantRepositoryTest {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParticipableTimeRepository participableTimeRepository;

    @BeforeEach
    void setup() {
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
    }

    @Test
    void 참여자_생성하기(){
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var participant = Participant.builder()
                .name("bear")
                .event(event)
                .build();

        //when
        participantRepository.save(participant);

        //then
        assertThat(participantRepository.findAll().get(0)).isEqualTo(participant);
    }

    @Test
    void 참여자_수정하기() {
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var participant = Participant.builder()
                .name("bear")
                .event(event)
                .build();
        participantRepository.save(participant);
        var expectedName = "rabbit";
        var expectedPassword = "0308";
//
//        //when
        participant.update(expectedName, expectedPassword);
        var expectedParticipant = participantRepository.findAll().get(0);

        //then
        assertThat(expectedParticipant.getName()).isEqualTo(expectedName);
        assertThat(expectedParticipant.getPassword()).isEqualTo(expectedPassword);
    }

    @Test
    void 참여자_제거하기() {
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var participant = Participant.builder()
                .name("bear")
                .event(event)
                .build();
        participantRepository.save(participant);

        //when
        participantRepository.delete(participant);

        //then
        assertThat(participantRepository.findAll().isEmpty());
    }

    @Test
    void 참여자_가져오기() {
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var participant1 = Participant.builder()
                .name("bear")
                .event(event)
                .build();
        participantRepository.save(participant1);
        var participant2 = Participant.builder()
                .name("rabbit")
                .event(event)
                .build();
        participantRepository.save(participant2);

        //when
        var expectedParticipant1 = participantRepository.findByName("bear").get();

        //then
        assertThat(expectedParticipant1).isEqualTo(participant1);
    }
    @Test
    void 참여한_이벤트_가져오기(){
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var participant = Participant.builder()
                .name("bear")
                .event(event)
                .build();
        participantRepository.save(participant);

        //when
        var expectedEvent = participant.getEvent();

        //then
        assertThat(expectedEvent).isEqualTo(event);
    }

    @Test
    void 참여가능한_시간_가져오기() {
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var participant = Participant.builder()
                .name("bear")
                .event(event)
                .build();
        participantRepository.save(participant);
        var participableTime1 = participableTimeRepository.save(ParticipableTime.builder()
                .event(event)
                .participant(participant)
                .build());
        var participableTime2 = participableTimeRepository.save(ParticipableTime.builder()
                .event(event)
                .participant(participant)
                .build());

        //when
        var participableTimeList = participantRepository.findParticipableTimeList(participant);

        //then
        assertThat(participableTimeList.size()).isEqualTo(2);
        assertThat(participableTimeList.containsAll(List.of(participableTime1, participableTime2)));

    }
}