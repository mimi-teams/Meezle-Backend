package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.EventParticipant;
import com.mimi.w2m.backend.domain.EventParticipantAbleTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @since 2022-12-17
 * @author yeh35
 */
public interface EventParticipantAbleTimeRepository extends JpaRepository<EventParticipantAbleTime, Long> {

    /**
     * 이벤트에 해당되는 모든 선택 가능한 시간을 가져온다.
     */
    List<EventParticipantAbleTime> findByEventParticipant(EventParticipant event);

    /**
     * 이벤트에 해당되는 모든 선택 가능한 시간을 삭제한다.
     */
    Long deleteByEventParticipant(EventParticipant event);

}
