package com.mimi.w2m.backend.api;

import com.mimi.w2m.backend.service.AuthService;
import com.mimi.w2m.backend.type.dto.security.SessionInfoResponseDto;
import com.mimi.w2m.backend.type.response.ApiCallResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Tag(name = "Root('/') Api", description = "로그인이나 로그아웃 수행 시, Redirect 되는 URI. 현재 로그인한 이용자 정보를 담고 있다")
@RequiredArgsConstructor
@RequestMapping(path = "/")
@RestController
public class RootApi {
    private final AuthService authService;
    private final HttpSession httpSession;

    @Operation(method = "GET", summary = "로그인 정보 반환",
            description = "[로그인 X] 로그인 정보를 반환한다. 정보가 없다면 EntityNotFoundHandler 가 수행된다",
            responses = {@ApiResponse(useReturnTypeSchema = true)})
    @GetMapping(path = "")
    public ApiCallResponse<SessionInfoResponseDto> get() {


        final var loginInfo = authService.getLoginInfo(httpSession);
        return ApiCallResponse.ofSuccess(SessionInfoResponseDto.of(loginInfo));
    }
}