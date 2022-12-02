package com.mimi.w2m.backend.config.web.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringWebValidationConfig
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/02
 **/
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = SpringWebValidationConfig.class)
public class SpringWebValidationConfig implements WebMvcConfigurer {
    /**
     * Api 에서 Query 혹은 Path Variables 의 Validation 을 위해 등록한다
     *
     * @author teddy
     * @since 2022/12/02
     **/
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}