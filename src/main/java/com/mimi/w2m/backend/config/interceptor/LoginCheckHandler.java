package com.mimi.w2m.backend.config.interceptor;

import com.mimi.w2m.backend.config.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 *
 * 로그인 확인하는 헨들러
 *
 * @since 2022-12-10
 * @author yeh35
 */

@RequiredArgsConstructor
@Component
public class LoginCheckHandler {

    private final JwtHandler jwtHandler;

    public long getUserId(HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization.isBlank()) {
            throw new UnauthorizedException("Authorization 헤더가 존재하지 않습니다");
        }

        final String token = tokenParser(authorization);
        if (token.isBlank()) {
            throw new UnauthorizedException("token 존재하지 않습니다.");
        }

        final Optional<JwtHandler.TokenInfo> tokenInfo = jwtHandler.verify(token);
        if (tokenInfo.isEmpty()) {
            throw new UnauthorizedException("검증되지 않은 토큰입니다.");
        }

        return tokenInfo.get().userId;
    }


    /**
     * authorization Header에서 토큰 추출
     * @return 토큰이 없으면 빈 값을 리턴
     * @since 2022-12-10
     * @author yeh35
     */
    @NotNull
    public String tokenParser(String authorization) {

        if (authorization.length() < 7) {
            return "";
        }

        final String token;
        if (Pattern.matches("^Bearer *", authorization)) {
            token = authorization.replaceAll("^Bearer( )*", "");
            return token;
        } else {
            return "";
        }
    }
}
