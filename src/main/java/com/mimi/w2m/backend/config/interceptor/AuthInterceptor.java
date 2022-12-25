package com.mimi.w2m.backend.config.interceptor;

import com.mimi.w2m.backend.config.constants.Constants;
import com.mimi.w2m.backend.config.exception.IllegalAccessException;
import com.mimi.w2m.backend.dto.auth.CurrentUserInfo;
import com.mimi.w2m.backend.dto.security.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 인증 처리를 담당
 *
 * @since 2022-12-10
 * @author yeh35
 */

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final LoginCheckHandler loginCheckHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!(handler instanceof final HandlerMethod handlerMethod)) {
            return true;
        }

        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        if (auth == null) {
            return true;
        }

        // 유저 정보
        final LoginInfo loginInfo = loginCheckHandler.loadLoginInfo(request);
        
        // 유저 타입이 맞는지 확인
        if (auth.value() != loginInfo.role()) {
            throw new IllegalAccessException("허용된 유저 타입이 다릅니다.");
        }
        
        request.setAttribute(Constants.CURRENT_USER, CurrentUserInfo.of(loginInfo));
        return true;
    }
}
