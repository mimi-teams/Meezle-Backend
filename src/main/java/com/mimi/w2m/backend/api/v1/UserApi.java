package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.service.*;
import com.mimi.w2m.backend.common.Role;
import com.mimi.w2m.backend.dto.user.UserRequestDto;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.exception.IllegalAccessException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.util.Objects;

/**
 * UserApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
@Tag(name = "User Api", description = "가입한 이용자와 관련된 API")
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/v1/users")
@RestController
public class UserApi {
    private final Logger logger = LoggerFactory.getLogger(UserApi.class.getName());
    private final EventService eventService;
    private final GuestService guestService;
    private final EventParticipantService eventParticipantService;

    private final UserService userService;
    private final AuthService authService;
    private final HttpSession httpSession;

    @Operation(method = "GET",
            summary = "이용자 정보 반환",
            description = "[로그인 O, 인가 X] ID에 해당하는 이용자 정보를 반환한다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @GetMapping(path = "/{id}")
    public @Valid ApiCallResponse<UserResponseDto> get(
            @Parameter(name = "id", description = "이용자가 로그인할 때 제공된 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id
    ) {
        authService.getLoginInfo(httpSession);
        final var user = userService.get(id);
        return ApiCallResponse.ofSuccess(UserResponseDto.of(user));
    }

    @Operation(method = "PATCH",
            summary = "본인 정보 수정",
            description = "[로그인 O, 인가 O] ID에 해당하는 이용자의 정보를 수정한다. 현재 로그인한 이용자와 같아야 하며, 반환되는 정보는 없다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @PatchMapping(path = "/{id}")
    public @Valid ApiCallResponse<UserResponseDto> patch(
            @Parameter(name = "id", description = "이용자가 로그인할 때 제공된 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id,
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isValidLogin(loginInfo, id, Role.USER);
        final var user = userService.update(id, requestDto);
        return ApiCallResponse.ofSuccess(null);
    }

    @Operation(method = "DELETE",
            summary = "회원 탈퇴",
            description = "[로그인 O, 인가 O] ID에 해당하는 이용자를 삭제한다. 현재 로그인한 이용자와 같아야 하며, 삭제 시 Host 로 있던 Event 가 모두 삭제된다. " +
                    "삭제 후, Logout 되고 '/' 로 Redirect 된다",
            responses = {@ApiResponse(description = "'/'로 Redirect",
                    content = {@Content(schema = @Schema(description = "GET '/'"))})})
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(
            @Parameter(name = "id", description = "이용자가 로그인할 때 제공된 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isValidLogin(loginInfo, id, Role.USER);

        final var associatedEvents = eventService.getAllByHost(id);
        associatedEvents.forEach(event -> {
            eventParticipantService.deleteAll(eventParticipantService.getAll(event.getId()));
            guestService.deleteAll(guestService.getAllInEvent(event.getId()));
        });
        eventService.deleteAll(associatedEvents);

        authService.logout(httpSession);
        userService.deleteReal(id);
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(method = "GET",
            summary = "이메일에 해당하는 이용자 정보 반환",
            description = "[로그인 O, 인가 X] Email에 해당하는 이용자를 반환한다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @GetMapping(path = "")
    public @Valid ApiCallResponse<UserResponseDto> getByEmail(
            @Parameter(name = "email",
                    description = "검색에 사용하는 email",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) @Email @NotNull @Valid
            @RequestParam String email
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        final var user = userService.getByEmail(email);
        return ApiCallResponse.ofSuccess(UserResponseDto.of(user));
    }

    @Operation(method = "GET",
            summary = "플랫폼을 이용한 로그인",
            description = "[로그인 X] Google 이나 Kakao 계정을 이용해 로그인한다. 로그인된 이용자가 있다면 로그아웃을 수행한다. 가입된 Email 이 없는 경우, " +
                    "이용자를 새로 등록한다. 로그인 후, '/'로 Redirect 된다",
            responses = {@ApiResponse(description = "'/'로 Redirect",
                    content = {@Content(schema = @Schema(description = "GET '/'"))})})
    @GetMapping(path = "/login")
    public ResponseEntity<?> loginWithOauth2(
            @Parameter(name = "platform", description = "로그인할 계정의 플랫폼", in = ParameterIn.QUERY, required = true)
            @NotNull @Pattern(regexp = "^(kakao|google)$") @Valid @RequestParam String platform
    ) {
        authService.logout(httpSession);
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/oauth2/authorization/" + platform));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(method = "GET",
            summary = "가입한 이용자의 로그아웃",
            description = "[로그인 O, 인가 X] 이용자의 로그아웃을 처리한다. 로그아웃 후, '/'로 Redirect 된다",
            responses = {@ApiResponse(description = "'/'로 Redirect",
                    content = {@Content(schema = @Schema(description = "GET '/'"))})})
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        final var auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (Objects.nonNull(auth)) {
            authService.logout(httpSession);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        } else {
            throw new IllegalAccessException("로그아웃을 하려고 했지만, 로그인되지 않았습니다", "로그아웃을 하려고 했지만, 로그인되지 않았습니다");
        }
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}