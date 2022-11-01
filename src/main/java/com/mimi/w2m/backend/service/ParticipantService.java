package com.mimi.w2m.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Participant를 관리하는 서비스
 *
 * @since 2022-11-01
 * @author yeh35
 */

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ParticipantService {

    /**
     * 참여자 생성
     *
     * @since 2022-11-01
     * @author yeh35
     */
    public void createParticipant() {

    }

    /**
     * 참여자 가져오기
     *
     * @since 2022-11-01
     * @author yeh35
     */
    public void getParticipant(Long participantId) {

    }


}
