package com.mimi.w2m.backend.dto.security;

import com.mimi.w2m.backend.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Getter
public class OAuthAttributes {
private final Map<String, Object> attributes;
private final String              nameAttributeKey;
private final String              name;
private final String              email;

@Builder
protected OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
    this.attributes       = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name             = name;
    this.email            = email;
}

public static OAuthAttributes of(String registerationId, String userNameAttributeName, Map<String, Object> attributes) {
    if(registerationId.equals("google")) {
        return ofGoogle(userNameAttributeName, attributes);
    } else if(registerationId.equals("kakao")) {
        return ofKakao(userNameAttributeName, attributes);
    } else {
        return null;
    }
}

private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
                          .name((String) attributes.get("name"))
                          .email((String) attributes.get("email"))
                          .attributes(attributes)
                          .nameAttributeKey(userNameAttributeName)
                          .build();
}

private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
    var kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
    var kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
    return OAuthAttributes.builder()
                          .name((String) kakaoProfile.get("nickname"))
                          .email((String) kakaoAccount.get("email"))
                          .attributes(attributes)
                          .nameAttributeKey(userNameAttributeName)
                          .build();
}

public User toEntity() {
    return User.builder()
               .name(name)
               .email(email)
               .build();
}
}