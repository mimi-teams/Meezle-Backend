package com.mimi.w2m.backend.api;

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.ApiResponse;
import com.mimi.w2m.backend.dto.ApiResultCode;
import com.mimi.w2m.backend.dto.security.SessionInfo;
import com.mimi.w2m.backend.dto.security.SessionInfoResponseDto;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.service.ParticipantService;
import com.mimi.w2m.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Objects;

@Tag(name = "Root('/') Api", description = "Login 이나 Logout 성공시 Redirect 되는 Uri 처리")
@RequiredArgsConstructor
@RequestMapping(path = "/")
@RestController
public class RootApi {
private final HttpSession        httpSession;
private final Logger             logger = LogManager.getLogger(RootApi.class);
private final UserService        userService;
private final ParticipantService participantService;

@Operation(method = "GET", description = "[인증x] Session 에 저장된 정보 보내기(없으면 null)")
@GetMapping(path = "")
public ApiResponse<SessionInfoResponseDto> get() {
    try {
        final var sessionInfo = (SessionInfo) httpSession.getAttribute(SessionInfo.key);
        if(Objects.nonNull(sessionInfo)) {
            final var dto = switch(sessionInfo.role()) {
                case USER -> SessionInfoResponseDto.of(userService.getUser(sessionInfo.loginId()));
                case PARTICIPANT -> SessionInfoResponseDto.of(participantService.getParticipant(sessionInfo.loginId()));
                case NONE -> throw new InvalidValueException("잘못된 역할입니다 : " + Role.NONE, "잘못된 역할입니다");
            };
            return ApiResponse.ofSuccess(dto);
        } else {
            return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, "로그인 정보가 없습니다", null);
        }
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(InvalidValueException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.INVALID_VALUE, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, ApiResultCode.SERVER_ERROR.defaultMessage, null);
    }
}
}