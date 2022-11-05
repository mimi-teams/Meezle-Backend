package com.mimi.w2m.backend.api;

import com.mimi.w2m.backend.api.dto.EventCreateDto;
import com.mimi.w2m.backend.dto.ApiResponse;
import com.mimi.w2m.backend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @since 2022-11-05
 * @author yeh35
 */

@Tag(name = "Event", description = "이벤트 처리와 관련된 API")
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController
public class EventApi {

    private final EventService eventService;

    @Operation(description = "[인증] 이벤트 생성")
    @PostMapping("")
    public ApiResponse<EventCreateDto.Response> createEvent(
        @RequestBody EventCreateDto.Request request
    ) {

//        eventService.createEvent( 0l,  request.getEvent());

        return ApiResponse.ofSuccess(null);
    }

    @Operation(description = "[인증] 이벤트 수정")
    @PatchMapping("/{eventId}")
    public ApiResponse<EventCreateDto.Response> modifyEvent(
            @RequestBody EventCreateDto.Request request
    ) {

        return ApiResponse.ofSuccess(null);
    }

    @Operation(description = "[인증] 이벤트 삭제")
    @DeleteMapping("/{eventId}")
    public ApiResponse<EventCreateDto.Response> deleteEvent(
            @RequestBody EventCreateDto.Request request
    ) {

        return ApiResponse.ofSuccess(null);
    }

    @Operation(description = "[인증] 이벤트 조회")
    @GetMapping("")
    public ApiResponse<EventCreateDto.Response> getEvents(
            @RequestBody EventCreateDto.Request request
    ) {

        return ApiResponse.ofSuccess(null);
    }

    @Operation(description = "[인증] 이벤트 상세 조회")
    @GetMapping("/{eventId}")
    public ApiResponse<EventCreateDto.Response> getEvent() {

        return ApiResponse.ofSuccess(null);
    }
}
