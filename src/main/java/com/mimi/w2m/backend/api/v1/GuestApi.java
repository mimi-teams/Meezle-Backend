package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.service.AuthService;
import com.mimi.w2m.backend.service.EventParticipantService;
import com.mimi.w2m.backend.service.GuestService;
import com.mimi.w2m.backend.common.Role;
import com.mimi.w2m.backend.dto.guest.GuestRequestDto;
import com.mimi.w2m.backend.dto.guest.GuestResponseDto;
import com.mimi.w2m.backend.dto.base.ApiCallResponse;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;

/**
 * GuestApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/27
 **/
@Tag(name = "Guest Api", description = "Guest 와 관련된 Api 관리")
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/v1/guests")
@RestController
public class GuestApi {
    private final Logger logger = LoggerFactory.getLogger(GuestApi.class.getName());
    private final EventParticipantService eventParticipantService;

    private final GuestService guestService;
    private final AuthService authService;
    private final HttpSession httpSession;

    @Operation(method = "GET",
            summary = "임시 이용자 정보 반환",
            description = "[로그인 O, 인가 O] ID 에 해당하는 이용자 정보를 반환한다. 이용자가 속한 이벤트의 참여자만 이용할 수 있다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @GetMapping(path = "/{id}")
    public @Valid ApiCallResponse<GuestResponseDto> get(
            @Parameter(name = "id", description = "이용자가 로그인할 때 제공된 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @PathVariable("id") Long id
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        final var guest = guestService.get(id);
        authService.isInEvent(loginInfo, guest.getEvent()
                .getId());
        return ApiCallResponse.ofSuccess(GuestResponseDto.of(guest));
    }

    @Operation(method = "PATCH",
            summary = "임시 이용자 정보 수정",
            description = "[로그인 O, 인가 O] ID에 해당하는 이용자 정보를 수정한다. 업데이트 가능한 값은 name & password 이고 event 는 변경할 수 없다. " +
                    "현재 로그인한 이용자와 같아야 하며, 반환되는 정보는 없다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @PatchMapping(path = "/{id}")
    public @Valid ApiCallResponse<GuestResponseDto> patch(
            @Parameter(name = "id", description = "이용자가 로그인할 때 제공된 ID", in = ParameterIn.PATH, required = true)
            @PositiveOrZero @NotNull @Valid @PathVariable("id") Long id,
            @Valid @RequestBody GuestRequestDto requestDto
    ) {
        final var loginInfo = authService.getLoginInfo(httpSession);
        authService.isValidLogin(loginInfo, id, Role.GUEST);
        final var guest = guestService.update(id, requestDto);
        return ApiCallResponse.ofSuccess(null);
    }

    @Operation(method = "GET",
            summary = "임시 이용자의 name & password 를 이용한 로그인",
            description = "[로그인 X] name 과 password 를 이용해 로그인한다. 로그인된 이용자가 있다면 로그아웃을 수행한다. 이벤트에 해당 name 이 없는 경우 새롭게" +
                    " 생성되며, 한 이벤트에서 name 은 유일하게 존재한다. 로그인 후, '/'로 Redirect 된다",
            responses = {@ApiResponse(description = "'/'로 Redirect",
                    content = {@Content(schema = @Schema(description = "GET '/'"))})})
    @GetMapping(path = "/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody GuestRequestDto requestDto
    ) {
        authService.logout(httpSession);
        guestService.login(requestDto);
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(method = "GET",
            summary = "임시 이용자의 로그아웃",
            description = "[로그인 O, 인가 X] 임시 이용자의 로그아웃을 처리한다. 로그아웃 후, '/'로 Redirect 된다",
            responses = {@ApiResponse(description = "'/'로 Redirect",
                    content = {@Content(schema = @Schema(description = "GET '/'"))})})
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout() {
        authService.logout(httpSession);
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}