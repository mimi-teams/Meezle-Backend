package com.mimi.w2m.backend.api.v1;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author : teddy
 * @since : 2022/10/09
 */
@RequestMapping("/auth/v1")
@RestController
public class AuthController {

    /**
     * OAuth2 인증을 위한 리다이렉트 ....
     * TODO 이거 설명을 추가해줭
     *
     * @since 2022-11-01
     * @author teddy
     */
    @GetMapping("/security/login/oauth2")
    public void loginWithOauth2(@RequestParam String registrationId, HttpServletResponse response) throws IOException {
        if(registrationId.equals("google") || registrationId.equals("kakao")) {
            response.sendRedirect("/oauth2/authorization/" + registrationId);
        } else {
            response.sendError(501, registrationId + " Not Implemented");
        }

        //TODO 여기에 있는 정보를 토대로 User Entity 생성
    }

    @GetMapping("/security/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(!Objects.isNull(auth)) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        response.sendRedirect("/");
    }
}