package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.repository.EventParticipantRepository;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.common.TimeRange;
import com.mimi.w2m.backend.type.converter.db.SetParticipleTimeConverter;
import com.mimi.w2m.backend.type.domain.Event;
import com.mimi.w2m.backend.type.domain.EventParticipant;
import com.mimi.w2m.backend.type.dto.participant.EventParticipantRequestDto;
import com.mimi.w2m.backend.type.response.exception.EntityDuplicatedException;
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
    public EventParticipant create(EventParticipantRequestDto requestDto)
    throws InvalidValueException, EntityNotFoundException, EntityDuplicatedException {
        final var formatter = new Formatter();
        final var event     = eventService.get(requestDto.getEventId());
        return switch(requestDto.getOwnerType()) {
            case USER -> {
                final var user  = userService.get(requestDto.getOwnerId());
                final var other = eventParticipantRepository.findByUserInEvent(user, event);
                if(other.isPresent()) {
                    final var msg = formatter.format(
                                                     "[EventParticipantService] Entity Duplicated(event=%d, id=%d, " + "role=%s)", event.getId(),
                                                     user.getId(), Role.USER.getKey())
                                             .toString();
                    throw new EntityDuplicatedException(msg);
                } else {
                    yield eventParticipantRepository.save(requestDto.to(event, user));
                }
            }
            case GUEST -> {
                final var msg = formatter.format("[EventParticipantService] Invalid Request(event=%d, role=%s)",
                                                 event.getId(), Role.GUEST.getKey())
                                         .toString();
                throw new InvalidValueException(msg);
            }
        };
    }

    /**
     * 참여자 정보 수정
     *
     * @author teddy
     * @since 2022/12/02
     **/
    @Transactional
    public EventParticipant modify(Long id, EventParticipantRequestDto requestDto) throws EntityNotFoundException {
        final var participant = get(id);
        return participant.update(requestDto.getAbleDaysAndTimes());
    }

    public EventParticipant get(Long id) throws EntityNotFoundException {
        final var participant = eventParticipantRepository.findById(id);
        if(participant.isPresent()) {
            return participant.get();
        } else {
            final var formatter = new Formatter();
            final var msg = formatter.format("[EventParticipantService] Entity Not Found(id=%d)", id)
                                     .toString();
            throw new EntityNotFoundException(msg);
        }
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
            if(!verify(selectableDaysAndTimesMap, ableDaysAndTimesMap)) {
                final var msg = formatter.format(
                                                 "[EventParticipantService] Out Of Selectable Range(event=%d, " +
                                                 "participant=%d)", eventId,
                                                 p.getId())
                                         .toString();
                throw new InvalidValueException(msg);
            }
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
        });
        event.setSelectedDaysAndTimes(converter.convertToSet(selectedDaysAndTimesMap));
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
        for(var day : selected.keySet()) {
            if(!selectable.containsKey(day)) {
                return false;
            }
            final var selectableTimeRanges = selectable.get(day);

            final var selectedTimeRanges = selected.get(day);
            for(var selectedRange : selectedTimeRanges) {
                var isIncluded = false;
                for(var selectableRange : selectableTimeRanges) {
                    final var intersectedRange = TimeRange.Operator.intersection(selectedRange, selectableRange)
                                                                   .getLeft();
                    if(!Objects.equals(intersectedRange, selectedRange)) {
                        isIncluded = true;
                        break;
                    }
                }
                if(!isIncluded) {
                    return false;
                }
            }

        }
        return true;
    }

    private Set<TimeRange> findSharedRange(Set<TimeRange> firstRanges, Set<TimeRange> secondRanges) {
        final var out = new HashSet<TimeRange>();
        final var orderedFirstRanges = firstRanges.stream()
                                                  .sorted()
                                                  .toList();
        final var orderedSecondRanges = secondRanges.stream()
                                                    .sorted()
                                                    .toList();
        // Union Each
        final var unionOrderedFirstRanges  = unionRanges(orderedFirstRanges);
        final var unionOrderedSecondRanges = unionRanges(orderedSecondRanges);

        // Intersect Each
        return intersectRanges(unionOrderedFirstRanges, unionOrderedSecondRanges);
    }

    private List<TimeRange> unionRanges(List<TimeRange> orderedRanges) {
        final var unionOrderedRanges = new LinkedList<TimeRange>();

        orderedRanges.forEach(range -> {
            if(unionOrderedRanges.isEmpty()) {
                unionOrderedRanges.addLast(range);
            } else {
                final var last  = unionOrderedRanges.removeLast();
                final var union = TimeRange.Operator.union(range, last);
                unionOrderedRanges.addLast(union.getLeft());
                if(!Objects.equals(union.getRight(), TimeRange.Operator.EMPTY)) {
                    unionOrderedRanges.addLast(union.getRight());
                }
            }
        });
        return unionOrderedRanges;
    }

    private Set<TimeRange> intersectRanges(List<TimeRange> firstRanges, List<TimeRange> secondRanges) {
        final var intersectionRanges = new HashSet<TimeRange>();
        for(var first : firstRanges) {
            for(var second : secondRanges) {
                final var intersection = TimeRange.Operator.intersection(first, second);
                if(!Objects.equals(intersection.getLeft(), TimeRange.Operator.EMPTY)) {
                    intersectionRanges.add(intersection.getLeft());
                }
                if(!Objects.equals(intersection.getRight(), TimeRange.Operator.EMPTY)) {
                    intersectionRanges.add(intersection.getRight());
                }
            }
        }
        return intersectionRanges;
    }
}
