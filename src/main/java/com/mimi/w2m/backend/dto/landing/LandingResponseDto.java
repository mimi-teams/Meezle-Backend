package com.mimi.w2m.backend.dto.landing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

/**
 * LandingResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/18
 **/
@Builder
public record LandingResponseDto(
        @JsonProperty(value = "eventCount")
        Long eventCount,
        @JsonProperty(value = "userCount")
        Long userCount
) implements Serializable {
}
