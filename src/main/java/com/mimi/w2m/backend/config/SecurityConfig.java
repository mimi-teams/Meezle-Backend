package com.mimi.w2m.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 보안 관련 설정
 * 
 * @since 2022-12-17
 * @author yeh35
 */
@Configuration
public class SecurityConfig {

    /**
     * 비밀번호를 암호화하는 Encoder 추가하기
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
