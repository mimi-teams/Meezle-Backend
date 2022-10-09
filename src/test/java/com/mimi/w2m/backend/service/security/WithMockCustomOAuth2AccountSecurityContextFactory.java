package com.mimi.w2m.backend.service.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;

/**
 * @author : teddy
 * @since : 2022/10/09
 */

public class WithMockCustomOAuth2AccountSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomOAuth2Account> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomOAuth2Account annotation) {
        var context = SecurityContextHolder.createEmptyContext();

        var attributes = new HashMap<String, Object>();
        attributes.put("username", annotation.username());
        attributes.put("name", annotation.name());
        attributes.put("email", annotation.email());

        var principal = new DefaultOAuth2User(
                List.of(new OAuth2UserAuthority(annotation.role(), attributes)),
                attributes,
                annotation.name());
        var token = new OAuth2AuthenticationToken(
                principal, principal.getAuthorities(), annotation.registrationId()
        );
        context.setAuthentication(token);
        return context;
    }
}