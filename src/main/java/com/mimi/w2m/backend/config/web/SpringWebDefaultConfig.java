package com.mimi.w2m.backend.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
public class SpringWebDefaultConfig {
    /**
     * No thread-bound request found Error 발생을 막기 위해 RequestContextListener 를 등록한다
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Bean
    public RequestContextListener getRequestContextListener() {
        return new RequestContextListener();
    }

}