package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 이벤트 처리를 책임지는 서비스
 *
 * @since 2022-10-31
 * @author yeh35
 */

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventService {

    private final EventRepository eventRepository;


    /**
     * 이벤트 생성
     *
     * @since 2022-10-31
     * @author yeh35
     */
    @Transactional
    public void createEvent() {
        
    }
    
    /**
     * 이벤트 수정
     *
     * @since 2022-10-31
     * @author yeh35
     */
    @Transactional
    public void modifyEvent() {
        //TODO 이벤트 참여자가 등록하기 전까지는 수정 가능
    }
        
    /**
     * 이벤트 조회,
     *
     * @since 2022-10-31
     * @author yeh35
     */
    public void getEvents() {

    }

    /**
     * Event를 수정할 권리가 있는지 확인한다.
     *
     * @since 2022-10-31
     * @author yeh35
     */
    public void checkEventModifiable() {

    }

}
