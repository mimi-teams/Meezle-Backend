package com.mimi.w2m.backend.repository.vo;

import com.mimi.w2m.backend.domain.EventParticipantAbleTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 이벤트 참여자 정보, Query용 VO
 * @since 2022-12-18
 * @author yeh35
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipantQueryVo {

    private long id;

    @Nullable
    private String userName;

    @Nullable
    private String guestName;

    private EventParticipantAbleTime participantAbleTime;

}
