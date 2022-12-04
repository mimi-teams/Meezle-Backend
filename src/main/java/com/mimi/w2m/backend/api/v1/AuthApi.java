package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.dto.auth.OAauth2AuthorizationResponse;
import com.mimi.w2m.backend.service.Oauth2Service;
import com.mimi.w2m.backend.type.OAuth2PlatformType;
import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "Auth Api", description = "로그인/회원가입")
@RequiredArgsConstructor
@RequestMapping(path = "/v1/auth")
@RestController
public class AuthApi {

    private final Oauth2Service oauth2Service;

    @Operation(summary = "OAuth2 이용한 로그인",
            description = "Google 이나 Kakao 계정을 이용해 로그인한다. 로그인된 이용자가 있다면 로그아웃을 수행한다. 가입된 Email 이 없는 경우, " +
                    "이용자를 새로 등록한다. 로그인 후, '/'로 Redirect 된다",
            responses = {@ApiResponse(description = "'/'로 Redirect",
                    content = {@Content(schema = @Schema(description = "GET '/'"))})})
    @GetMapping(path = "/oauth2/authorization")
    public ApiCallResponse<OAauth2AuthorizationResponse> oauth2Authorization(
            @Parameter(name = "platform", description = "로그인할 계정의 플랫폼", in = ParameterIn.QUERY, required = true)
            @Valid @NotNull @RequestParam OAuth2PlatformType platform
    ) {
        final String authorizationUrl = oauth2Service.getOauthAuthorizationUrl(platform);
        final var response = OAauth2AuthorizationResponse.builder()
                .authorizationUrl(authorizationUrl)
                .build();
        return ApiCallResponse.ofSuccess(response);
    }

    @Operation(summary = "Kakao OAuth2 로그인 이후 리다이렉션", description = "프론트에서는 해당 API는 신경쓰지 않아도 된다.")
    @GetMapping(path = "/oauth2/authorization/redirect/kakao")
    public ApiCallResponse<?> oauth2Authorization(
            @RequestParam String code
    ) {
        oauth2Service.afterAuthorization(OAuth2PlatformType.KAKAO, code);

        return ApiCallResponse.ofSuccess(null);
    }

}
