package com.mimi.w2m.backend.dto.participant;

import com.mimi.w2m.backend.domain.type.ParticipleTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@SuppressWarnings("FieldMayBeFinal")
@Getter
@Schema(name = "이벤트 참여자")
public class EventParticipantDto {

    @Schema(description = "참여자 번호")
    private Long id;
    @Schema(description = "이름")
    private String name;

    @Schema(title = "참여자가 선택한 시간", description = "참여자가 선택한 시간 정보를 받음")
    private Set<ParticipleTime> ableDaysAndTimes;

    @Builder
    public EventParticipantDto(Long id, String name, Set<ParticipleTime> ableDaysAndTimes) {
        this.id = id;
        this.name = name;
        this.ableDaysAndTimes = ableDaysAndTimes;
    }
}
