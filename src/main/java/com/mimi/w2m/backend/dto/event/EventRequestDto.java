package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * EventRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
// TODO: 2022/11/27 설정 가능한 제한 시간 설정 
@Getter
@Schema(description = "이벤트 정보", requiredProperties = {"title", "color", "possibleRangeOfDays", "possibleRangeOfTimes"})
public class EventRequestDto implements Serializable {
    @Schema(type = "String", minLength = 1, maxLength = 200, description = "이벤트 이름")
    private String         title;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime  dDay;
    @Schema(description = "참여자가 입력 가능한 요일")
    private Set<DayOfWeek> possibleRangeOfDays;

    private ParticipleTime possibleRangeOfTimes;

    private ColorDto color;

    @Schema(type = "String", maxLength = 1000, description = "이벤트 세부 설명", nullable = true)
    private String description;

    public EventRequestDto(String title, LocalDateTime dDay, ColorDto color, String description) {
        this.title       = title;
        this.dDay        = dDay;
        this.color       = color;
        this.description = description;
    }

    protected EventRequestDto() {
    }

    public Event to(User user) {
        return Event.builder()
                    .title(title)
                    .user(user)
                    .dDay(dDay)
                    .color(color.to())
                    .description(description)
                    .build();
    }

}