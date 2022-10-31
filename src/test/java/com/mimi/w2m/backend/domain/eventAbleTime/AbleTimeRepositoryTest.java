package com.mimi.w2m.backend.domain.eventAbleTime;

import com.mimi.w2m.backend.domain.AbleTime;
import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.repository.AbleTimeRepository;
import com.mimi.w2m.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AbleTimeRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AbleTimeRepository ableTimeRepository;
    @BeforeEach
    void setup() {
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .role(Role.Tester)
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
    void 가능한시간_생성하기(){
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var ableTime = AbleTime.builder()
                .event(event)
                .ableDate(LocalDate.of(1999, 3, 8))
                .startTime(LocalTime.of(12, 12))
                .endTime(LocalTime.of(13, 13))
                .build();
        //when
        ableTimeRepository.save(ableTime);

        //then
        assertThat(ableTimeRepository.findAll().get(0)).isEqualTo(ableTime);
    }
    @Test
    void 가능한시간_수정하기() {
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var ableTime = AbleTime.builder()
                .event(event)
                .ableDate(LocalDate.of(1999, 3, 8))
                .startTime(LocalTime.of(12, 12))
                .endTime(LocalTime.of(13, 13))
                .build();
        ableTimeRepository.save(ableTime);
        var expectedAbleDate = LocalDate.of(2022, 10, 2);
        var expectedStartTime = LocalTime.of(15,15);
        var expectedEndTime = LocalTime.of(17, 17);

        //when
        ableTime.update(expectedAbleDate, expectedStartTime, expectedEndTime);
        var expectedAbleTime = ableTimeRepository.findAll().get(0);

        //then
        assertThat(expectedAbleTime.getAbleDate().isEqual(expectedAbleDate));
        assertThat(expectedAbleTime.getStartTime()).isEqualTo(expectedStartTime);
        assertThat(expectedAbleTime.getEndTime()).isEqualTo(expectedEndTime);
    }
    @Test
    void 가능한시간_제거하기() {
        //given
        var event = eventRepository.findByName("teddyEvent").get();
        var ableTime = ableTimeRepository.save(AbleTime.builder()
                .event(event)
                .ableDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(1))
                .build());

        //when
        ableTimeRepository.delete(ableTime);

        //then
        assertThat(ableTimeRepository.findAll().isEmpty());
    }
    @Test
    void 이벤트_가능한_시간_가져오기() {
        //given
        var user = userRepository.findByName("teddy").get();
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
        var ableTimeList = ableTimeRepository.findAllByEvent(event);

        //then
        assertThat(ableTimeList.size()).isEqualTo(2);
        assertThat(ableTimeList.containsAll(List.of(ableTime1, ableTime2)));
    }

}