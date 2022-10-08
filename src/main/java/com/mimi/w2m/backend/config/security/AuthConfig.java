package com.mimi.w2m.backend.config.security;

/**
 * @author : teddy
 * @since : 2022/09/29
 */

import com.mimi.w2m.backend.dto.security.Role;
import com.mimi.w2m.backend.service.security.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     *
     * @return
     *
     * webSecurityCustomizer를 이용해 ignoring을 하는 것은 비권장 사항이다.
     * SecurityFilterChain과 WebSecurityCustomizer를 구분하여 작성하지 말고, SecurityFilterChain으로 통합해 작성하자!
     * 대응하는 chain의 구문: http.authorizeRequests().antMatcher(....).permitAll() : antMathcer에 해당하는 uri를 모두에게 개방한다
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//
//        return web -> {
//            web.ignoring().antMatchers("/resources/**");
//            web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//        };
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .and().authorizeRequests().antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/")
                .and().oauth2Login().defaultSuccessUrl("/").userInfoEndpoint().userService(customOAuth2UserService);

        return http.build();
    }
}
