package com.mimi.w2m.backend.service.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : teddy
 * @since : 2022/10/09
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomOAuth2AccountSecurityContextFactory.class)
public @interface WithMockCustomOAuth2Account {
    String username() default "teddy";
    String name() default "username";
    String email() default "teddy@super.com";
    String role() default "ROLE_USER";
    String registrationId() default "google";
}
