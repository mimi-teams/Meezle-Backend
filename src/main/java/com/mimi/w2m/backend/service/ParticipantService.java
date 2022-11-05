package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Participant를 관리하는 서비스
 *
 * @author yeh35
 * @since 2022-11-01
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    /**
     * 참여자 생성
     *
     * @author yeh35
     * @since 2022-11-01
     */
    @Transactional
    public Long createParticipant(Long eventId, String name, String password) throws DuplicateKeyException, NoSuchElementException {
        participantRepository.findByName(name).ifPresent(entity -> {
            throw new DuplicateKeyException(name);
        });
        var event = eventRepository.findById(eventId).orElseThrow();
        var participant = Participant.builder()
                .event(event)
                .name(name)
                .password(password)
                .role(Role.PARTICIPANT)
                .build();
        return participantRepository.save(participant).getId();
    }

    /**
     * 참여자 가져오기
     *
     * @author yeh35
     * @since 2022-11-01
     */
    public Participant getParticipant(Long participantId) throws NoSuchElementException {
        var participant = participantRepository.findById(participantId).orElseThrow();
        return participant;
    }

    public List<Participant> getAllParticipantInEvent(Long eventId) throws NoSuchElementException {
        var event = eventRepository.findById(eventId).orElseThrow();
        return participantRepository.findAllByEvent(event);
    }
}
