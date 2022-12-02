package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.repository.UserRepository;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.domain.User;
import com.mimi.w2m.backend.type.dto.security.LoginInfo;
import com.mimi.w2m.backend.type.dto.security.OAuthAttributes;
import com.mimi.w2m.backend.type.dto.user.UserRequestDto;
import com.mimi.w2m.backend.type.response.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;

/**
 * UserService
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/19
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final HttpSession    httpSession;
    private final UserRepository userRepository;
    private final Logger         logger = LoggerFactory.getLogger(UserService.class.getName());

    /**
     * OAuth2 를 이용해 Login 을 수행하고, 가입되지 않은 이용자는 가입된다
     *
     * @author teddy
     * @since 2022/12/01
     **/
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth의 리소스 서버로부터 값을 대신 받아와준다.
        final OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // Kakao or google
        final var registrationId = userRequest.getClientRegistration()
                                              .getRegistrationId();

        // kakao : id, google : email. CK or PK를 의미한다(application-oauth.yaml 에 정의됨)
        final var userNameAttributeName = userRequest.getClientRegistration()
                                                     .getProviderDetails()
                                                     .getUserInfoEndpoint()
                                                     .getUserNameAttributeName();

        final Map<String, Object> userAttributes = oauth2User.getAttributes();
        final String              name           = String.valueOf(userAttributes.get("name"));
        final String              email          = String.valueOf(userAttributes.get("email"));

        final var attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oauth2User.getAttributes());

        // 회원가입 여부 확인
        final Optional<User> byEmail = getUserByEmail(email);
        final User           user;
        if(byEmail.isPresent()) {
            user = byEmail.get();
        } else {
            user = signup(name, email);
        }

        httpSession.setAttribute(LoginInfo.key, new LoginInfo(user.getId(), Role.USER));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole()
                                                                                          .getKey())),
                                     attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 이용자 정보를 저장한다
     *
     * @author paul
     * @since 2022-11-01
     */
    @Transactional
    protected User signup(String name, String email) {
        return userRepository.save(User.builder()
                                       .name(name)
                                       .email(email)
                                       .build());
    }

    /**
     * 가입된 이용자 정보 삭제
     *
     * @author teddy
     * @since 2022/11/19
     **/
    @Transactional
    public void deleteReal(Long userId) throws EntityNotFoundException {
        var user = get(userId);
        userRepository.delete(user);
    }

    /**
     * 가입된 이용자 정보 가져오기
     *
     * @author paul
     * @since 2022-11-01
     */
    public User get(Long id) throws EntityNotFoundException {
        final var user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
            final var formatter = new Formatter();
            final var msg = formatter.format("[UserService] Entity Not Found" + "(id=%d)", id)
                                     .toString();
            throw new EntityNotFoundException(msg);
        }
    }

    /**
     * 이용자 삭제하기(deletedAt 만 설정)
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Transactional
    public void deleteNotReal(Long userId) throws EntityNotFoundException {
        var user = get(userId);
        user.delete();
    }

    public User getByEmail(String email) throws EntityNotFoundException {
        final var user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            return user.get();
        } else {
            final var formatter = new Formatter();
            final var msg = formatter.format("[UserService] Entity Not Found" + "(email=%s)", email)
                                     .toString();
            throw new EntityNotFoundException(msg);
        }
    }
    // TODO: 2022/12/01 Email 을 변경 가능하도록 수정하기

    /**
     * 이용자 정보 변경하기. 현재 Email 을 변경할 수는 없다
     *
     * @author teddy
     * @since 2022/11/19
     **/
    @Transactional
    public User update(Long userId, UserRequestDto requestDto) throws EntityNotFoundException {
        final var user = get(userId);
        return user.updateName(requestDto.getName());
    }
}