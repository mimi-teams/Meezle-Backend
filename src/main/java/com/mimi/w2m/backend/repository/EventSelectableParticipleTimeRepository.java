package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @since 2022-12-17
 * @author yeh35
 */
public interface EventSelectableParticipleTimeRepository extends JpaRepository<EventSelectableParticipleTime, UUID> {

    /**
     * 이벤트에 해당되는 모든 선택 가능한 시간을 가져온다.
     */
    List<EventSelectableParticipleTime> findByEvent(Event event);

    /**
     * 이벤트에 해당되는 모든 선택 가능한 시간을 삭제한다.
     */
    Long deleteByEvent(Event event);

}
