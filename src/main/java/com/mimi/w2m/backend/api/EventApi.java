package com.mimi.w2m.backend.api;

import com.mimi.w2m.backend.api.dto.EventCreateDto;
import com.mimi.w2m.backend.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @since 2022-11-05
 * @author yeh35
 */

@Tag(name = "Event", description = "이벤트 처리와 관련된 API")
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController
public class EventApi {

    
    @Operation(description = "[인증] 이벤트 생성")
    @PostMapping("")
    public ApiResponse<EventCreateDto.Response> createEvent(
        @RequestBody EventCreateDto.Request request
    ) {

        return ApiResponse.ofSuccess(null);
    } 

}
