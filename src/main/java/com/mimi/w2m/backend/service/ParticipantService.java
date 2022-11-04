package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 참여자 생성
     *
     * @author yeh35
     * @since 2022-11-01
     */
    @Transactional
    public Long createParticipant(String name, String password) throws DuplicateKeyException {
        participantRepository.findByName(name).ifPresent(entity -> {
            throw new DuplicateKeyException(name);
        });
        var participant = Participant.builder()
                .name(name).
                password(password)
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


}
