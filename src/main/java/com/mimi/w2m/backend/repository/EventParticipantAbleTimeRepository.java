package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipant;
import com.mimi.w2m.backend.domain.EventParticipantAbleTime;
import com.mimi.w2m.backend.repository.vo.EventParticipantQueryVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @since 2022-12-17
 * @author yeh35
 */
public interface EventParticipantAbleTimeRepository extends JpaRepository<EventParticipantAbleTime, Long> {

    /**
     * 이벤트에 해당되는 모든 선택 가능한 시간을 가져온다.
     */
    @Query("SELECT new com.mimi.w2m.backend.repository.vo.EventParticipantQueryVo(e.id, u.name, g.name, t)" +
            "FROM EventParticipantAbleTime t " +
            "INNER JOIN EventParticipant e ON e = t.eventParticipant " +
            "LEFT OUTER JOIN User u ON u = e.user " +
            "LEFT OUTER JOIN Guest g ON g = e.guest " +
            "WHERE e.event = :event")
    List<EventParticipantQueryVo> findByEvent(@Param("event") Event event);

//    @Query("SELECT e.id, e.user.name, t " +
//            "FROM EventParticipantAbleTime t " +
//            "INNER JOIN EventParticipant e ON e = t.eventParticipant " +
//            "WHERE e.event = :event AND e.user is not null")
//    List<EventParticipantQueryVo> findUserByEvent(@Param("event") Event event);

    /**
     * 이벤트에 해당되는 모든 선택 가능한 시간을 삭제한다.
     */
    @Modifying
    @Query("DELETE FROM EventParticipantAbleTime t WHERE t.eventParticipant = :participant")
    void deleteByEventParticipant(@Param("participant") EventParticipant participant);

    @Modifying
    @Query("DELETE FROM EventParticipantAbleTime t WHERE t.eventParticipant in :participantList")
    void deleteByEventParticipantList(@Param("participantList") List<EventParticipant> participantList);


}
