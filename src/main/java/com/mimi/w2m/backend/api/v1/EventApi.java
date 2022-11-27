package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.ApiResponse;
import com.mimi.w2m.backend.dto.ApiResultCode;
import com.mimi.w2m.backend.dto.event.EventRequestDto;
import com.mimi.w2m.backend.dto.event.EventResponseDto;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
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
    try {
        final var event = service.getEvent(id);
        return ApiResponse.ofSuccess(EventResponseDto.of(event));
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, null);
    }
}

@Override
public ApiResponse<EventResponseDto> post(
        @RequestBody EventRequestDto requestDto) {
    try {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        if(!Objects.equals(loginInfo.role(), Role.USER)) {
            throw new UnauthorizedException("Role=" + loginInfo.role() + " 는 이벤트를 생성할 수 없습니다", "가입된 이용자만 이벤트를 생성할 수 " +
                                                                                               "있습니다");
        }
        final var event = service.createEvent(loginInfo.loginId(), requestDto);
        return ApiResponse.ofSuccess(EventResponseDto.of(event));
    } catch(UnauthorizedException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, null);
    }
}

@Override
public ApiResponse<EventResponseDto> patch(
        @PathVariable("id") Long id,
        @RequestBody EventRequestDto requestDto) {
    try {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        authService.isHost(loginInfo, id);
        final var event = service.modifyEvent(id, requestDto);
        return ApiResponse.ofSuccess(EventResponseDto.of(event));
    } catch(UnauthorizedException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, null);
    }
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
public ApiResponse<EventResponseDto> delete(
        @PathVariable("id") Long id) {
    try {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        authService.isHost(loginInfo, id);
        eventParticipleTimeService.deleteAll(eventParticipleTimeService.getEventParticipleTimes(id));
        participantService.deleteAll(participantService.getAllParticipantInEvent(id));
        service.deleteEventReal(id);
        return ApiResponse.ofSuccess(null);
    } catch(UnauthorizedException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, null);
    }
}
}