package com.mimi.w2m.backend.dto.participant.guest;

import com.mimi.w2m.backend.domain.Guest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Guest Entity 와 1 대 1 매칭된다.
 * @since 2022-12-17
 * @author yeh35
 */
@Getter
@Schema(title = "이벤트 게스트", description = "이용자의 ID 와 Name, 연관된 이벤트 ID를 보냄")
public class GuestDto implements Serializable {
    @Schema(description = "이용자 ID")
    @NotNull
    @PositiveOrZero
    private Long id;
    @Schema(description = "이용자가 속한 Event 의 ID")
    @NotNull
    @PositiveOrZero
    private Long eventId;
    @Schema(description = "이용자 이름", maxLength = 20)
    @Size(min = 1, max = 20)
    @NotNull
    private String name;

    @Builder
    public GuestDto(Long id, Long eventId, String name) {
        this.id = id;
        this.eventId = eventId;
        this.name = name;
    }

    protected GuestDto() {
    }

    public static GuestDto of(Guest entity) {
        return GuestDto.builder()
                .id(entity.getId())
                .eventId(entity.getEvent()
                        .getId())
                .name(entity.getName())
                .build();
    }

}