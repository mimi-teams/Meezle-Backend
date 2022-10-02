package com.mimi.w2m.backend.domain.eventParticipableTime;

import com.mimi.w2m.backend.domain.event.Event;
import com.mimi.w2m.backend.domain.event.EventRepository;
import com.mimi.w2m.backend.domain.eventParticipant.Participant;
import com.mimi.w2m.backend.domain.eventParticipant.ParticipantRepository;
import com.mimi.w2m.backend.domain.user.User;
import com.mimi.w2m.backend.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ParticipableTimeRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParticipantRepository participantRepository;
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

        var participant = Participant.builder()
                .name("bear")
                .event(event)
                .build();
        participantRepository.save(participant);
    }

    @Test
    void 참여가능시간_생성하기() {
        //given
        var user = userRepository.findByName("teddy").get();
        var event = eventRepository.findByName("teddyEvent").get();
        var participableTime = ParticipableTime.builder()
                .user(user)
                .event(event)
                .build();

        //when
        participableTimeRepository.save(participableTime);

        //then
        assertThat(participableTimeRepository.findAll().get(0)).isEqualTo(participableTime);
    }

    /**
     * 참여가능한시간은 이벤트, 유저, 참여자와 연관되어만 의미가 있다. 따라서 해당 도메인에서 찾는 것으로 바꿨다.
     */
//    @Test
//    void 참여가능시간_가져오기() {
//        //given
//        var user = userRepository.findByName("teddy").get();
//        var event = eventRepository.findByName("teddyEvent").get();
//        var participant = participantRepository.findByName("bear").get();
//        var participableTime = ParticipableTime.builder()
//                .user(user)
//                .participant(participant)
//                .event(event)
//                .build();
//        participableTimeRepository.save(participableTime);
//
//        //when
//        var expectedParticipableTime1 = participableTimeRepository.findByUser(user).get();
//        var expectedParticipableTime2 = participableTimeRepository.findByParticipant(participant).get();
//        var expectedParticipableTime3 = participableTimeRepository.findByEvent(event).get();
//
//        //then
//        assertThat(participableTime).isEqualTo(expectedParticipableTime1).isEqualTo(expectedParticipableTime2).isEqualTo(expectedParticipableTime3);
//
//    }

    @Test
    void 참여가능시간_수정하기() {
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var participableTime = participableTimeRepository.save(ParticipableTime.builder()
                .event(event)
                .build());
        var expectedUser = userRepository.findByName("teddy").get();
        var expectedParticipant = participantRepository.findByName("bear").get();
        var expectedAbleDate = LocalDate.of(1999, 3, 8);
        var expectedStartTime = LocalTime.of(12, 12);
        var expectedEndTime = LocalTime.of(13, 13);

        //when
        participableTime.updateUser(expectedUser);
        participableTime.updateParticipant(expectedParticipant);
        participableTime.updateTime(expectedAbleDate, expectedStartTime, expectedEndTime);
        var expectedParticipableTime = participableTimeRepository.findAll().get(0);

        //then
        assertThat(expectedParticipableTime.getUser()).isEqualTo(expectedUser);
        assertThat(expectedParticipableTime.getParticipant()).isEqualTo(expectedParticipant);
        assertThat(expectedParticipableTime.getAbleDate()).isEqualTo(expectedAbleDate);
        assertThat(expectedParticipableTime.getStartTime()).isEqualTo(expectedStartTime);
        assertThat(expectedParticipableTime.getEndTime()).isEqualTo(expectedEndTime);
    }

    @Test
    void 참여가능시간_제거하기() {
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var participableTime = participableTimeRepository.save(ParticipableTime.builder()
                .event(event)
                .build());

        //when
        participableTimeRepository.delete(participableTime);

        //then
        assertThat(participableTimeRepository.findAll().isEmpty());

    }

}