package com.mimi.w2m.backend.service.security;

import com.mimi.w2m.backend.domain.user.User;
import com.mimi.w2m.backend.domain.user.UserRepository;
import com.mimi.w2m.backend.dto.security.OAuthAttributes;
import com.mimi.w2m.backend.dto.security.Role;
import com.mimi.w2m.backend.dto.security.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var delegate = new DefaultOAuth2UserService();
        var oauth2User = delegate.loadUser(userRequest);
        var registrationId = userRequest.getClientRegistration().getRegistrationId();
        var userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        var attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oauth2User.getAttributes());
        var user = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new UserSession(user));
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.USER.name())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        var user =  userRepository.findByName(attributes.getName())
                .map(entity -> entity.update(attributes.getName(), attributes.getEmail()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}