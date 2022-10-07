package com.mimi.w2m.backend.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author : teddy
 * @since : 2022/10/02
 */
@RestController
@RequiredArgsConstructor
public class loginController {
    private final HttpSession httpSession;
}