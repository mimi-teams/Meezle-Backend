package com.mimi.w2m.backend.api.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : teddy
 * @since : 2022/10/09
 */
@RequestMapping("/auth/v1")
@RestController
public class AuthController {

    @GetMapping("/security/login/oauth2")
    public void loginWithOauth2(@RequestParam String registrationId, HttpServletResponse response) throws IOException {
        if(registrationId.equals("google") || registrationId.equals("kakao")) {
            response.sendRedirect("/oauth2/authorization/" + registrationId);
        } else {
            response.sendError(501, registrationId + " Not Implemented");
        }
    }
//    @GetMapping("/security/logout")
//    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//        if(!Objects.isNull(auth)) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        response.sendRedirect("/");
//    }
}