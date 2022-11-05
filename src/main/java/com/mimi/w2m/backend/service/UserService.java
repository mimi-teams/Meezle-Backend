package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.dto.security.OAuthAttributes;
import com.mimi.w2m.backend.dto.security.UserSession;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import com.mimi.w2m.backend.error.BusinessException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Objects;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final Log logger = LogFactory.getLog(this.getClass());

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
        var user = signUpOrLoad(attributes);

        httpSession.setAttribute("user", new UserSession(user));
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())), // Authority를 설정할 때, ROLE_** 문자열이 넣어져야만 한다!!(우씨 몇 시간 잡아먹은거양..)
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    User signUpOrLoad(OAuthAttributes attributes) throws DuplicateKeyException {
        var user = userRepository.findByEmail(attributes.getEmail());
        return user.orElse(signup(attributes.toEntity()));
    }

    /**
     * 회원가입
     *
     * @author paul
     * @since 2022-11-01
     */
    @Transactional
    public User signup(User user) throws DuplicateKeyException {
        userRepository.findByName(user.getName())
                .ifPresent(entity -> {
                    throw new DuplicateKeyException(user.getName());
                });
        return userRepository.save(user);
    }


    /**
     * 유저 정보 가져오기
     *
     * @author paul
     * @since 2022-11-01
     */
    //TODO 케쉬 처리
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new EntityNotFoundException("존재하지 않는 유저 : " + userId, "존재하지 않는 유저");
        });
    }
    
    /**
     * 현재 로그인된 유저
     *
     * @since 2022-11-05
     * @author yeh35
     */
    public User getCurrentUser() {
        final var userSession = (UserSession)httpSession.getAttribute("user");
        
        if (Objects.isNull(userSession)) {
            throw new UnauthorizedException("로그인된 유저의 정보를 찾을 수 없습니다.");
        }

        try {
            return getUser(userSession.getUserId());
        } catch (BusinessException e) {
            logger.warn("로그인은 되어있는데 유저 정보가 없습니다.!! userId = " + userSession.getUserId());
            throw new UnauthorizedException("정보를 찾을 수 없습니다.", "유저 정보를 찾을 수 없습니다.");
        }
    }
}