package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.dto.landing.LandingResponseDto;
import com.mimi.w2m.backend.service.EventService;
import com.mimi.w2m.backend.service.GuestService;
import com.mimi.w2m.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * LandingApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/18
 **/
@Tag(name = "Landing Page Api", description = "Landing Page 와 관련된 API")
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/v1/landing")
@RestController
public class LandingApi {
    private final Logger logger = LoggerFactory.getLogger(LandingApi.class.getName());
    private final EventService eventService;
    private final GuestService guestService;
    private final UserService userService;
    private final CacheManager cacheManager;

    @Operation(summary = "개설된 이벤트와 참여자 수 반환", description = "[인증X] 현재 개설된 이벤트 및 이벤트에 참여한 인원의 총합을 반환한다")
    @GetMapping(path = "")
    public @Valid ApiCallResponse<LandingResponseDto> getInfo() {
        final var numEvents = eventService.getTotal();
        final var numGuests = guestService.getTotal();
        final var numUsers = userService.getTotal();
        return ApiCallResponse.ofSuccess(LandingResponseDto.builder()
                .eventCount(numEvents)
                .userCount(numUsers + numGuests)
                .build());
    }
}