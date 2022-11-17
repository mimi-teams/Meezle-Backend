package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.repository.EventParticipleTimeRepository;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.ParticipantRepository;
import com.mimi.w2m.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * 이벤트 참여를 책임지는 서비스
 *
 * @author yeh35
 * @since 2022-11-01
 */

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventParticipateService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipantRepository         participantRepository;
    private final EventParticipleTimeRepository eventParticipleTimeRepository;

    /**
     * 이벤트 참여
     * 참여 시간을 입력하는데, 기존에 참여 시간은 지우고, 전부 다시 Insert한다.
     *
     * @author yeh35
     * @since 2022-11-01
     */
    @Transactional
    public List<EventParticipleTime> updateParticipatableTime(Long eventId, Long id, Role role, HashSet<HashMap<String, LocalDateTime>> times) throws NoSuchElementException, IllegalAccessException {
        //TODO 참여자가 처음인 경우 participantService.createParticipant() -> 참여자는 이미 id, pw로 로그인 후 참여한다
        //기존이 참여한 이벤트 참여자의 경우 참여 가능한 시간 Delete -> Insert --> 모든 참여 가능 시간 지우고, 새로운 것으로 채운다.
        var event = eventRepository.findById(eventId).orElseThrow();
        if (role == Role.USER) {
            var user = userRepository.findById(id).orElseThrow();
            eventParticipleTimeRepository.findAllByEvent(event).stream().filter(entity -> {
                return entity.getUser().equals(user);
            }).forEach(entity -> eventParticipleTimeRepository.delete(entity));

            return times.stream().map(time -> {
                var ableDate = time.get("date").toLocalDate();
                var startTime = time.get("start").toLocalTime();
                var endTime = time.get("end").toLocalTime();

                var entity = EventParticipleTime.builder()
                                                .event(event)
                                                .user(user)
                                                .participant(null)
                                                .ableDate(ableDate)
                                                .startTime(startTime)
                                                .endTime(endTime)
                                                .build();
                return eventParticipleTimeRepository.save(entity);
            }).collect(Collectors.toList());
        } else if (role == Role.PARTICIPANT) {
            var participant = participantRepository.findById(id).orElseThrow();
            eventParticipleTimeRepository.findAllByEvent(event).stream().filter(entity -> {
                return entity.getParticipant().equals(participant);
            }).forEach(entity -> eventParticipleTimeRepository.delete(entity));

            return times.stream().map(time -> {
                var ableDate = time.get("date").toLocalDate();
                var startTime = time.get("start").toLocalTime();
                var endTime = time.get("end").toLocalTime();

                var entity = EventParticipleTime.builder()
                                                .event(event)
                                                .user(null)
                                                .participant(participant)
                                                .ableDate(ableDate)
                                                .startTime(startTime)
                                                .endTime(endTime)
                                                .build();
                return eventParticipleTimeRepository.save(entity);
            }).collect(Collectors.toList());
        } else {
            throw new IllegalAccessException(role.name());
        }
    }

    /**
     * 특정 이벤트 참여 가능한 시간 조회
     *
     * @author yeh35
     * @since 2022-10-31
     */
    public List<EventParticipleTime> getEventsParticipate(Long eventId) throws NoSuchElementException {
        var event = eventRepository.findById(eventId).orElseThrow();
        return eventParticipleTimeRepository.findAllByEvent(event);
    }

    /**
     * 특정 이벤트 참여자가 입력한 참여 가능한 시간
     *
     * @author yeh35
     * @since 2022-11-01
     */
    public List<EventParticipleTime> getEventsParticipate(Long eventId, Long id, Role role) throws NoSuchElementException, IllegalAccessException {
        var event = eventRepository.findById(eventId).orElseThrow();
        if (role == Role.USER) {
            var user = userRepository.findById(id).orElseThrow();
            return eventParticipleTimeRepository.findAllByEvent(event).stream().filter(entity -> {
                return entity.getUser().equals(user);
            }).collect(Collectors.toList());
        } else if (role == Role.PARTICIPANT) {
            var participant = participantRepository.findById(id).orElseThrow();
            return eventParticipleTimeRepository.findAllByEvent(event).stream().filter(entity -> {
                return entity.getParticipant().equals(participant);
            }).collect(Collectors.toList());
        } else {
            throw new IllegalAccessException();
        }
    }
}
