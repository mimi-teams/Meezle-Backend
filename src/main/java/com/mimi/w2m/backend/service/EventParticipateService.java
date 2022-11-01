package com.mimi.w2m.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 이벤트 참여를 책임지는 서비스
 *
 * @since 2022-11-01
 * @author yeh35
 */

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventParticipateService {

    private final ParticipantService participantService;

    /**
     * 이벤트 참여
     * 참여 시간을 입력하는데, 기존에 참여 시간은 지우고, 전부 다시 Insert한다.
     *
     * @since 2022-11-01
     * @author yeh35
     */
    @Transactional
    public void updateParticipatableTime() {
        //TODO 참여자가 처음인 경우 participantService.createParticipant()
        //기존이 참여한 이벤트 참여자의 경우 참여 가능한 시간 Delete -> Insert
    }

    /**
     * 특정 이벤트 참여 가능한 시간 조회
     *
     * @since 2022-10-31
     * @author yeh35
     */
    public void getEventsParticipate() {


    }

    /**
     * 특정 이벤트 참여자가 입력한 참여 가능한 시간
     *
     * @since 2022-11-01
     * @author yeh35
     */
    public void getEventsParticipate(Long participant) {

    }

}
