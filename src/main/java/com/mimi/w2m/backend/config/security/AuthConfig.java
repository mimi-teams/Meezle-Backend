package com.mimi.w2m.backend.config.security;

/**
 * @author : teddy
 * @since : 2022/09/29
 */

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {
    private final UserService customOAuth2UserService;
    private final HttpSession session;

    /**
     * @return webSecurityCustomizer를 이용해 ignoring을 하는 것은 비권장 사항이다.
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
        http.authorizeRequests()
                .antMatchers("/api/v1/security/login/**").permitAll()
                .antMatchers("/api/v1/test/**").hasRole(Role.Tester.name())
                .antMatchers("/api/v1/**").hasAnyRole(Role.USER.name(), Role.Tester.name())
                .anyRequest().permitAll().and()
                .csrf().disable()
                .logout().logoutUrl("/api/v1/security/logout").addLogoutHandler(((request, response, authentication)-> {
                    if(!Objects.isNull(authentication)) {
                        new SecurityContextLogoutHandler().logout(request, response, authentication);
                    }
                    try {
                        response.sendRedirect("/");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .invalidateHttpSession(true).clearAuthentication(true).and()
                .oauth2Login()
                .defaultSuccessUrl("/")
                .userInfoEndpoint().userService(customOAuth2UserService);

        return http.build();
    }
}