package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * EventRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/

@Getter
@Schema(description = "Event를 생성할 때, 전달하는 정보")
public class EventRequestDto implements Serializable {

private String        title;
@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
@Schema(description = "종료일")
private LocalDateTime dDay;

@Schema(description = "Event 색상 정보")
private ColorDto color;

@Schema(description = "1000자까지 허용")
private String description;

@Builder
public EventRequestDto(String title, LocalDateTime dDay, ColorDto color, String description) {
    this.title       = title;
    this.dDay        = Objects.nonNull(dDay) ? dDay : LocalDateTime.of(2099, 12, 31, 23, 59);
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