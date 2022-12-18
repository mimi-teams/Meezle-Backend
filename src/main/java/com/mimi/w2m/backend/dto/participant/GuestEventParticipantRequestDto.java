package com.mimi.w2m.backend.dto.participant;

import com.mimi.w2m.backend.domain.type.ParticipleTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.Set;

/**
 * 게스트 이벤트 참여하기
 * 
 * @since 2022-12-17
 * @author yeh35
 */
@Getter
@Schema(title = "게스트가 이벤트 참여요청", description = "")
public class GuestEventParticipantRequestDto implements Serializable {
    @Schema(title = "Event 의 ID")
    @NotNull
    @PositiveOrZero
    private Long eventId;

    @Schema(title = "게스트 이름")
    @NotNull
    @NotBlank
    private String guestName;

    @Schema(title = "게스트 비밀번호")
    @NotNull
    @NotBlank
    private String guestPassword;

    @Schema(title = "참여자가 선택한 시간", description = "참여자가 선택한 시간 정보를 받음(null = 모든 선택 가능한 시간이 가능하다고 가정)")
    @Nullable
    @Valid
    private Set<ParticipleTime> ableDaysAndTimes;


    @SuppressWarnings("unused")
    protected GuestEventParticipantRequestDto() {
    }

    @SuppressWarnings("unused")
    @Builder
    public GuestEventParticipantRequestDto(
            Long eventId,
            String guestName,
            String guestPassword,
            @Nullable Set<ParticipleTime> ableDaysAndTimes
    ) {
        this.eventId = eventId;
        this.guestName = guestName;
        this.guestPassword = guestPassword;
        this.ableDaysAndTimes = ableDaysAndTimes;
    }

}