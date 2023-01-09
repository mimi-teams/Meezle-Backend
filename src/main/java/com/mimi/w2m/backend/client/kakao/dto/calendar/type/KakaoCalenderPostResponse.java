package com.mimi.w2m.backend.client.kakao.dto.calendar.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KakaoCalenderPostResponse
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/09
 **/
public record KakaoCalenderPostResponse(
        @JsonProperty("calendar_id") String id
) {
}