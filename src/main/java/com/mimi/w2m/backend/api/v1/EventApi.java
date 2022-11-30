package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.service.*;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.dto.event.EventRequestDto;
import com.mimi.w2m.backend.type.dto.event.EventResponseDto;
import com.mimi.w2m.backend.type.dto.guest.GuestResponseDto;
import com.mimi.w2m.backend.type.dto.participant.EventParticipantRequestDto;
import com.mimi.w2m.backend.type.dto.participant.EventParticipantResponseDto;
import com.mimi.w2m.backend.type.dto.response.ApiCallResponse;
import com.mimi.w2m.backend.type.dto.user.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
@Tag(name = "Event Api", description = "Event 와 관련된 Api 관리")
@RequestMapping(path = "/events")
@RestController
public class EventApi extends BaseGenericApi<EventService> {
    private final Logger                     logger = LogManager.getLogger(EventApi.class);
    private final UserService                userService;
    private final GuestService            guestService;
    private final EventParticipantService eventParticipantService;

    public EventApi(EventService service, AuthService authService, HttpSession httpSession, UserService userService,
                    GuestService guestService, EventParticipantService timeService) {
        super(service, authService, httpSession);
        this.userService        = userService;
        this.guestService       = guestService;
        eventParticipantService = timeService;
    }

    @Operation(method = "GET", description = "[인증] ID의 EVENT 가져오기(이벤트 참여자만 가능)")
    @GetMapping(path = "/{id}")
    public ApiCallResponse<EventResponseDto> get(
            @PathVariable("id") Long id) {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        authService.isInEvent(loginInfo, id);
        final var event = service.getEvent(id);
        return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
    }

    @Operation(method = "POST", description = "[인증] EVENT 등록하기(USER 만 가능)")
    @PostMapping(path = "")
    public ApiCallResponse<EventResponseDto> post(
            @RequestBody EventRequestDto requestDto) {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        if(!Objects.equals(loginInfo.role(), Role.USER)) {
            throw new UnauthorizedException("Role=" + loginInfo.role() + " 는 이벤트를 생성할 수 없습니다",
                                            "가입된 이용자만 이벤트를 생성할 수 있습니다");
        }
        final var event = service.createEvent(loginInfo.loginId(), requestDto);
        return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
    }

    @Operation(method = "PATCH", description = "[인증] EVENT 수정하기(HOST 만 가능)")
    @PatchMapping(path = "/{id}")
    public ApiCallResponse<EventResponseDto> patch(
            @PathVariable("id") Long id,
            @RequestBody EventRequestDto requestDto) {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        authService.isHost(loginInfo, id);
        final var event = service.modifyEvent(id, requestDto);
        return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
    }

    /**
     * 연관된 요소 모두 삭제
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Operation(method = "DELETE", description = "[인증] EVENT 삭제하기. 연관된 정보(참여자 등)도 모두 제거됨(HOST만 가능)")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") Long id) {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        authService.isHost(loginInfo, id);

        eventParticipantService.deleteAll(eventParticipantService.getAllParticipantInfo(id));
        guestService.deleteAll(guestService.getAllInEvent(id));
        service.deleteEventReal(id);

        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    // TODO: 2022/11/30 Participant를 별도로 만들어 HOST도 같이 가져올 수 있게 만든다
    @Operation(method = "GET", description = "[인증] EVENT 모든 참여자 가져오기(이벤트에 참여자만 가능)")
    @GetMapping(path = "/{id}/participants")
    public ApiCallResponse<List<GuestResponseDto>> getParticipants(
            @PathVariable("id") Long id) {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        authService.isInEvent(loginInfo, id);
        final var participants = guestService.getAllInEvent(id);
        final var resDto = participants.stream()
                                       .map(GuestResponseDto::of)
                                       .toList();
        return ApiCallResponse.ofSuccess(resDto);
    }

    /**
     * 이벤트 생성자 가져오기
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Operation(method = "GET", description = "[인증] Event HOST 가져오기(이벤트 참여자만 가능)")
    @GetMapping(path = "/{id}/host")
    public ApiCallResponse<UserResponseDto> getHost(
            @PathVariable("id") Long id) {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        authService.isInEvent(loginInfo, id);
        final var event = service.getEvent(id);
        return ApiCallResponse.ofSuccess(UserResponseDto.of(event.getHost()));
    }

    /**
     * 모든 사용자가 참여 가능한 시간 계산하기
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Operation(method = "PATCH",
               description = "[인증] EventParticipant 의 공통 부분 계산 or 직접 설정(?mode=[merge|set])(이벤트 HOST만 가능)")
    @PatchMapping(path = "/{id}/meeting-time")
    public ApiCallResponse<EventResponseDto> getCommonParticipleTime(
            @PathVariable("id") Long id,
            @RequestParam String mode,
            @RequestBody EventParticipantRequestDto requestDto) {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        authService.isHost(loginInfo, id);

        if(Objects.equals(mode, "merge")) {
            final var event = eventParticipantService.calculateSharedTime(id);
            return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
        } else if(Objects.equals(mode, "set")) {
            final var event = service.setEventTimeDirectly(id, requestDto);
            return ApiCallResponse.ofSuccess(EventResponseDto.of(event));
        } else {
            throw new InvalidValueException("잘못된 모드 : " + mode,
                                            "잘못된 모드입니다. mode=[merge|set]");
        }
    }

    /**
     * ParticipleTime 설정하기
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Operation(method = "PUT", description = "[인증] 참여가능한 시간 설정하기")
    @PutMapping(path = "/{id}/participle-time")
    public ApiCallResponse<EventParticipantResponseDto> putParticipleTime(
            @PathVariable("id") Long id,
            @RequestBody EventParticipantRequestDto requestDto) {
        final var info = authService.getCurrentLogin(httpSession);
        authService.isInEvent(info, id);
        final var participleTime = eventParticipantService.createOrUpdate(id, requestDto, info.loginId(),
                                                                          info.role());
        return ApiCallResponse.ofSuccess(EventParticipantResponseDto.of(participleTime));
    }

    /**
     * ParticipleTime 모두 가져오기
     *
     * @author teddy
     * @since 2022/11/27
     **/
//    @Operation(method = "GET", description = "[인증] 이벤트의 참여 가능한 시간 모두 가져오기")
//    @GetMapping(path = "/{id}/participant-time/all")
//    public ApiCallResponse<EventParticipantResponseDto> getParticipleTimes(
//            @PathVariable("id") Long id) {
//        final var info = authService.getCurrentLogin(httpSession);
//        authService.isInEvent(info, id);
//        final var participleTimes = eventParticipleTimeService.getEventParticipleTimes(id);
//        final var resDto = participleTimes.stream()
//                                          .map(EventParticipantResponseDto::of)
//                                          .toList();
//        return ApiCallResponse.ofSuccess(resDto.get(0));
//    }

    /**
     * 자신의 ParticipleTime 가져오기
     *
     * @author teddy
     * @since 2022/11/27
     **/
//    @Operation(method = "GET", description = "[인증] 자신의 참여 가능한 시간을 가져오기")
//    @GetMapping(path = "/{id}/participant-time")
//    public ApiCallResponse<EventParticipantResponseDto> getParticipleTime(
//            @PathVariable("id") Long id) {
//        final var info = authService.getCurrentLogin(httpSession);
//        authService.isInEvent(info, id);
//        final var participleTimes = eventParticipleTimeService.getEventParticipleTimes(id, info.loginId(), info.role());
//        final var resDto = participleTimes.stream()
//                                          .map(EventParticipantResponseDto::of)
//                                          .toList();
//        return ApiCallResponse.ofSuccess(resDto.get(0));
//    }

}