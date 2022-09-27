package com.mimi.w2m.backend.api;

import com.mimi.w2m.backend.service.SimpleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 셈플용 API
 *
 * @since 2022-09-27
 * @auther yeh35
 */

@RequiredArgsConstructor
@RequestMapping("/simple")
@RestController
public class SimpleApi {

    private final SimpleService simpleService;

    @GetMapping("/")
    @ResponseBody
    public String getRoot() {
        simpleService.sdfadfa();
        return "hello";
    }
}
