package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.dto.security.OAuthAttributes;
import com.mimi.w2m.backend.dto.security.UserSession;
import com.mimi.w2m.backend.error.EntityDuplicatedException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Comment;
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
import java.util.Optional;

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
private final Log            logger = LogFactory.getLog(this.getClass());

@Comment("OAuth2를 사용해 login을 처리하고, 이용자 정보를 저장 및 갱신하기 위해 이용한다")
@Override
public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    var delegate   = new DefaultOAuth2UserService();
    var oauth2User = delegate.loadUser(userRequest);
    // kakao or google
    var registrationId = userRequest.getClientRegistration().getRegistrationId();
    // kakao : id, google : email. CK or PK를 의미한다(application-oauth.yaml에 정의됨)
    var userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                           .getUserInfoEndpoint().getUserNameAttributeName();
    var attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oauth2User.getAttributes());
    var user       = signUpOrLoad(attributes);

    httpSession.setAttribute("user", new UserSession(user));
    return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())), // Authority를 설정할 때, ROLE_**
            // 문자열이 넣어져야만 한다!!(우씨 몇 시간 잡아먹은거양..)
            attributes.getAttributes(),
            attributes.getNameAttributeKey()
    );
}

@Comment("DB를 조회해 가입된 이메일인지 확인하고, 신규 가입을 진행한다")
User signUpOrLoad(OAuthAttributes attributes) {
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
public User signup(User user) {
    return userRepository.save(user);
}

/**
 * 현재 로그인된 유저
 *
 * @author yeh35
 * @since 2022-11-05
 */
public User getCurrentUser() throws UnauthorizedException {
    Long userId = -1L;
    try {
        var userSession =
                Optional.ofNullable((UserSession) httpSession.getAttribute("user"))
                        .orElseThrow(() -> new UnauthorizedException("로그인된 유저의 정보를 찾을 수 없습니다."));
        userId = userSession.getUserId();
        return getUser(userId);
    } catch(EntityNotFoundException e) {
        logger.warn("로그인은 되어있는데 유저 정보가 없습니다.!! userId = " + userId);
        throw new UnauthorizedException("정보를 찾을 수 없습니다.", "유저 정보를 찾을 수 없습니다.");
    }
}

/**
 * 유저 정보 가져오기
 *
 * @author paul
 * @since 2022-11-01
 */
public User getUser(Long userId) throws EntityNotFoundException {
    return userRepository.findById(userId).orElseThrow(() -> {
        throw new EntityNotFoundException("존재하지 않는 유저 : " + userId, "존재하지 않는 유저");
    });
}

/**
 * 이용자 삭제하기
 *
 * @author teddy
 * @since 2022/11/19
 **/
public void removeUser(Long userId) throws EntityNotFoundException {
    var user = userRepository.findById(userId)
                             .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저 : " + userId
                                     , "존재하지 않는 유저"));
    userRepository.delete(user);
}

/**
 * 이용자 정보 변경하기(name, email) Email의 경우, Google이나 Kakao의 중복되지 않은 이메일이어야 한다
 *
 * @author teddy
 * @since 2022/11/19
 **/
public User updateUser(Long userId, String name, String email) throws EntityNotFoundException,
                                                                      EntityDuplicatedException {
    var user = userRepository.findById(userId)
                             .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저 : " + userId
                                     , "존재하지 않는 유저"));
    userRepository.findByEmail(email)
                  .ifPresent((User entity) -> {
                      throw new EntityDuplicatedException("이미 존재하는 유저 : " + email, "이미 존재하는 유저");
                  });
    return user.update(name, email);
}
}