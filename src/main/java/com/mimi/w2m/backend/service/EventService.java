package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.client.kakao.KaKaoApiClient;
import com.mimi.w2m.backend.client.kakao.dto.calendar.KakaoCalenderListResponse;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendarType;
import com.mimi.w2m.backend.config.exception.EntityNotFoundException;
import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventSelectableParticipleTime;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.dto.event.EventRequestDto;
import com.mimi.w2m.backend.repository.EventParticipantRepository;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.EventSelectableParticipleTimeRepository;
import com.mimi.w2m.backend.repository.GuestRepository;
import com.mimi.w2m.backend.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final Logger logger = LoggerFactory.getLogger(EventService.class.getName());
    private final KaKaoApiClient kakaoApiClient;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventSelectableParticipleTimeRepository eventSelectableParticipleTimeRepository;
    private final GuestRepository guestRepository;
    private final EventParticipantRepository eventParticipantRepository;

    /**
     * 이벤트 생성(Host 는 EventParticipant 에 추가된다)
     *
     * @author yeh35
     * @since 2022-10-31
     */
    @Transactional
    public Event createEvent(UUID hostId, EventRequestDto requestDto) throws EntityNotFoundException {
        final var host = userService.getUser(hostId);
        final var event = eventRepository.save(requestDto.to(host));

        final var selectableParticipleTimes = EventSelectableParticipleTime.of(event, requestDto.getSelectableParticipleTimes().toParticipleTimeSet());
        eventSelectableParticipleTimeRepository.saveAll(selectableParticipleTimes);

        return event;
    }

    /**
     * 이벤트 수정 : host 는 항상 이벤트의 정보를 수정할 수 있다(D-day, title, description, time 등등)
     *
     * @author yeh35
     * @since 2022-10-31
     */
    @Transactional
    public Event modifyEvent(UUID eventId, EventRequestDto requestDto) throws EntityNotFoundException {
        var event = getEvent(eventId);
        event.update(
                requestDto.getTitle(),
                requestDto.getDDay(),
                requestDto.getColor(),
                requestDto.getDescription()
        );

        eventSelectableParticipleTimeRepository.deleteByEvent(event);
        eventSelectableParticipleTimeRepository.saveAll(EventSelectableParticipleTime.of(event, requestDto.getSelectableParticipleTimes().toParticipleTimeSet()));

        return event;
    }

    /**
     * @author yeh35
     * @since 2022-11-05
     */
    public Event getEvent(UUID id) throws EntityNotFoundException {
        return eventRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("[EventService] Entity Not Found(id=%s)", id));
        });
    }

    /**
     * @author yeh35
     * @since 2022-11-05
     */
    public Set<ParticipleTime> getEventSelectableParticipleTimes(UUID eventId) throws EntityNotFoundException {
        final var event = getEvent(eventId);

        return eventSelectableParticipleTimeRepository.findByEvent(event)
                .stream()
                .map(EventSelectableParticipleTime::toParticipleTime)
                .collect(Collectors.toSet());

    }


    /**
     * 이벤트 삭제하기,
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Transactional
    public void delete(UUID eventId) throws EntityNotFoundException {
        final var event = getEvent(eventId);

        eventParticipantRepository.deleteByEvent(event);
        guestRepository.deleteByEvent(event);
        eventSelectableParticipleTimeRepository.deleteByEvent(event);
        eventRepository.delete(event);
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
            final var msg = String.format("[EventService] Entity Not Found(title=%s)", title);
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
    public List<Event> getAllByHost(UUID hostId) throws EntityNotFoundException {
        final var user = userService.getUser(hostId);
        final var events = eventRepository.findAllByHost(user);
        if (events.isEmpty()) {
            final var msg = String.format("[EventService] Entity Not Found(host=%s)", hostId);
            throw new EntityNotFoundException(msg);
        } else {
            return events;
        }
    }

    public void deleteAll(List<Event> events) {
        eventRepository.deleteAll(events);
    }

    public KakaoCalenderListResponse getKakaoCalendars(String accessToken, KakaoCalendarType filter) {
        return kakaoApiClient.getCalenders(HttpUtils.withBearerToken(accessToken), filter);
    }
}
