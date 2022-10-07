package com.mimi.w2m.backend.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Configuration
@RequiredArgsConstructor
public class SpringWebConfig implements WebMvcConfigurer {
    private final LoginArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolverList) {
        argumentResolverList.add(loginUserArgumentResolver);
    }
}