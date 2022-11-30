package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.repository.EventParticipantRepository;
import com.mimi.w2m.backend.repository.GuestRepository;
import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.domain.Event;
import com.mimi.w2m.backend.type.domain.EventParticipant;
import com.mimi.w2m.backend.type.dto.participant.EventParticipantRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final EventService               eventService;
    private final GuestRepository            guestRepository;
    private final EventParticipantRepository eventParticipantRepository;

    /**
     * 이벤트 참여 참여 시간을 입력하는데, 기존에 참여 시간은 지우고, 전부 다시 Insert한다.
     *
     * @author yeh35
     * @since 2022-11-01
     */
    @Transactional
    public EventParticipant createOrUpdate(Long eventId, EventParticipantRequestDto requestDto, Long ownerId,
                                           Role role) throws InvalidValueException, EntityNotFoundException {
        final var event = eventService.getEvent(eventId);
        return switch(role) {
            case USER -> {
                final var user        = userService.getUser(ownerId);
                final var participant = eventParticipantRepository.findByUserInEvent(user, event);
                yield participant.isPresent() ? participant.get()
                                                           .update(requestDto.getAbleDayOfWeeks(),
                                                                   requestDto.getParticipleTimes())
                                              : eventParticipantRepository.save(requestDto.to(event, user));

            }
            case GUEST -> {
                final var guest = guestRepository.findById(ownerId)
                                                 .orElseThrow(
                                                         () -> new EntityNotFoundException("존재하지 않는 참여자 : " + ownerId,
                                                                                           "존재하지 않는 참여자"));
                final var participant = eventParticipantRepository.findByGuestInEvent(guest, event);
                yield participant.isPresent() ? participant.get()
                                                           .update(requestDto.getAbleDayOfWeeks(),
                                                                   requestDto.getParticipleTimes())
                                              : eventParticipantRepository.save(requestDto.to(event, guest));

            }
            case NONE -> {
                throw new InvalidValueException("유효하지 않은 사용자 : " + role.getKey(), "유효하지 않은 사용자");
            }
        };
    }

    /**
     * 특정 이벤트 참여자가 입력한 참여 가능한 시간
     *
     * @author yeh35
     * @since 2022-11-01
     */
    public EventParticipant get(Long eventId, Long id, Role role)
    throws EntityNotFoundException, InvalidValueException {
        final var event = eventService.getEvent(eventId);
        return switch(role) {
            case USER -> {
                final var user = userService.getUser(id);
                yield eventParticipantRepository.findByUserInEvent(user, event)
                                                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자",
                                                                                               "존재하지 않는 참여자"));

            }
            case GUEST -> {
                var guest = guestRepository.findById(id)
                                           .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자 : " + id,
                                                                                          "존재하지 않는 참여자"));
                yield eventParticipantRepository.findByGuestInEvent(guest, event)
                                                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자",
                                                                                               "존재하지 않는 참여자"));
            }
            case NONE -> throw new InvalidValueException("유효하지 않은 사용자 : " + role.getKey(), "유효하지 않은 사용자");
        };
    }

    public void deleteAll(List<EventParticipant> eventParticipants) {
        eventParticipantRepository.deleteAll(eventParticipants);
    }

    /**
     * eventParticipleTime 에 저장된 각 참여자의 가능한 시간의 공통 부분을 계산하여 dayOfWeeks, begin & end time 에 저장한다
     *
     * @author teddy
     * @since 2022/11/20
     **/
    @Transactional
    public Event calculateSharedTime(Long eventId) throws EntityNotFoundException, InvalidValueException {
        var event        = eventService.getEvent(eventId);
        var participants = getAll(eventId);

        final var dayOfWeeks      = event.getSelectableDayOfWeeks();
        final var participleTimes = event.getSelectableParticipleTimes();
        final var sharedCount = new java.util.ArrayList<>(dayOfWeeks.stream()
                                                               .map(day -> 0)
                                                               .toList());

        participants.forEach(p -> {
            final var possibleDays  = p.getAbleDayOfWeeks();
            final var possibleTimes = p.getAbleTimes();
            try {
                for(int i = 0; i < possibleDays.size(); i++) {
                    final var day   = possibleDays.get(i);
                    final var index = day.getValue() - 1;
                    sharedCount.set(index, sharedCount.get(index) + 1);
                    final var storedTime   = participleTimes.get(index);
                    final var possibleTime = possibleTimes.get(i);
                    final var updatedBegTime = possibleTime.beginTime()
                                                           .isAfter(storedTime.beginTime()) ? possibleTime.beginTime()
                                                                                            : storedTime.beginTime();
                    final var updatedEndTime = possibleTime.endTime()
                                                           .isBefore(storedTime.endTime()) ? possibleTime.endTime()
                                                                                           : storedTime.endTime();
                    participleTimes.set(index, new ParticipleTime(updatedBegTime, updatedEndTime));
                }
            } catch(IndexOutOfBoundsException e) {
                throw new InvalidValueException("선택 가능한 범위를 초과했습니다", "선택 가능한 범위를 초과했습니다");
            }
        });
        final var sharedDays  = new ArrayList<DayOfWeek>();
        final var sharedTimes = new ArrayList<ParticipleTime>();
        for(int i = 0; i < sharedCount.size(); i++) {
            if(Objects.equals(sharedCount.get(i), participants.size())) {
                sharedDays.add(dayOfWeeks.get(i));
                sharedTimes.add(participleTimes.get(i));
            }
        }

        return event.update(sharedDays, sharedTimes);
    }

    /**
     * 특정 이벤트 참여 가능한 모든 시간 조회(등록된 모든 것)
     *
     * @author yeh35
     * @since 2022-10-31
     */
    public List<EventParticipant> getAll(Long eventId) throws EntityNotFoundException {
        var event = eventService.getEvent(eventId);
        return eventParticipantRepository.findAllInEvent(event);
    }
}
