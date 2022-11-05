package com.mimi.w2m.backend.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Schema
public class EventDto {

    private final Long id;
    private final String title;

}
