package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * EventRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/

@Data
@Builder
@Schema(description = "Event를 생성할 때, 전달하는 정보")
public class EventRequestDto {

private final String title;
private final Long   userId;

@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
@Schema(description = "종료일")
private final LocalDateTime dDay;

@Schema(description = "Event 색상 정보")
private final ColorDto color;

@Schema(description = "1000자까지 허용")
private final String description;

public EventRequestDto(String title, Long userId, LocalDateTime dDay, ColorDto color, String description) {
    this.title       = title;
    this.userId      = userId;
    this.dDay = Objects.nonNull(dDay) ? dDay : LocalDateTime.of(2099, 12, 31, 23, 59);
    this.color       = color;
    this.description = description;
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