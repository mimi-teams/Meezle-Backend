package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.config.interceptor.Auth;
import com.mimi.w2m.backend.dto.auth.CurrentUserInfo;
import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.dto.user.UserRequestDto;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import com.mimi.w2m.backend.service.AuthService;
import com.mimi.w2m.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "이용자 정보 반환", description = "[인증] 로그인한 이용자 정보를 반환한다")
    @Auth
    @GetMapping(path = "/me")
    public @Valid ApiCallResponse<UserResponseDto> get() {
        final CurrentUserInfo currentUserInfo = authService.getCurrentUserInfo();

        final var user = userService.getUser(currentUserInfo.userId());

        return ApiCallResponse.ofSuccess(UserResponseDto.of(user));
    }

    @Operation(summary = "본인 정보 수정",
            description = "[인증] ID에 해당하는 이용자의 정보를 수정한다. 현재 로그인한 이용자와 같아야 하며, 반환되는 정보는 없다")
    @Auth
    @PatchMapping(path = "/me")
    public @Valid ApiCallResponse<UserResponseDto> patch(
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        final CurrentUserInfo currentUserInfo = authService.getCurrentUserInfo();

        final var user = userService.update(currentUserInfo.userId(), requestDto);

        return ApiCallResponse.ofSuccess(UserResponseDto.of(user));
    }

    @Operation(method = "GET",
            summary = "가입한 이용자의 로그아웃",
            description = "[인증] 이용자의 로그아웃을 처리한다. 로그아웃 후, 처리는 클라이언트에서 해주시면 됩니다.")
    @Auth
    @PostMapping(path = "/me/logout")
    public ApiCallResponse<?> logout() {
        final CurrentUserInfo currentUserInfo = authService.getCurrentUserInfo();

        authService.logoutToken("token");

        return ApiCallResponse.ofSuccess(null);
    }
    /**
     * 회원탈퇴 만들기
     * @author teddy
     * @since 2022/12/28
    **/
    @Operation(summary = "회원 탈퇴",
            description = "[인증] 이용자를 삭제한다, 삭제 후에는 클라이언트에서 처리해주세요.")
    @Auth
    @PostMapping(path = "/me/leave")
    public ApiCallResponse<?> delete() {
        final CurrentUserInfo currentUserInfo = authService.getCurrentUserInfo();

        authService.logoutToken("");
        userService.deleteReal(currentUserInfo.userId());
        return ApiCallResponse.ofSuccess(null);
    }

}