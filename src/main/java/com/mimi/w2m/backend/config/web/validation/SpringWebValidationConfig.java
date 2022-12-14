package com.mimi.w2m.backend.config.web.validation;

import com.mimi.w2m.backend.config.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringWebValidationConfig
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/02
 **/
@RequiredArgsConstructor
// //이걸 사용하는 순간 더이상 Spring
@Configuration
//@ComponentScan(basePackageClasses = SpringWebValidationConfig.class)
public class SpringWebValidationConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;


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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**").allowedOrigins("*"); // CORS 전체 허용
    }
}