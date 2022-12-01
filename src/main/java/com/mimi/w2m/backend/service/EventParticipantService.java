package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.repository.EventParticipantRepository;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.common.TimeRange;
import com.mimi.w2m.backend.type.converter.db.SetParticipleTimeConverter;
import com.mimi.w2m.backend.type.domain.Event;
import com.mimi.w2m.backend.type.domain.EventParticipant;
import com.mimi.w2m.backend.type.dto.participant.EventParticipantRequestDto;
import com.mimi.w2m.backend.type.response.exception.EntityNotFoundException;
import com.mimi.w2m.backend.type.response.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;

/**
 * 각 참여자의 이벤트 참여 시간을 담당하는 서비스
 *
 * @author yeh35
 * @since 2022-11-01
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventParticipantService {
    private final UserService                userService;
    private final GuestService               guestService;
    private final EventService               eventService;
    private final EventParticipantRepository eventParticipantRepository;

    /**
     * 이벤트 참여자 등록
     *
     * @author yeh35
     * @since 2022-11-01
     */
    @Transactional
    public EventParticipant create(Long roleId, Role role, EventParticipantRequestDto requestDto)
    throws InvalidValueException, EntityNotFoundException {
        return switch(role) {
            case USER -> eventParticipantRepository.save(
                    requestDto.to(eventService.get(requestDto.getEventId()), userService.get(roleId)));
            case GUEST -> eventParticipantRepository.save(
                    requestDto.to(eventService.get(requestDto.getEventId()), guestService.get(roleId)));
        };
    }

    /**
     * 이벤트 참여자 반환
     *
     * @author yeh35
     * @since 2022-11-01
     */
    public EventParticipant get(Long eventId, Long roleId, Role role) throws EntityNotFoundException {
        final var event     = eventService.get(eventId);
        final var formatter = new Formatter();
        final var msg = formatter.format("[EventParticipantService] Entity Not Found(event=%d, id=%d, role=%s)",
                                         eventId, roleId, role)
                                 .toString();
        return switch(role) {
            case USER -> eventParticipantRepository.findByUserInEvent(userService.get(roleId), event)
                                                   .orElseThrow(() -> new EntityNotFoundException(msg));

            case GUEST -> eventParticipantRepository.findByGuestInEvent(guestService.get(roleId), event)
                                                    .orElseThrow(() -> new EntityNotFoundException(msg));

        };
    }

    @Transactional
    public void deleteAll(List<EventParticipant> eventParticipants) {
        eventParticipantRepository.deleteAll(eventParticipants);
    }

    /**
     * eventParticipleTime 에 저장된 각 참여자의 가능한 시간의 공통 부분을 계산하여 dayOfWeeks, begin & end time 에 저장한다. 모두가 가능한 시간을 찾으므로, 전원 일치
     * 알고리즘으로 수행한다. 또한, 선택하지 않은 참여자는 무시한다
     *
     * @author teddy
     * @since 2022/11/20
     **/
    @Transactional
    public Event calculateSharedTime(Long eventId) throws EntityNotFoundException, InvalidValueException {
        final var formatter    = new Formatter();
        final var converter    = new SetParticipleTimeConverter();
        final var event        = eventService.get(eventId);
        final var participants = getAll(eventId);

        final var selectableDaysAndTimesMap = converter.convertToMap(event.getSelectableDaysAndTimes());
        final var selectedDaysAndTimesMap   = new HashMap<DayOfWeek, Set<TimeRange>>();

        participants.forEach(p -> {
            final var ableDaysAndTimesMap = converter.convertToMap(p.getAbleDaysAndTimes());
            if(verify(selectableDaysAndTimesMap, ableDaysAndTimesMap)) {
                ableDaysAndTimesMap.forEach((d, t) -> {
                    if(selectedDaysAndTimesMap.containsKey(d)) {
                        // 해당 요일에 가능한 누군가 있다면, 선택될 수 있다.
                        final var selectedTimeRange = selectedDaysAndTimesMap.get(d);
                        final var updatedTimeRange  = findSharedRange(t, selectedTimeRange);
                        selectedDaysAndTimesMap.put(d, updatedTimeRange);
                    } else if(selectedDaysAndTimesMap.isEmpty()) {
                        // 처음에는 그냥 넣을 수 있다
                        selectedDaysAndTimesMap.put(d, t);
                    }
                    // 처음이 아니거나, 앞서 해당 요일을 선택한 참여자가 없다면, 전원 일치가 불가능하므로 무시한다
                });
            } else {
                final var msg = formatter.format(
                                                 "[EventParticipantService] Out Of Selectable Range(event=%d, " +
                                                 "participant=%d)", eventId,
                                                 p.getId())
                                         .toString();
                throw new InvalidValueException(msg);
            }
        });
        final var selectedDaysAndTimes = converter.convertToSet(selectedDaysAndTimesMap);
        event.setSelectedDaysAndTimes(selectedDaysAndTimes);
        return event;
    }

    /**
     * 특정 이벤트 참여 가능한 모든 시간 조회(등록된 모든 것)
     *
     * @author yeh35
     * @since 2022-10-31
     */
    public List<EventParticipant> getAll(Long eventId) throws EntityNotFoundException {
        final var event        = eventService.get(eventId);
        final var participants = eventParticipantRepository.findAllInEvent(event);
        if(participants.isEmpty()) {
            final var formatter = new Formatter();
            final var msg = formatter.format("[EventParticipantService] Any Entity Not Found(event=%d)", eventId)
                                     .toString();
            throw new EntityNotFoundException(msg);
        } else {
            return participants;
        }
    }

    private boolean verify(Map<DayOfWeek, Set<TimeRange>> selectable, Map<DayOfWeek, Set<TimeRange>> selected) {
        return false;
    }

    private Set<TimeRange> findSharedRange(Set<TimeRange> first, Set<TimeRange> second) {
        return null;
    }
}
