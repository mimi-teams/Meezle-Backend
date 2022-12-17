package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @since 2022-12-17
 * @author yeh35
 */
public interface EventSelectableParticipleTimeRepository extends JpaRepository<EventSelectableParticipleTime, Long> {

    /**
     * 이벤트에 해당되는 모든 선택 가능한 시간을 가져온다.
     */
    List<EventSelectableParticipleTime> findAllByEvent(Event event);

}
