package com.mimi.w2m.backend.api.v1.security;

import com.mimi.w2m.backend.api.v1.BaseApiEntry;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
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
@RestController
public class AuthController extends BaseApiEntry {

    @GetMapping("/security/login/oauth2")
    public void loginWithOauth2(@RequestParam String registrationId, HttpServletResponse response) throws IOException {
        if(registrationId.equals("google") || registrationId.equals("kakao")) {
            response.sendRedirect("/oauth2/authorization/" + registrationId);
        } else {
            response.sendError(501, registrationId + " Not Implemented");
        }
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