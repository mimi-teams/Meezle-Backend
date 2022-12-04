package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.repository.EventParticipantRepository;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipant;
import com.mimi.w2m.backend.dto.event.EventRequestDto;
import com.mimi.w2m.backend.dto.participant.EventParticipantRequestDto;
import com.mimi.w2m.backend.exception.EntityNotFoundException;
import com.mimi.w2m.backend.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;

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
    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final Logger logger = LoggerFactory.getLogger(EventService.class.getName());

    /**
     * 이벤트 생성(Host 는 EventParticipant 에 추가된다)
     *
     * @author yeh35
     * @since 2022-10-31
     */
    @Transactional
    public Event createEvent(Long hostId, EventRequestDto requestDto) throws EntityNotFoundException {
        final var host = userService.get(hostId);
        final var event = eventRepository.save(requestDto.to(host));
        final var participant = EventParticipant.builder()
                .event(event)
                .user(host)
                .build();
        eventParticipantRepository.save(participant);
        return event;
    }

    /**
     * 이벤트 수정 : host 는 항상 이벤트의 정보를 수정할 수 있다(D-day, title, description, time 등등)
     *
     * @author yeh35
     * @since 2022-10-31
     */
    @Transactional
    public Event modifyEvent(Long eventId, EventRequestDto requestDto) throws EntityNotFoundException {
        var event = get(eventId);
        return event.update(requestDto.getTitle(), requestDto.getDDay(), requestDto.getSelectableParticipleTimes(),
                requestDto.getColor()
                        .to(), requestDto.getDescription());
    }

    /**
     * @author yeh35
     * @since 2022-11-05
     */
    public Event get(Long id) throws EntityNotFoundException {
        final var event = eventRepository.findById(id);
        if (event.isPresent()) {
            return event.get();
        } else {
            final var formatter = new Formatter();
            final var msg = formatter.format("[EventService] Entity Not Found(id=%d)", id)
                    .toString();
            throw new EntityNotFoundException(msg);
        }
    }

    /**
     * Host 가 직접 event 의 참여 시간을 수정한다
     *
     * @author teddy
     * @since 2022/11/21
     **/
    @Transactional
    public Event modifySelectedDaysAndTimesDirectly(EventParticipantRequestDto requestDto)
            throws EntityNotFoundException, InvalidValueException {
        final var event = get(requestDto.getEventId());
        event.setSelectedDaysAndTimes(requestDto.getAbleDaysAndTimes());
        return event;
    }

    /**
     * 이벤트 삭제하기(진짜)
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Transactional
    public void deleteReal(Long eventId) throws EntityNotFoundException {
        final var event = get(eventId);
        eventRepository.delete(event);
    }

    /**
     * 이벤트 삭제하기(deletedAt 설정하기)
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Transactional
    public void deleteNotReal(Long eventId) throws EntityNotFoundException {
        final var event = get(eventId);
        event.delete();
    }

    /**
     * 이벤트 조회, 삭제된 이벤트는 조회되지 않는다
     *
     * @author yeh35
     * @since 2022-10-31
     */
    public List<Event> getAllByTitle(String title) throws EntityNotFoundException {
        final var events = eventRepository.findAllByTitle(title)
                .stream()
                .filter(event -> Objects.isNull(event.getDeletedAt()) || event.getDeletedAt()
                        .isAfter(
                                LocalDateTime.now()))
                .toList();
        if (events.isEmpty()) {
            final var formatter = new Formatter();
            final var msg = formatter.format("[EventService] Entity Not Found(title=%s)", title)
                    .toString();
            throw new EntityNotFoundException(msg);
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
    public List<Event> getAllByHost(Long hostId) throws EntityNotFoundException {
        final var user = userService.get(hostId);
        final var events = eventRepository.findAllByHost(user);
        if (events.isEmpty()) {
            final var formatter = new Formatter();
            final var msg = formatter.format("[EventService] Entity Not Found(host=%d)", hostId)
                    .toString();
            throw new EntityNotFoundException(msg);
        } else {
            return events;
        }
    }

    public void deleteAll(List<Event> events) {
        eventRepository.deleteAll(events);
    }
}
