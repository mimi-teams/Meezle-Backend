package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.dto.event.EventDto;
import com.mimi.w2m.backend.repository.EventParticipableTimeRepository;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * 이벤트 처리를 책임지는 서비스
 *
 * @author yeh35
 * @since 2022-10-31
 */

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventService {

    private final UserService userService;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventParticipableTimeRepository eventParticipableTimeRepository;

    /**
     * 이벤트 생성
     *
     * @author yeh35
     * @since 2022-10-31
     */
    @Transactional
    public Event createEvent(Long userId, EventDto eventDto) {
        final var user = userService.getUser(userId);
        final var event = eventDto.createEntity(user);
        return eventRepository.save(event);
    }

    /**
     * 이벤트 수정
     *
     * @author yeh35
     * @since 2022-10-31
     */
    @Transactional
    public Event modifyEvent(Long eventId, Long userId, String title, LocalDateTime dDay) throws NoSuchElementException, IllegalAccessError {
        //TODO 이벤트 참여자가 등록하기 전까지는 수정 가능 ?? 등록해도 host는 수정 가능해야 하지 않낭?
        if (checkEventModifiable(eventId, userId)) {
            var event = eventRepository.findById(eventId).orElseThrow();
            event.setTitle(title);
            event.setDDay(dDay);
            return event;
        } else {
            throw new IllegalAccessError();
        }
    }

    @Transactional
    public void deleteEvent(Long eventId, Long userId) throws NoSuchElementException, IllegalAccessError {
        if (checkEventModifiable(eventId, userId)) {
            var event = eventRepository.findById(eventId).orElseThrow();
            event.setDeletedDate(LocalDateTime.now());
        } else {
            throw new IllegalAccessError();
        }
    }

    /**
     * 이벤트 조회,
     *
     * @author yeh35
     * @since 2022-10-31
     */
    public Event getEventByTitle(String title) throws NoSuchElementException {
        var event = eventRepository.findByTitle(title).orElseThrow();
        if (LocalDateTime.now().compareTo(event.getDeletedDate()) < 0) {
            return event;
        } else {
            throw new NoSuchElementException();
        }
    }

    public Event getEventById(Long eventId) throws NoSuchElementException {
        var event = eventRepository.findById(eventId).orElseThrow();
        if (LocalDateTime.now().compareTo(event.getDeletedDate()) < 0) {
            return event;
        } else {
            throw new NoSuchElementException();
        }
    }

    public List<Event> getEventsCreatedByUser(Long userId) {
        final var user = userService.getUser(userId);

        return eventRepository.findAllByUser(user).stream().filter(entity -> {
            return LocalDateTime.now().compareTo(entity.getDeletedDate()) < 0;
        }).collect(Collectors.toList());
    }

    public List<Event> getEventsCreatedByUserIncludingDeleted(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        return eventRepository.findAllByUser(user);
    }

    /**
     * Event를 수정할 권리가 있는지 확인한다(host인지 확인한다)
     *
     * @author yeh35
     * @since 2022-10-31
     */
    public Boolean checkEventModifiable(Long eventId, Long userId) throws NoSuchElementException {
        var user = userRepository.findById(userId).orElseThrow();

        var event = eventRepository.findById(eventId).orElseThrow();
        return event.getUser().equals(user);
    }

}
