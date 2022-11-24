package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import com.mimi.w2m.backend.domain.converter.SetParticipleTimeConverter;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.dto.event.EventRequestDto;
import com.mimi.w2m.backend.dto.participle.EventParticipleTimeRequestDto;
import com.mimi.w2m.backend.dto.security.UserSession;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.time.DayOfWeek.values;
import static java.util.stream.Collectors.toList;

/**
 * 이벤트 처리를 책임지는 서비스
 *
 * @author yeh35
 * @since 2022-10-31
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
private final HttpSession                httpSession;
private final UserService                userService;
private final EventParticipleTimeService eventParticipleTimeService;
private final EventRepository            eventRepository;
// TODO: 2022/11/20 Authorization 확인하기 -> controller 에서 수행한다

/**
 * 이벤트 생성
 *
 * @author yeh35
 * @since 2022-10-31
 */
@Transactional
public Event createEvent(EventRequestDto requestDto) throws EntityNotFoundException {
    var user = userService.getUser(requestDto.getUserId());
    return eventRepository.save(requestDto.to(user));
}

/**
 * 이벤트 수정 : host 는 항상 이벤트의 정보를 수정할 수 있다(D-day, title, description, time 등등)
 *
 * @author yeh35
 * @since 2022-10-31
 */
@Transactional
public Event modifyEvent(Long eventId, EventRequestDto requestDto) throws EntityNotFoundException {
    var event = getEvent(eventId);
    return event.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getColor().to(),
                        requestDto.getDDay());
}

/**
 * @author yeh35
 * @since 2022-11-05
 */
public Event getEvent(Long eventId) throws EntityNotFoundException {
    return eventRepository.findById(eventId).orElseThrow(() -> {
        throw new EntityNotFoundException("Event를 찾을 수 없습니다. : event = " + eventId, "Event를 찾을 수 없습니다");
    });
}

/**
 * eventParticipleTime 에 저장된 각 참여자의 가능한 시간의 공통 부분을 계산하여 dayOfWeeks, begin & end time 에 저장한다
 *
 * @author teddy
 * @since 2022/11/20
 **/
@Transactional
public Event calculateSharedTime(Long eventId) throws EntityNotFoundException, InvalidValueException {
    var event                = getEvent(eventId);
    var eventParticipleTimes = eventParticipleTimeService.getEventParticipleTimes(eventId);

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
 * Host 가 직접 event 의 시간을 정한다(EventParticipleTimeRequestDto 의 형식으로 보내고, 시간이 겹치는지 여부 등을 확인하지 않는다 Controller 에서 requestDto
 * 의 ownerId가 userSession 에 저장된 Id와 동일하고, event 를 생성한 사람인지 확인해야 한다
 *
 * @author teddy
 * @since 2022/11/21
 **/
@Transactional
public Event setEventTimeDirectly(EventParticipleTimeRequestDto requestDto) throws EntityNotFoundException,
                                                                                   InvalidValueException {
    var event = getEvent(requestDto.getEventId());
    var dayOfWeeks = new SetDayOfWeekConverter()
                             .convertToEntityAttribute(requestDto.getAbleDayOfWeeks());
    var participleTime = new SetParticipleTimeConverter()
                                 .convertToEntityAttribute(requestDto.getParticipleTimes());
    if(participleTime.size() != 1) {
        throw new InvalidValueException("참여 시간은 유일해야 합니다 : " + participleTime, "참여 시간은 유일해야 합니다");
    }
    return event.update(dayOfWeeks, participleTime.stream().toList().get(0));
}

@Transactional
public Event deleteEvent(Long eventId) throws EntityNotFoundException {
    var event = getEvent(eventId);
    return event.delete();
}

/**
 * Event 를 수정할 권리가 있는지 확인한다(host & Login 한 사용자와 동일한지 확인한다) -> controller 에서 수행한다
 *
 * @author yeh35
 * @since 2022-10-31
 */
public void checkEventModifiable(Long eventId, Long userId) throws UnauthorizedException, EntityNotFoundException {
    var userSession = Optional.ofNullable((UserSession) httpSession.getAttribute("user"))
                              .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 세션"));
    if(!userId.equals(userSession.getUserId())) {
        throw new UnauthorizedException(String.format("현재 이용자 정보와 일치하지 않습니다 : userId = %d, userSession = %d", userId,
                                                      userSession.getUserId()), "현재 이용자 정보와 일치하지 않습니다");
    }
    var user  = userService.getUser(userId);
    var event = getEvent(eventId);

    if(!event.getUser().getId().equals(user.getId())) {
        throw new UnauthorizedException(String.format("해당 이벤트에 수정권한이 없습니다. : user = %d, event = %d", userId,
                                                      eventId), "해당 이벤트에 수정권한이 없습니다.");
    }
}

/**
 * 이벤트 조회, 삭제된 이벤트는 조회되지 않는다
 *
 * @author yeh35
 * @since 2022-10-31
 */
public List<Event> getEventByTitle(String title) throws EntityNotFoundException {
    var events = eventRepository.findByTitle(title)
                                .stream()
                                .filter(event -> Objects.isNull(event.getDeletedDate()) ||
                                                 LocalDateTime.now().isBefore(event.getDeletedDate()))
                                .collect(toList());
    if(events.isEmpty()) {
        throw new EntityNotFoundException("이벤트가 존재하지 않습니다 : " + title, "이벤트가 존재하지 않습니다");
    } else {
        return events;
    }
}

/**
 * 삭제된 이벤트도 모두 조회된다
 *
 * @author teddy
 * @since 2022/11/21
 **/
public List<Event> getEventsCreatedByUser(Long userId) throws EntityNotFoundException {
    final var user   = userService.getUser(userId);
    var       events = eventRepository.findAllByUser(user);
    if(events.isEmpty()) {
        throw new EntityNotFoundException("이용자가 생성한 이벤트가 없습니다 : " + userId, "이용자가 생성한 이벤트가 없습니다");
    }
    {
        return events;
    }
}
}
