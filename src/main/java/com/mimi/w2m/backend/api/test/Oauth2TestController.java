package com.mimi.w2m.backend.api.test;

import com.mimi.w2m.backend.repository.UserRepository;
import com.mimi.w2m.backend.dto.security.UserSession;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author : teddy
 * @since : 2022/10/09
 */
@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class Oauth2TestController {
    private final UserRepository userRepository;
    private final HttpSession session;
    private final Log logger = LogFactory.getLog(this.getClass());

    @GetMapping("/login/oauth2")
    public String login(Model model) {
        var userSession = (UserSession)session.getAttribute("user");
        logger.info(Objects.isNull(userSession) ? "empty" : userSession.getUserId());
        var rawUser = Objects.isNull(userSession) ? null : userRepository.findById(userSession.getUserId()).get();
        var user = Objects.isNull(rawUser) ? new UserResponseDto() : new UserResponseDto(rawUser);
        model.addAttribute("user", user);
        return "/test/oauth2";
    }

//    @GetMapping("/test/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//        if(auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/api/v1/test/login/oauth2";
//    }
}