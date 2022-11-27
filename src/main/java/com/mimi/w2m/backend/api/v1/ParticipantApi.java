package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.ApiResponse;
import com.mimi.w2m.backend.dto.ApiResultCode;
import com.mimi.w2m.backend.dto.participant.ParticipantRequestDto;
import com.mimi.w2m.backend.dto.participant.ParticipantResponseDto;
import com.mimi.w2m.backend.service.AuthService;
import com.mimi.w2m.backend.service.EventParticipleTimeService;
import com.mimi.w2m.backend.service.EventService;
import com.mimi.w2m.backend.service.ParticipantService;
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
 * ParticipantApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/27
 **/
@Tag(name = "Participant Api", description = "Participant 와 관련된 Api 관리")
@RequestMapping(path = "/participants")
@RestController
public class ParticipantApi extends BaseGenericApi<ParticipantRequestDto, ParticipantResponseDto, Long,
                                                          ParticipantService> {
private final Logger                     logger = LogManager.getLogger(ParticipantApi.class);
private final EventService               eventService;
private final EventParticipleTimeService eventParticipleTimeService;

public ParticipantApi(ParticipantService service, AuthService authService, HttpSession httpSession,
                      EventService eventService, EventParticipleTimeService timeService) {
    super(service, authService, httpSession);
    this.eventService          = eventService;
    eventParticipleTimeService = timeService;
}

@Override
public ApiResponse<ParticipantResponseDto> get(
        @PathVariable("id") Long id) {
    final var participant = service.getParticipant(id);
    return ApiResponse.ofSuccess(ParticipantResponseDto.of(participant));
}

@Override
@Deprecated
public ApiResponse<ParticipantResponseDto> post(
        @RequestBody ParticipantRequestDto requestDto) {
    return ApiResponse.of(ApiResultCode.UNUSED_API, null);
}

@Override
public ApiResponse<ParticipantResponseDto> patch(
        @PathVariable("id") Long id,
        @RequestBody ParticipantRequestDto requestDto) {
    authService.isValidLogin(id, Role.PARTICIPANT, httpSession);
    final var participant = service.updateParticipant(id, requestDto);
    return ApiResponse.ofSuccess(ParticipantResponseDto.of(participant));
}

@Override
@Deprecated
public ApiResponse<ParticipantResponseDto> put(
        @PathVariable("id") Long id,
        @RequestBody ParticipantRequestDto requestDto) {
    return ApiResponse.of(ApiResultCode.UNUSED_API, null);
}

/**
 * 연관된 모든 요소 삭제
 *
 * @author teddy
 * @since 2022/11/27
 **/
@Operation(method = "DELETE", description = "[인증] PARTICIPANT 삭제(연관된 모든 정보 삭제 후, '/'로 Redirect")
@Override
public ResponseEntity<?> delete(Long id) {
    authService.isValidLogin(id, Role.PARTICIPANT, httpSession);
    final var participant = service.getParticipant(id);
    eventParticipleTimeService.deleteAll(eventParticipleTimeService
                                                 .getEventParticipleTimes(participant.getEvent().getId(), id,
                                                                          Role.PARTICIPANT));
    authService.logout(httpSession);
    service.delete(participant);
    final var headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
}

@Operation(method = "GET", description = "[인증X] Participant Login(name & password)")
@GetMapping(path = "/login")
public ResponseEntity<?> login(
        @RequestBody ParticipantRequestDto requestDto) {
    service.login(requestDto);
    final var headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
}

@Operation(method = "GET", description = "[인증] Participant logout 처리")
@GetMapping(path = "/logout")
public ResponseEntity<?> logout() {
    authService.logout(httpSession);
    final var headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
}

}