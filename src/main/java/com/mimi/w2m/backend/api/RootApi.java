package com.mimi.w2m.backend.api;

import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.dto.response.ApiCallResponse;
import com.mimi.w2m.backend.type.dto.security.LoginInfo;
import com.mimi.w2m.backend.type.dto.security.SessionInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Tag(name = "Root('/') Api", description = "Login 이나 Logout 성공시 Redirect 되는 Uri 처리")
@RequiredArgsConstructor
@RequestMapping(path = "/")
@RestController
public class RootApi {
private final HttpSession httpSession;

@Operation(method = "GET", description = "[인증x] Session 에 저장된 정보 보내기(없으면 null)")
@GetMapping(path = "")
public ApiCallResponse<SessionInfoResponseDto> get() {
    final var sessionInfo = (LoginInfo) httpSession.getAttribute(LoginInfo.key);
    if(Objects.isNull(sessionInfo)) {
        throw new EntityNotFoundException("로그인 정보가 없습니다", "로그인 정보가 없습니다");
    }
    if(Objects.equals(sessionInfo.role(), Role.NONE)) {
        throw new InvalidValueException("잘못된 역할 : " + Role.NONE, "잘못된 이용자입니다.");
    }
    return ApiCallResponse.ofSuccess(SessionInfoResponseDto.of(sessionInfo));
}
}