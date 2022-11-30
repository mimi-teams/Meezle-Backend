package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.service.*;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.dto.response.ApiCallResponse;
import com.mimi.w2m.backend.type.dto.response.ApiResultCode;
import com.mimi.w2m.backend.type.dto.user.UserRequestDto;
import com.mimi.w2m.backend.type.dto.user.UserResponseDto;
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
public class UserApi extends BaseGenericApi<UserService> {
    private final Logger                     logger = LogManager.getLogger(UserApi.class);
    private final EventService               eventService;
    private final GuestService            guestService;
    private final EventParticipantService eventParticipantService;

    public UserApi(UserService service, AuthService authService, HttpSession httpSession, EventService eventService,
                   GuestService guestService, EventParticipantService timeService) {
        super(service, authService, httpSession);
        this.eventService       = eventService;
        this.guestService       = guestService;
        eventParticipantService = timeService;
    }

    @Operation(method = "GET", description = "[인증] ID의 USER 가져오기(인가 X)")
    @GetMapping(path = "/{id}")
    public UserResponseDto get(
            @PathVariable("id") Long id) {
        final var user = service.getUser(id);
//        return ApiCallResponse.ofSuccess(UserResponseDto.of(user));
        return UserResponseDto.of(user);
    }

    @Operation(method = "PATCH", description = "[인증] ID의 정보 수정하기(본인만 가능)")
    @PatchMapping(path = "/{id}")
    public ApiCallResponse<UserResponseDto> patch(
            @PathVariable("id") Long id,
            @RequestBody UserRequestDto dto) {
        authService.isValidLogin(id, Role.USER, httpSession);
        final var user = service.updateUser(id, dto.getName(), dto.getEmail());
        return ApiCallResponse.ofSuccess(null);
    }

    @Operation(method = "DELETE", description = "[인증] USER 삭제(연관된 모든 정보 삭제 후, '/'로 Redirect")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") Long id) {
        authService.isValidLogin(id, Role.USER, httpSession);
        final var associatedEvents = eventService.getEventsCreatedByUser(id);
        associatedEvents.forEach(event -> {
            eventParticipantService.deleteAll(eventParticipantService.getAllParticipantInfo(event.getId()));
            guestService.deleteAll(guestService.getAllInEvent(event.getId()));
        });
        eventService.deleteAll(associatedEvents);

        authService.logout(httpSession);
        service.deleteUserReal(id);
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(method = "GET", description = "[인증] Email 로 이용자 가져오기(인가 X)")
    @GetMapping(path = "")
    public ApiCallResponse<UserResponseDto> getByEmail(
            @RequestParam String email) {
        final var user = service.getUserByEmail(email);
        return ApiCallResponse.ofSuccess(UserResponseDto.of(user));
    }

    @Operation(method = "GET", description = "[인증X] Login(platform = google or kakao). 신규 사용자의 경우 새로 등록")
    @GetMapping(path = "/login")
    public ResponseEntity<?> loginWithOauth2(
            @RequestParam String platform) {
        final var validPlatforms = Set.of("kakao", "google");
        final var headers        = new HttpHeaders();
        if(validPlatforms.contains(platform)) {
            headers.setLocation(URI.create("/oauth2/authorization/" + platform));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        } else {
            return ResponseEntity.of(Optional.of(ApiCallResponse.of(ApiResultCode.INVALID_VALUE, null)));
        }
    }

    @Operation(method = "GET", description = "[인증] User logout 처리")
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        final var auth = SecurityContextHolder.getContext()
                                              .getAuthentication();
        if(Objects.nonNull(auth)) {
            authService.logout(httpSession);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        } else {
            logger.warn("Not login");
        }
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}