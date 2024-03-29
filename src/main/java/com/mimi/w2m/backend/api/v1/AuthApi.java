package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.config.interceptor.JwtHandler;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.PlatformType;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.auth.LoginSuccessResponse;
import com.mimi.w2m.backend.dto.auth.OAauth2AuthorizationResponse;
import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.service.Oauth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Tag(name = "Auth Api", description = "로그인/회원가입")
@RequiredArgsConstructor
@RequestMapping(path = "/v1/auth")
@RestController
@Validated
public class AuthApi {

    private final Oauth2Service oauth2Service;
    private final JwtHandler jwtHandler;
    private final Logger logger = LoggerFactory.getLogger("AuthApi");

    /**
     * Kakao Oauth2 과정에서 인가 과정에서 사용된 Redirect URL 과 Access Token 을 발급할 때 사용된 것은 동일해야 한다.
     * 또한, 이 Redirect URL 은 KaKao App 의 Redirect Url 에 등록되어 있어야 한다.
     * + 캘린더 및 메시지 권한을 획득할 수 있도록 추가
     * + Redirect URL = 카카오 api에서 redirect url을 호출할 때 http 통신으로 넘긴다. 따라서
     * 1. redirect url을 명시적으로 넘기거나
     * 2. http 연결을 받도록 KAKAO_HOST를 수정한다.(적용 : http://... 을 호스트로 설정한다)
     * 3. http 요청을 https로 바꾼다.
     *
     * @author teddy
     * @since 2023/01/07
     **/
    @Operation(summary = "KaKao OAuth2 이용한 로그인(1단계)",
            description = "KaKao Oauth2 를 이용한 로그인 단계 1. 요청 후, Kakao 계정으로 로그인할 수 있는 URL 가 반환된다. GET {authorizationUrl} 으로 2단계에 진입할 수 있다.",
            responses = {@ApiResponse(description = "'/'로 Redirect",
                    content = {@Content(schema = @Schema(description = "GET '/'"))})})
    @GetMapping(path = "/oauth2/authorization")
    public @Valid ApiCallResponse<OAauth2AuthorizationResponse> oauth2Authorization(
            @Parameter(name = "platform", description = "로그인할 계정의 플랫폼", in = ParameterIn.QUERY, required = true)
            @Valid @NotNull @RequestParam PlatformType platform
    ) {
        final String authorizationUrl = oauth2Service.getOauthAuthorizationUrl(platform);
        final var response = OAauth2AuthorizationResponse.builder()
                .authorizationUrl(authorizationUrl)
                .build();
        return ApiCallResponse.ofSuccess(response);
    }

    @Operation(summary = "KaKao OAuth2 이용한 로그인(2단계)",
            description = "1단계에서 GET {authorizationUrl} 이후 자동으로 Redirect 되며, Jwt Token 이 발급된다.")
    @GetMapping(path = "/oauth2/authorization/redirect/kakao")
    public @Valid ApiCallResponse<LoginSuccessResponse> oauth2Authorization(
            HttpServletRequest request,
            @Valid @NotNull @RequestParam String code,
            @RequestParam(required = false) String requestUrl
    ) {
        if(Objects.isNull(requestUrl)) {
            requestUrl = request.getRequestURL().toString();
            if (!requestUrl.contains("localhost")) {
                requestUrl = requestUrl.replace("http", "https");
            }
        }
        logger.info(requestUrl);
        final User user = oauth2Service.afterAuthorization(PlatformType.KAKAO, code, requestUrl);
        final String token = jwtHandler.createToken(user.getId(), Role.USER);

        return ApiCallResponse.ofSuccess(
                LoginSuccessResponse.builder()
                        .name(user.getName())
                        .token(token)
                        .build()
        );
    }

}
