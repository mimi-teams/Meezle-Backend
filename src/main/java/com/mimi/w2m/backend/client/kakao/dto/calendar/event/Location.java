package com.mimi.w2m.backend.client.kakao.dto.calendar.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Size;

/**
 * Location
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/10
 **/
public record Location(
        @Size(min = 1, max = 100)
        @JsonProperty(value = "name")
        String name,
        @JsonProperty(value = "location_id")
        Integer locationId,
        @JsonProperty(value = "address")
        String address,
        @JsonProperty(value = "latitude")
        Double latitude,
        @JsonProperty(value = "longitude")
        Double longitude
) {
}
