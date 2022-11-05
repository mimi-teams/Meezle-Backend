package com.mimi.w2m.backend.api.dto;

import com.mimi.w2m.backend.dto.event.EventDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EventCreateDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private EventDto event;
    }

    @Getter
    @Builder
    public static class Response {
        private final EventDto event;
    }
}
