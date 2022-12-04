package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.service.*;
import com.mimi.w2m.backend.common.Role;
import com.mimi.w2m.backend.dto.event.EventRequestDto;
import com.mimi.w2m.backend.dto.event.EventResponseDto;
import com.mimi.w2m.backend.dto.participant.EventParticipantRequestDto;
import com.mimi.w2m.backend.dto.participant.EventParticipantResponseDto;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.exception.InvalidValueException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * EventApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/27
 **/
@Tag(name = "Event Api", description = "Event 와 관련된 Api")
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/v1/events")
@RestController
public class EventApi {
    private final Logger logger = LoggerFactory.getLogger(EventApi.class.getName());
    private final UserService userService;
    private final GuestService guestService;
    private final EventParticipantService eventParticipantService;

    private final EventService eventService;
    private final AuthService authService;
    private final HttpSession httpSession;

    @Operation(method = "GET",
            summary = "이벤트 정보 반환",
            description = "[로그인 O, 인가 O] ID에 해당하는 이벤트 정보를 반환한다. 이벤트 참여자만 이용할 수 있다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @GetMapping(path = "/{id}")
    public @Valid ApiCallResponse<EventResponseDto> get(
            @Parameter(name = "id", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isInEvent(loginInfo, id);
        final var event = eventService.get(id);
        return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
    }

    @Operation(method = "POST",
            summary = "새로운 이벤트 등록",
            description = "[로그인 O, 인가 O] 새로운 이벤트를 등록한다. 가입한 이용자만 이용할 수 있다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @PostMapping(path = "")
    public @Valid ApiCallResponse<EventResponseDto> post(
            @Valid @RequestBody EventRequestDto requestDto
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isValidLogin(loginInfo, loginInfo.loginId(), Role.USER);
        final var event = eventService.createEvent(loginInfo.loginId(), requestDto);
        return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
    }

    @Operation(method = "PATCH",
            summary = "이벤트 정보 수정",
            description = "[로그인 O, 인가 O] ID에 해당하는 이벤트 정보를 수정한다. 이벤트 생성자만 수정 가능한다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @PatchMapping(path = "/{id}")
    public @Valid ApiCallResponse<EventResponseDto> patch(
            @Parameter(name = "id", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id,
            @Valid @RequestBody EventRequestDto requestDto
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isHost(loginInfo, id);
        final var event = eventService.modifyEvent(id, requestDto);
        return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
    }

    @Operation(method = "DELETE",
            summary = "이벤트 삭제",
            description = "[로그인 O, 인가 O] ID에 해당하는 이벤트를 삭제한다. 이벤트 생성자만 가능하며, 삭제 시 Event 와 연관된 정보가 모두 삭제된다. 삭제 후, " +
                    "'/'로 Redirect 된다",
            responses = {@ApiResponse(description = "'/'로 Redirect",
                    content = {@Content(schema = @Schema(description = "GET '/'"))})})
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(
            @Parameter(name = "id", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isHost(loginInfo, id);

        eventParticipantService.deleteAll(eventParticipantService.getAll(id));
        guestService.deleteAll(guestService.getAllInEvent(id));
        eventService.deleteReal(id);

        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(method = "GET",
            summary = "이벤트 생성자 가져오기",
            description = "[로그인 O, 인가 O] 이벤트의 생성자를 반환한다. 이벤트에 속한 참여자만 이용 가능하다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @GetMapping(path = "/{id}/host")
    public @Valid ApiCallResponse<UserResponseDto> getHost(
            @Parameter(name = "id", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isInEvent(loginInfo, id);
        final var event = eventService.get(id);
        return ApiCallResponse.ofSuccess(UserResponseDto.of(event.getHost()));
    }

    @Operation(method = "GET",
            summary = "모든 참여자 가져오기",
            description = "[로그인 O, 인가 O] 이벤트에 속한 모든 참여자를 반환한다. 이벤트에 속한 참여자만 이용 가능한다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @GetMapping(path = "/{id}/participants")
    public @Valid ApiCallResponse<List<EventParticipantResponseDto>> getParticipants(
            @Parameter(name = "id", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isInEvent(loginInfo, id);
        final var participants = eventParticipantService.getAll(id)
                .stream()
                .map(EventParticipantResponseDto::of)
                .toList();
        return ApiCallResponse.ofSuccess(participants);
    }

    @Operation(method = "GET",
            summary = "참여자 가져오기",
            description = "[로그인 O, 인가 O] 이벤트에 속한 ID에 해당하는 참여자를 반환한다. 이벤트에 속한 참여자만 이용 가능한다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @GetMapping(path = "/{eventId}/participants/{id}")
    public @Valid ApiCallResponse<EventParticipantResponseDto> getParticipant(
            @Parameter(name = "eventId", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("eventId") Long eventId,
            @Parameter(name = "id", description = "참여자의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isInEvent(loginInfo, eventId);
        final var participant = eventParticipantService.get(id);
        return ApiCallResponse.ofSuccess(EventParticipantResponseDto.of(participant));
    }

    @Operation(method = "POST",
            summary = "이벤트에 참여자 등록하기",
            description = "[로그인 O, 인가 O] 이벤트에 참여자를 등록시칸다. 이벤트 생성자만 가능하다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @PostMapping(path = "/{id}/participants")
    public @Valid ApiCallResponse<EventParticipantResponseDto> joinEvent(
            @Parameter(name = "id", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id,
            @Valid @RequestBody EventParticipantRequestDto requestDto
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isHost(loginInfo, id);
        final var participant = eventParticipantService.create(requestDto);
        return ApiCallResponse.ofSuccess(EventParticipantResponseDto.of(participant));
    }

    @Operation(method = "PATCH",
            summary = "이벤트 참여자 정보 수정하기",
            description = "[로그인 O, 인가 O] 이벤트 참여자 정보를 수정한다. 본인만 수정 가능한다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @PatchMapping(path = "/{eventId}/participants/{id}")
    public @Valid ApiCallResponse<EventParticipantResponseDto> modifyParticipant(
            @Parameter(name = "eventId", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("eventId") Long eventId,
            @Parameter(name = "id", description = "참여자의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id,
            @Valid @RequestBody EventParticipantRequestDto requestDto
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isInEvent(loginInfo, eventId);
        final var participant = eventParticipantService.modify(id, requestDto);
        return ApiCallResponse.ofSuccess(EventParticipantResponseDto.of(participant));
    }

    @Operation(method = "PATCH",
            summary = "이벤트 참가 시간 설정하기",
            description = "[로그인 O, 인가 O] 이벤트 생성자만 이용 가능하다. mode=[calculate|modify]가 있으며, Query 로 설정한다. " +
                    "(mode=calculate) 이벤트의 참여 시간을 계산한다. 모든 참여자가 참여 가능한 시간이 계산되며, 아무 시간도 선택하지 않은 참여자는 제외된다. " +
                    "(mode=modify) 이벤트의 참여 시간을 직접 수정한다. Request Body 에 해당 정보를 담아 보낸다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @PatchMapping(path = "/{id}/time")
    public @Valid ApiCallResponse<EventResponseDto> calculateTime(
            @Parameter(name = "id", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id,
            @Parameter(name = "mode", description = "시간 설정 모드", in = ParameterIn.QUERY, required = true)
            @NotNull @Pattern(regexp = "^(calculate|modify)$") @Valid @RequestParam String mode,
            @RequestBody @Nullable @Valid EventParticipantRequestDto requestDto
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isHost(loginInfo, id);
        if (Objects.equals(mode, "calculate")) {
            final var event = eventParticipantService.calculateSharedTime(id);
            return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
        } else if (Objects.equals(mode, "modify")) {
            if (Objects.nonNull(requestDto)) {
                final var event = eventService.modifySelectedDaysAndTimesDirectly(requestDto);
                return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
            } else {
                throw new InvalidValueException("[EventApi] Request body 가 없습니다");
            }
        } else {
            throw new InvalidValueException("[EventApi] 잘못된 mode 의 요청입니다 : " + mode);
        }
    }
}