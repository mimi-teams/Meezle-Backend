package com.mimi.w2m.backend.type.dto.event;

import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.domain.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * EventResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema(title = "Event 에 대한 반환 정보", description = "이벤트와 관련된 정보를 반환",
        requiredProperties = {"id", "hostId", "title", "dDay", "selectableParticipleTime", "selectedParticipleTime",
                              "color", "description"})
public class EventResponseDto implements Serializable {

    @Schema(title = "Event 의 ID", type = "Integer")
    @NotNull
    @PositiveOrZero
    private Long   id;
    @Schema(title = "Event 의 Host ID", type = "Integer", description = "이벤트 생성자의 ID")
    @NotNull
    @PositiveOrZero
    private Long   hostId;
    @Schema(title = "Event 의 제목", type = "String", minLength = 1, maxLength = 50, description = "이벤트 이름")
    @Size(min = 1, max = 50)
    @NotNull
    private String title;

    @Schema(title = "시간 투표 종료일", description = "이벤트 참여자들이 이벤트 시간을 확정하기 위한 투표를 할 수 있는 마지막 시간을 반환",
            example = "2022-12-01T00:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Nullable
    private LocalDateTime       dDay;
    @Schema(title = "참여자들이 선택 가능한 시간", description = "이벤트 참여자들이 선택할 수 있는 시간의 범위를 반환")
    @Nullable
    @Valid
    private Set<ParticipleTime> selectableParticipleTimes;

    @Schema(title = "이벤트의 선택된 시간", description = "이벤트의 선택된 시간의 범위를 반환")
    @Nullable
    @Valid
    private Set<ParticipleTime> selectedParticipleTimes;

    @Valid
    private ColorDto color;
    @Schema(type = "String", maxLength = 1000, description = "이벤트 세부 설명", nullable = true)
    @Nullable
    @Size(max = 1000)
    private String   description;

    protected EventResponseDto() {
    }

    @Builder
    public EventResponseDto(Long id, Long hostId, String title,
                            @Nullable LocalDateTime dDay,
                            @Nullable Set<ParticipleTime> selectableParticipleTimes,
                            @Nullable Set<ParticipleTime> selectedParticipleTimes, ColorDto color,
                            @Nullable String description) {
        this.id                        = id;
        this.hostId                    = hostId;
        this.title                     = title;
        this.dDay                      = dDay;
        this.selectableParticipleTimes = selectableParticipleTimes;
        this.selectedParticipleTimes   = selectedParticipleTimes;
        this.color                     = color;
        this.description               = description;
    }

    public static EventResponseDto of(Event entity) {
        return EventResponseDto.builder()
                               .id(entity.getId())
                               .hostId(entity.getHost()
                                             .getId())
                               .title(entity.getTitle())
                               .dDay(entity.getDDay())
                               .selectableParticipleTimes(entity.getSelectableDaysAndTimes())
                               .selectedParticipleTimes(entity.getSelectedDaysAndTimes())
                               .color(ColorDto.of(entity.getColor()))
                               .description(entity.getDescription())
                               .build();
    }
}