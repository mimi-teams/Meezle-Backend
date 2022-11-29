package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipleTime;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.participle.EventParticipleTimeRequestDto;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.repository.EventParticipleTimeRepository;
import com.mimi.w2m.backend.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.time.DayOfWeek.values;

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
private final EventService                  eventService;
private final GuestRepository               guestRepository;
private final EventParticipleTimeRepository eventParticipleTimeRepository;

/**
 * 이벤트 참여 참여 시간을 입력하는데, 기존에 참여 시간은 지우고, 전부 다시 Insert한다.
 *
 * @author yeh35
 * @since 2022-11-01
 */
@Transactional
public EventParticipleTime createOrUpdate(Long eventId, EventParticipleTimeRequestDto requestDto, Long ownerId,
                                          Role role) throws InvalidValueException, EntityNotFoundException {
    var event = eventService.getEvent(eventId);
    if(role == Role.USER) {
        var user = userService.getUser(ownerId);
        eventParticipleTimeRepository.deleteAll(eventParticipleTimeRepository.findAllByEntityAtEvent(user, event));
        return eventParticipleTimeRepository.save(requestDto.to(event, user));

    } else if(role == Role.GUEST) {
        var guest = guestRepository.findById(ownerId)
                                   .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자 : " + ownerId,
                                                                                  "존재하지 않는 참여자"));
        eventParticipleTimeRepository.deleteAll(eventParticipleTimeRepository.findAllByEntityAtEvent(guest, event));
        return eventParticipleTimeRepository.save(requestDto.to(event, guest));
    } else {
        throw new InvalidValueException("유효하지 않은 사용자 : " + role.getKey(), "유효하지 않은 사용자");
    }
}

/**
 * 특정 이벤트 참여자가 입력한 참여 가능한 시간
 *
 * @author yeh35
 * @since 2022-11-01
 */
public List<EventParticipleTime> getEventParticipleTimes(Long eventId, Long id, Role role) throws EntityNotFoundException, InvalidValueException {
    var event = eventService.getEvent(eventId);
    if(role == Role.USER) {
        var user = userService.getUser(id);
        return eventParticipleTimeRepository.findAllByEntityAtEvent(user, event);
    } else if(role == Role.GUEST) {
        var guest = guestRepository.findById(id)
                                   .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자 : " + id,
                                                                                  "존재하지 않는 참여자"));
        return eventParticipleTimeRepository.findAllByEntityAtEvent(guest, event);
    } else {
        throw new InvalidValueException("유효하지 않은 사용자 : " + role.getKey(), "유효하지 않은 사용자");
    }
}

public void deleteAll(List<EventParticipleTime> eventParticipleTimes) {
    eventParticipleTimeRepository.deleteAll(eventParticipleTimes);
}

/**
 * eventParticipleTime 에 저장된 각 참여자의 가능한 시간의 공통 부분을 계산하여 dayOfWeeks, begin & end time 에 저장한다
 *
 * @author teddy
 * @since 2022/11/20
 **/
@Transactional
public Event calculateSharedTime(Long eventId) throws EntityNotFoundException, InvalidValueException {
    var event                = eventService.getEvent(eventId);
    var eventParticipleTimes = getEventParticipleTimes(eventId);

    final var dayOfWeeks = new java.util.HashSet<>(Set.of(values()));
    final var ableTime   = new java.util.HashMap<>(Map.of("beginTime", LocalTime.MIN, "endTime", LocalTime.MAX));

    eventParticipleTimes.forEach((entity) -> {
        dayOfWeeks.retainAll(entity.getAbleDayOfWeeks());
        entity.getParticipleTimes().forEach((participleTime) -> {
            if(participleTime.beginTime().isAfter(ableTime.get("beginTime"))) {
                ableTime.put("beginTime", participleTime.beginTime());
            }
            if(participleTime.endTime().isBefore(ableTime.get("endTime"))) {
                ableTime.put("endTime", participleTime.endTime());
            }
        });
    });
    final var participleTime = ParticipleTime.of(ableTime);
    return event.update(dayOfWeeks, participleTime);
}

/**
 * 특정 이벤트 참여 가능한 모든 시간 조회(등록된 모든 것)
 *
 * @author yeh35
 * @since 2022-10-31
 */
public List<EventParticipleTime> getEventParticipleTimes(Long eventId) throws EntityNotFoundException {
    var event = eventService.getEvent(eventId);
    return eventParticipleTimeRepository.findAllByEvent(event);
}
}
