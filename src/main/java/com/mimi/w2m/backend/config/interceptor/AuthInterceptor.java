package com.mimi.w2m.backend.config.interceptor;

import com.mimi.w2m.backend.config.constants.AttributeConstants;
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
        final Long userId = loginCheckHandler.getUserId(request);
        request.setAttribute(AttributeConstants.USER_ID, userId);

        return true;
    }
}
