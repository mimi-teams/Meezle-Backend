package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.config.interceptor.Auth;
import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventParticipant;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.dto.event.EventGetResponse;
import com.mimi.w2m.backend.dto.event.EventRequestDto;
import com.mimi.w2m.backend.dto.event.EventResponseDto;
import com.mimi.w2m.backend.dto.participant.EventParticipantDto;
import com.mimi.w2m.backend.dto.participant.EventParticipantRequest;
import com.mimi.w2m.backend.dto.participant.EventParticipantRequestDto;
import com.mimi.w2m.backend.dto.participant.guest.GuestCreateDto;
import com.mimi.w2m.backend.dto.participant.guest.GuestLoginRequest;
import com.mimi.w2m.backend.dto.participant.guest.GuestLoginResponse;
import com.mimi.w2m.backend.dto.participant.guest.GuestOneResponseDto;
import com.mimi.w2m.backend.service.AuthService;
import com.mimi.w2m.backend.service.EventParticipantService;
import com.mimi.w2m.backend.service.EventService;
import com.mimi.w2m.backend.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

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
    private final GuestService guestService;
    private final EventParticipantService eventParticipantService;

    private final EventService eventService;
    private final AuthService authService;

    @Operation(summary = "이벤트 정보 반환", description = "ID에 해당하는 이벤트 정보를 반환한다. 이벤트 참여자만 이용할 수 있다")
    @GetMapping("/{eventId}")
    public @Valid ApiCallResponse<EventGetResponse> get(
            @Parameter(name = "eventId", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @NotNull @Valid @PathVariable("eventId") UUID eventId
    ) {
        final var event = eventService.getEvent(eventId);
        final var eventSelectableParticipleTimes = eventService.getEventSelectableParticipleTimes(eventId);
        final List<EventParticipantDto> eventParticipants = eventParticipantService.getEventParticipants(eventId);

        return ApiCallResponse.ofSuccess(EventGetResponse.of(event, eventSelectableParticipleTimes, eventParticipants));
    }

    @Operation(summary = "[인증]이용자가 생성한 모든 이벤트 반환", description = "이용자가 생성한 모든 이벤트 정보를 반환한다. 해당 이용자만 가능하다.")
    @Auth
    @GetMapping("/host")
    public @Valid ApiCallResponse<List<EventGetResponse>> getAllByHost() {
        final var currentUserInfo = authService.getCurrentUserInfo();
        final var eventGetResponses = eventService.getAllByHost(currentUserInfo.userId()).stream()
                .map((Event event) -> {
                    final var eventSelectableParticipleTimes = eventService.getEventSelectableParticipleTimes(event.getId());
                    final var eventParticipants = eventParticipantService.getEventParticipants(event.getId());
                    return EventGetResponse.of(event, eventSelectableParticipleTimes, eventParticipants);
                })
                .toList();
        return ApiCallResponse.ofSuccess(eventGetResponses);
    }

    @Operation(summary = "[인증] 새로운 이벤트 등록", description = "새로운 이벤트를 등록한다. 가입한 이용자만 이용할 수 있다")
    @Auth
    @PostMapping("")
    public @Valid ApiCallResponse<EventResponseDto> createEvent(
            @Valid @RequestBody EventRequestDto requestDto
    ) {
        final var currentUserInfo = authService.getCurrentUserInfo();

        final var event = eventService.createEvent(currentUserInfo.userId(), requestDto);
        final var eventSelectableParticipleTimes = eventService.getEventSelectableParticipleTimes(event.getId());

        return ApiCallResponse.ofSuccess(EventResponseDto.of(event, eventSelectableParticipleTimes));
    }

    @Operation(summary = "[인증] 이벤트 정보 수정", description = "ID에 해당하는 이벤트 정보를 수정한다. 이벤트 생성자만 수정 가능한다")
    @Auth
    @PatchMapping(path = "/{eventId}")
    public @Valid ApiCallResponse<EventResponseDto> patch(
            @Parameter(name = "eventId", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @NotNull @Valid @PathVariable("eventId") UUID eventId,
            @Valid @RequestBody EventRequestDto requestDto
    ) {
        final var currentUserInfo = authService.getCurrentUserInfo();
        authService.isHost(currentUserInfo.userId(), eventId);

        final var event = eventService.modifyEvent(eventId, requestDto);
        final var eventSelectableParticipleTimes = eventService.getEventSelectableParticipleTimes(eventId);

        return ApiCallResponse.ofSuccess(EventResponseDto.of(event, eventSelectableParticipleTimes));
    }

    @Operation(summary = "[인증]이벤트 삭제",
            description = "ID에 해당하는 이벤트를 삭제한다. 이벤트 생성자만 가능하며, 삭제 시 Event 와 연관된 정보가 모두 삭제된다. 삭제 후 처리는 클라이언트에서 해주세요")
    @Auth
    @DeleteMapping(path = "/{eventId}")
    public ApiCallResponse<?> delete(
            @Parameter(name = "eventId", description = "이벤트의 ID", in = ParameterIn.PATH, required = true)
            @NotNull @Valid @PathVariable("eventId") UUID eventId
    ) {
        final var currentUserInfo = authService.getCurrentUserInfo();
        authService.isHost(currentUserInfo.userId(), eventId);

        //TODO 한 트렌젝션에서 처리 되도록 수정하자
        eventParticipantService.deleteAboutEvent(eventId);
        eventService.delete(eventId);

        return ApiCallResponse.ofSuccess(null);
    }

    @Operation(summary = "이벤트 게스트 로그인", description = "없는 유저의 경우 자동으로 등록된다.")
    @PostMapping("/{eventId}/guests/login")
    public @Valid ApiCallResponse<GuestLoginResponse> login(
            @Parameter(description = "이벤트의 ID", required = true) @NotNull @Valid @PathVariable UUID eventId,
            @Valid @RequestBody GuestLoginRequest requestBody
    ) {
        if (!guestService.isUsedGuestName(eventId, requestBody.getName())) {
            final var createDto = GuestCreateDto.builder()
                    .eventId(eventId)
                    .name(requestBody.getName())
                    .password(requestBody.getPassword())
                    .build();
            guestService.create(createDto);
        }

        final GuestLoginResponse loginResponse = guestService.login(eventId, requestBody);
        return ApiCallResponse.ofSuccess(loginResponse);
    }

    @Operation(summary = "[인증] 게스트의 이벤트 참여", description = "다시 등록하고 싶으면 이 API를 다시 호출하면 된다. Delete & Insert")
    @Auth(Role.GUEST)
    @PostMapping("/{eventId}/guests/participate")
    public @Valid ApiCallResponse<GuestOneResponseDto> guestParticipate(
            @Parameter(description = "이벤트의 ID", required = true) @NotNull @Valid @PathVariable UUID eventId,
            @Valid @RequestBody EventParticipantRequest requestDto
    ) {
        final var currentUserInfo = authService.getCurrentUserInfo();

        final var eventParticipantRequest = EventParticipantRequestDto.builder()
                .eventId(eventId)
                .ownerId(currentUserInfo.userId())
                .ownerType(currentUserInfo.role())
                .ableDaysAndTimes(requestDto.getAbleDaysAndTimes())
                .build();

        final EventParticipant participate = eventParticipantService.participate(eventParticipantRequest);

        return ApiCallResponse.ofSuccess(null);
    }

    @Operation(summary = "[인증] 유저의 이벤트 참여", description = "다시 등록하고 싶으면 이 API를 다시 호출하면 된다. Delete & Insert")
    @Auth(Role.USER)
    @PostMapping("/{eventId}/user/participate")
    public @Valid ApiCallResponse<GuestOneResponseDto> userParticipate(
            @Parameter(description = "이벤트의 ID", required = true) @NotNull @Valid @PathVariable UUID eventId,
            @Valid @RequestBody EventParticipantRequest requestDto
    ) {
        final var currentUserInfo = authService.getCurrentUserInfo();

        final var eventParticipantRequest = EventParticipantRequestDto.builder()
                .eventId(eventId)
                .ownerId(currentUserInfo.userId())
                .ownerType(currentUserInfo.role())
                .ableDaysAndTimes(requestDto.getAbleDaysAndTimes())
                .build();

        eventParticipantService.participate(eventParticipantRequest);

        return ApiCallResponse.ofSuccess(null);
    }
}