package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.participle.EventParticipleTimeRequestDto;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.repository.EventParticipleTimeRepository;
import com.mimi.w2m.backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 각 참여자의 이벤트 참여 시간을 담당하는 서비스
 *
 * @author yeh35
 * @since 2022-11-01
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventParticipleTimeService {
private final UserService                   userService;
private final ParticipantService            participantService;
private final EventRepository               eventRepository;
private final EventParticipleTimeRepository eventParticipleTimeRepository;

/**
 * 이벤트 참여 참여 시간을 입력하는데, 기존에 참여 시간은 지우고, 전부 다시 Insert한다.
 *
 * @author yeh35
 * @since 2022-11-01
 */
@Transactional
public EventParticipleTime createOrUpdate(EventParticipleTimeRequestDto requestDto, Role role) throws InvalidValueException, EntityNotFoundException {
    var event = eventRepository.findById(requestDto.getEventId())
                               .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트 : " + requestDto.getEventId(), "존재하지 않는 이벤트"));
    if(role == Role.USER) {
        var user = userService.getUser(requestDto.getOwnerId());
        eventParticipleTimeRepository.deleteAll(eventParticipleTimeRepository.findAllByEntityAtEvent(user, event));
        return eventParticipleTimeRepository.save(requestDto.to(event, user));

    } else if(role == Role.PARTICIPANT) {
        var participant = participantService.getParticipant(requestDto.getOwnerId());
        eventParticipleTimeRepository.deleteAll(eventParticipleTimeRepository.findAllByEntityAtEvent(participant,
                                                                                                     event));
        return eventParticipleTimeRepository.save(requestDto.to(event, participant));
    } else {
        throw new InvalidValueException("유효하지 않은 사용자 : " + role.getKey(), "유효하지 않은 사용자");
    }
}

/**
 * 특정 이벤트 참여 가능한 모든 시간 조회(등록된 모든 것)
 *
 * @author yeh35
 * @since 2022-10-31
 */
public List<EventParticipleTime> getEventParticipleTimes(Long eventId) throws EntityNotFoundException {
    var event = eventRepository.findById(eventId)
                               .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트 : " + eventId, "존재하지 않는 " +
                                                                                                          "이벤트"));
    return eventParticipleTimeRepository.findAllByEvent(event);
}

/**
 * 특정 이벤트 참여자가 입력한 참여 가능한 시간
 *
 * @author yeh35
 * @since 2022-11-01
 */
public List<EventParticipleTime> getEventParticipleTimes(Long eventId, Long id, Role role) throws EntityNotFoundException, InvalidValueException {
    var event = eventRepository.findById(eventId)
                               .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트 : " + eventId, "존재하지 않는 " +
                                                                                                          "이벤트"));
    if(role == Role.USER) {
        var user = userService.getUser(id);
        return eventParticipleTimeRepository.findAllByEntityAtEvent(user, event);
    } else if(role == Role.PARTICIPANT) {
        var participant = participantService.getParticipant(id);
        return eventParticipleTimeRepository.findAllByEntityAtEvent(participant, event);
    } else {
        throw new InvalidValueException("유효하지 않은 사용자 : " + role.getKey(), "유효하지 않은 사용자");
    }
}
}
