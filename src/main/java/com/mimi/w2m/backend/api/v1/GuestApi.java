package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.service.AuthService;
import com.mimi.w2m.backend.service.EventParticipantService;
import com.mimi.w2m.backend.service.GuestService;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.dto.guest.GuestRequestDto;
import com.mimi.w2m.backend.type.dto.guest.GuestResponseDto;
import com.mimi.w2m.backend.type.dto.response.ApiCallResponse;
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

/**
 * GuestApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/27
 **/
@Tag(name = "Guest Api", description = "Guest 와 관련된 Api 관리")
@RequestMapping(path = "/guests")
@RestController
public class GuestApi extends BaseGenericApi<GuestService> {
    private final Logger                  logger = LogManager.getLogger(GuestApi.class);
    private final EventParticipantService eventParticipantService;

    public GuestApi(GuestService service, AuthService authService, HttpSession httpSession,
                    EventParticipantService timeService) {
        super(service, authService, httpSession);
        eventParticipantService = timeService;
    }

    @Operation(method = "GET", description = "[인증] ID의 GUEST 가져오기(이벤트 참여자만 가능)")
    @GetMapping(path = "/{id}")
    public ApiCallResponse<GuestResponseDto> get(
            @PathVariable("id") Long id) {
        final var loginInfo = authService.getCurrentLogin(httpSession);
        final var guest     = service.get(id);
        authService.isInEvent(loginInfo, guest.getEvent()
                                              .getId());
        return ApiCallResponse.ofSuccess(GuestResponseDto.of(guest));
    }

    @Deprecated
    @Operation(method = "PATCH", description = "[인증] GUEST 수정하기(본인만 가능)")
    @PatchMapping(path = "/{id}")
    public ApiCallResponse<GuestResponseDto> patch(
            @PathVariable("id") Long id,
            @RequestBody GuestRequestDto requestDto) {
        authService.isValidLogin(id, Role.GUEST, httpSession);
        final var guest = service.update(id, requestDto);
        return ApiCallResponse.ofSuccess(GuestResponseDto.of(guest));
    }

    /**
     * 연관된 모든 요소 삭제. 일단 Event 가 삭제될 때, Guest 정보가 삭제되는 것으로만 만든다
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Deprecated
    @Operation(method = "DELETE", description = "[인증] PARTICIPANT 삭제(연관된 모든 정보 삭제 후, '/'로 Redirect")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(Long id) {
        authService.isValidLogin(id, Role.GUEST, httpSession);
        final var guest = service.get(id);
        eventParticipantService.deleteAll(eventParticipantService.getAllParticipantInfo(guest.getEvent()
                                                                                             .getId(), id,
                                                                                        Role.GUEST));
        authService.logout(httpSession);
        service.delete(guest);
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(method = "GET", description = "[인증X] Guest Login(name & password)")
    @GetMapping(path = "/login")
    public ResponseEntity<?> login(
            @RequestBody GuestRequestDto requestDto) {
        service.login(requestDto);
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(method = "GET", description = "[인증] Guest logout 처리")
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout() {
        authService.logout(httpSession);
        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}