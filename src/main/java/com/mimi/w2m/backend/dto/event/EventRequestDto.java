package com.mimi.w2m.backend.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * EventRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema(title = "Event 에 대한 요청 정보", description = "이벤트 생성이나 업데이트에 필요한 정보를 받음", requiredProperties = {"title", "color"})
public class EventRequestDto implements Serializable {
    @Schema(title = "Event 의 제목", type = "String", minLength = 1, maxLength = 50, description = "이벤트 이름")
    @Size(min = 1, max = 50)
    @NotNull
    private String title;

    @Schema(title = "참여자들이 선택 가능한 시간", description = "이벤트 참여자들이 선택할 수 있는 시간의 범위를 지정")
    @NotNull
    @Valid
    private SelectableParticipleTimeDto selectableParticipleTimes;

    // TODO: 2023/02/11 이상한 오류. Swagger의 명명규칙과 Jackson으로 직렬화된 실제 이름이 충돌하는 것 같다.
    /**
     * dDay <- 직렬화 단계에서 "dDay" 로 바뀌지만, Swagger 에서는 dday 와 dDay가 모두 표시된다.
     * dday <- 직렬화 단계에서 "dday" 로 바뀌며, Swagger 역시 정상 동작한다.
     * @JsonProperty("dDay") dday <- 직렬화 단계에서 "dDay"로 바뀌며, Swagger 역시 정상 동작한다.
     * dDay 를 Swagger가 dday 로 바꿔버린다. 실제 필드 값은 dDay!
     */
    @Schema(type = "string", title = "시간 투표 종료일", description = "이벤트 참여자들이 이벤트 시간을 확정하기 위한 투표를 할 수 있는 마지막 시간을 지정",
            example = "2022-12-01T00:00:00")
    // JUnit Test 에서 Mock 으로 Api 를 호출할 때, JsonFormat 으로 지정해야 LocalDateTime 값이 저장된다.
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Nullable
    private LocalDateTime dday;

    @Valid
    private ColorDto color;

    @Schema(type = "String", maxLength = 1000, description = "이벤트 세부 설명", nullable = true)
    @Nullable
    @Size(max = 1000)
    private String description;

    @SuppressWarnings("unused")
    protected EventRequestDto() {
    }

    @SuppressWarnings("unused")
    @Builder
    public EventRequestDto(String title,
                           @Nullable SelectableParticipleTimeDto selectableParticipleTimes,
                           @Nullable LocalDateTime dday, ColorDto color,
                           @Nullable String description) {
        this.title = title;
        this.selectableParticipleTimes = selectableParticipleTimes;
        this.dday = dday;
        this.color = color;
        this.description = description;
    }

    public Event to(User user) {
        return Event.builder()
                .title(title)
                .host(user)
                .dday(dday)
                .color(color)
                .description(description)
                .build();
    }

}