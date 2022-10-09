package com.mimi.w2m.backend.api.test;

import com.mimi.w2m.backend.domain.user.UserRepository;
import com.mimi.w2m.backend.dto.security.UserSession;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author : teddy
 * @since : 2022/10/09
 */
@Controller
@RequiredArgsConstructor
public class Oauth2TestController {
    private final UserRepository userRepository;
    private final HttpSession session;

    @GetMapping("/test/oauth2")
    public String login(Model model) {
        var userSession = (UserSession)session.getAttribute("user");
        var rawUser = Objects.isNull(userSession) ? null : userRepository.findById(userSession.getUserId()).get();
        var user = Objects.isNull(rawUser) ? new UserResponseDto() : new UserResponseDto(rawUser);
        model.addAttribute("user", user);
        return "/test/oauth2";
    }
}