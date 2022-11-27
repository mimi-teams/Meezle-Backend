package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.ApiResponse;
import com.mimi.w2m.backend.dto.ApiResultCode;
import com.mimi.w2m.backend.dto.event.EventRequestDto;
import com.mimi.w2m.backend.dto.event.EventResponseDto;
import com.mimi.w2m.backend.dto.participant.ParticipantResponseDto;
import com.mimi.w2m.backend.dto.participle.EventParticipleTimeRequestDto;
import com.mimi.w2m.backend.dto.participle.EventParticipleTimeResponseDto;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.service.*;
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
public class EventApi extends BaseGenericApi<EventRequestDto, EventResponseDto, Long, EventService> {
private final Logger                     logger = LogManager.getLogger(EventApi.class);
private final UserService                userService;
private final ParticipantService         participantService;
private final EventParticipleTimeService eventParticipleTimeService;

public EventApi(EventService service, AuthService authService, HttpSession httpSession, UserService userService,
                ParticipantService participantService, EventParticipleTimeService timeService) {
    super(service, authService, httpSession);
    this.userService           = userService;
    this.participantService    = participantService;
    eventParticipleTimeService = timeService;
}

@Override
public ApiResponse<EventResponseDto> get(
        @PathVariable("id") Long id) {
    final var event = service.getEvent(id);
    return ApiResponse.ofSuccess(EventResponseDto.of(event));
}

@Override
public ApiResponse<EventResponseDto> post(
        @RequestBody EventRequestDto requestDto) {
    final var loginInfo = authService.getCurrentLogin(httpSession);
    if(!Objects.equals(loginInfo.role(), Role.USER)) {
        throw new UnauthorizedException("Role=" + loginInfo.role() + " 는 이벤트를 생성할 수 없습니다", "가입된 이용자만 이벤트를 생성할 수 " +
                                                                                           "있습니다");
    }
    final var event = service.createEvent(loginInfo.loginId(), requestDto);
    return ApiResponse.ofSuccess(EventResponseDto.of(event));
}

@Override
public ApiResponse<EventResponseDto> patch(
        @PathVariable("id") Long id,
        @RequestBody EventRequestDto requestDto) {
    final var loginInfo = authService.getCurrentLogin(httpSession);
    authService.isHost(loginInfo, id);
    final var event = service.modifyEvent(id, requestDto);
    return ApiResponse.ofSuccess(EventResponseDto.of(event));
}

@Override
@Deprecated
public ApiResponse<EventResponseDto> put(
        @PathVariable("id") Long id,
        @RequestBody EventRequestDto requestDto) {
    return ApiResponse.of(ApiResultCode.UNUSED_API, null);
}

/**
 * 연관된 요소 모두 삭제
 *
 * @author teddy
 * @since 2022/11/27
 **/
@Override
public ResponseEntity<?> delete(
        @PathVariable("id") Long id) {
    final var loginInfo = authService.getCurrentLogin(httpSession);
    authService.isHost(loginInfo, id);
    eventParticipleTimeService.deleteAll(eventParticipleTimeService.getEventParticipleTimes(id));
    participantService.deleteAll(participantService.getAllParticipantInEvent(id));

    service.deleteEventReal(id);
    final var headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
}

/**
 * 이벤트 참여자 모두 가져오기(host 제외)
 *
 * @author teddy
 * @since 2022/11/27
 **/
@Operation(method = "GET", description = "[인증] Event의 참여자 가져오기(이벤트에 참여자만 가능). Host는 가져오지 않는다")
@GetMapping(path = "/{id}/participants")
public ApiResponse<List<ParticipantResponseDto>> getParticipants(
        @PathVariable("id") Long id) {
    final var loginInfo = authService.getCurrentLogin(httpSession);
    authService.isInEvent(loginInfo, id);
    final var participants = participantService.getAllParticipantInEvent(id);
    final var resDto       = participants.stream().map(ParticipantResponseDto::of).toList();
    return ApiResponse.ofSuccess(resDto);
}

/**
 * 이벤트 생성자 가져오기
 *
 * @author teddy
 * @since 2022/11/27
 **/
@Operation(method = "GET", description = "[인증] Event의 생성자 가져오기(이벤트 참여자만 가능)")
@GetMapping(path = "/{id}/host")
public ApiResponse<UserResponseDto> getHost(
        @PathVariable("id") Long id) {
    final var loginInfo = authService.getCurrentLogin(httpSession);
    authService.isInEvent(loginInfo, id);
    final var event = service.getEvent(id);
    return ApiResponse.ofSuccess(UserResponseDto.of(event.getUser()));
}

/**
 * 모든 사용자가 참여 가능한 시간 계산하기
 *
 * @author teddy
 * @since 2022/11/27
 **/
@Operation(method = "PATCH", description = "[인증] EventParticipleTime의 공통 부분 계산 or 직접 설정(host만 가능)")
@PatchMapping(path = "/{id}/meeting-time")
public ApiResponse<EventResponseDto> getCommonParticipleTime(
        @PathVariable("id") Long id,
        @RequestParam String mode,
        @RequestBody EventParticipleTimeRequestDto requestDto) {
    final var loginInfo = authService.getCurrentLogin(httpSession);
    authService.isHost(loginInfo, id);

    if(Objects.equals(mode, "merge")) {
        final var event = service.calculateSharedTime(id);
        return ApiResponse.ofSuccess(EventResponseDto.of(event));
    } else if(Objects.equals(mode, "set")) {
        final var event = service.setEventTimeDirectly(id, requestDto);
        return ApiResponse.ofSuccess(EventResponseDto.of(event));
    } else {
        return ApiResponse.of(ApiResultCode.INVALID_VALUE, "mode=[merge|set]", null);
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
public ApiResponse<EventParticipleTimeResponseDto> putParticipleTime(
        @PathVariable("id") Long id,
        @RequestBody EventParticipleTimeRequestDto requestDto) {
    final var info = authService.getCurrentLogin(httpSession);
    authService.isInEvent(info, id);
    final var participleTime = eventParticipleTimeService.createOrUpdate(id, requestDto, info.loginId(), info.role());
    return ApiResponse.ofSuccess(EventParticipleTimeResponseDto.of(participleTime));
}

/**
 * ParticipleTime 모두 가져오기
 *
 * @author teddy
 * @since 2022/11/27
 **/
@Operation(method = "GET", description = "[인증] 이벤트의 참여 가능한 시간 모두 가져오기")
@GetMapping(path = "/{id}/participle-time/all")
public ApiResponse<List<EventParticipleTimeResponseDto>> getParticipleTimes(
        @PathVariable("id") Long id) {
    final var info = authService.getCurrentLogin(httpSession);
    authService.isInEvent(info, id);
    final var participleTimes = eventParticipleTimeService.getEventParticipleTimes(id);
    final var resDto          = participleTimes.stream().map(EventParticipleTimeResponseDto::of).toList();
    return ApiResponse.ofSuccess(resDto);
}

/**
 * 자신의 ParticipleTime 가져오기
 *
 * @author teddy
 * @since 2022/11/27
 **/
@Operation(method = "GET", description = "[인증] 자신의 참여 가능한 시간을 가져오기")
@GetMapping(path = "/{id}/participle-time")
public ApiResponse<List<EventParticipleTimeResponseDto>> getParticipleTime(
        @PathVariable("id") Long id) {
    final var info = authService.getCurrentLogin(httpSession);
    authService.isInEvent(info, id);
    final var participleTimes = eventParticipleTimeService.getEventParticipleTimes(id, info.loginId(), info.role());
    final var resDto          = participleTimes.stream().map(EventParticipleTimeResponseDto::of).toList();
    return ApiResponse.ofSuccess(resDto);
}

}