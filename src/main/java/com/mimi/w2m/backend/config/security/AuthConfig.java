package com.mimi.w2m.backend.config.security;

import com.mimi.w2m.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * AuthConfig
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/22
 **/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {
    private final UserService customOAuth2UserService;

    /**
     * @return webSecurityCustomizer를 이용해 ignoring을 하는 것은 비권장 사항이다. SecurityFilterChain과 WebSecurityCustomizer를 구분하여
     * 작성하지 말고, SecurityFilterChain으로 통합해 작성하자! 대응하는 chain의 구문: http.authorizeRequests().antMatcher(....).permitAll() :
     * antMathcer에 해당하는 uri를 모두에게 개방한다
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //    http.authorizeRequests()
        //        .antMatchers("/").permitAll()
        //        .antMatchers("/meezle/api/v1/users/login").permitAll()
        //        .antMatchers("/meezle/api/v1/users/**").hasAuthority(Role.USER.getKey())
        //        .antMatchers("/meezle/api/v1/participants/login").permitAll()
        //        .antMatchers("/meezle/api/v1/participants/**").hasAuthority(Role.PARTICIPANT.getKey())
        //        .antMatchers("/meezle/api/v1/events/**").hasAnyAuthority(Role.USER.getKey(), Role.PARTICIPANT
        //        .getKey())
        //        .anyRequest().permitAll().and()
        //        .logout().disable()
        //        .oauth2Login()
        //        .defaultSuccessUrl("/")
        //        .userInfoEndpoint().userService(customOAuth2UserService);

        http.authorizeRequests()
            .anyRequest().permitAll().and()
            .logout().disable()
            .oauth2Login()
            .defaultSuccessUrl("/")
            .userInfoEndpoint().userService(customOAuth2UserService);

        return http.build();
    }
}