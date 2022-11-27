package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.ApiResponse;
import com.mimi.w2m.backend.dto.ApiResultCode;
import com.mimi.w2m.backend.dto.user.UserRequestDto;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import com.mimi.w2m.backend.error.EntityDuplicatedException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * UserApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
@Tag(name = "User Api", description = "User 와 관련된 Api 관리")
@RequestMapping(path = "/users")
@RestController
public class UserApi extends BaseGenericApi<UserRequestDto, UserResponseDto, Long, UserService> {
private final Logger                     logger = LogManager.getLogger(UserApi.class);
private final EventService               eventService;
private final ParticipantService         participantService;
private final EventParticipleTimeService eventParticipleTimeService;

public UserApi(UserService service, AuthService authService, HttpSession httpSession, EventService eventService,
               ParticipantService participantService, EventParticipleTimeService timeService) {
    super(service, authService, httpSession);
    this.eventService          = eventService;
    this.participantService    = participantService;
    eventParticipleTimeService = timeService;
}

@Override
public ApiResponse<UserResponseDto> get(
        @PathVariable("id") Long id) {
    try {
        final var user = service.getUser(id);
        return ApiResponse.ofSuccess(UserResponseDto.of(user));
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
public ApiResponse<UserResponseDto> post(
        @RequestBody UserRequestDto dto) {
    return ApiResponse.of(ApiResultCode.UNUSED_API, null);
}

@Override
public ApiResponse<UserResponseDto> patch(
        @PathVariable("id") Long id,
        @RequestBody UserRequestDto dto) {
    try {
        authService.isValidLogin(id, Role.USER, httpSession);
        final var user = service.updateUser(id, dto.getName(), dto.getEmail());
        return ApiResponse.ofSuccess(null);
    } catch(UnauthorizedException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(EntityDuplicatedException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_DUPLICATED, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, null);
    }
}

@Override
@Deprecated
public ApiResponse<UserResponseDto> put(
        @PathVariable("id") Long id,
        @RequestBody UserRequestDto dto) {
    return ApiResponse.of(ApiResultCode.UNUSED_API, null);
}

@Override
public ApiResponse<UserResponseDto> delete(
        @PathVariable("id") Long id) {
    try {
        authService.isValidLogin(id, Role.USER, httpSession);
        final var associatedEvents = eventService.getEventsCreatedByUser(id);
        associatedEvents.forEach(event -> {
            eventParticipleTimeService.deleteAll(eventParticipleTimeService.getEventParticipleTimes(event.getId()));
            participantService.deleteAll(participantService.getAllParticipantInEvent(event.getId()));
        });
        eventService.deleteAll(associatedEvents);
        service.deleteUserReal(id);
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

@Operation(method = "GET", description = "[인증] Email 로 이용자 가져오기")
@GetMapping(path = "")
public ApiResponse<UserResponseDto> getByEmail(
        @RequestParam String email) {
    try {
        final var user = service.getUserByEmail(email);
        return ApiResponse.ofSuccess(UserResponseDto.of(user));
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

@Operation(method = "GET", description = "[인증X] Oauth Login(platform = google or kakao). 신규 사용자의 경우 새로 등록된다")
@GetMapping(path = "/login")
public ResponseEntity<?> loginWithOauth2(
        @RequestParam String platform) {
    final var validPlatforms = Set.of("kakao", "google");
    try {
        final var headers = new HttpHeaders();
        if(validPlatforms.contains(platform)) {
            headers.setLocation(URI.create("/oauth2/authorization/" + platform));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        } else {
            return ResponseEntity.of(Optional.of(ApiResponse.of(ApiResultCode.INVALID_VALUE, null)));
        }
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ResponseEntity.of(Optional.of(ApiResponse.of(ApiResultCode.SERVER_ERROR, null)));
    }
}

@Operation(method = "GET", description = "[인증] User logout 처리")
@GetMapping(path = "/logout")
public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
    try {
        final var auth = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.nonNull(auth)) {
            authService.logout(httpSession);
            new SecurityContextLogoutHandler().logout(request, response, auth);
            final var headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        } else {
            logger.warn("Not login");
            return ResponseEntity.of(Optional.of(ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, null)));
        }
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ResponseEntity.of(Optional.of(ApiResponse.of(ApiResultCode.SERVER_ERROR, null)));
    }
}
}