package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.converter.db.OAuth2PlatformTypeConverter;
import com.mimi.w2m.backend.domain.type.OAuth2PlatformType;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Oauth2Token
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/08
 **/
@Entity
@Getter
@Table(name = "oauth2_token")
public class Oauth2Token extends BaseTimeEntity {
    @Id
    @GenericGenerator(name = "sequential_uuid", strategy = "com.mimi.w2m.backend.domain.generator.SequentialUUIDGenerator")
    @GeneratedValue(generator = "sequential_uuid")
    @Column(name = "oauth2_token_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;
    @Column(name = "access_token", unique = true, nullable = false)
    private String accessToken;
    @Convert(converter = OAuth2PlatformTypeConverter.class)
    @Column(name = "platform_type", length = 10, nullable = false, columnDefinition = "VARCHAR(10)")
    private OAuth2PlatformType platformType;
    @Column(name = "access_token_expires", nullable = false)
    private LocalDateTime accessTokenExpires;
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;
    @Column(name = "refresh_token_expires", nullable = false)
    private LocalDateTime refreshTokenExpires;

    @Builder
    public Oauth2Token(User user, OAuth2PlatformType platformType, String accessToken, LocalDateTime accessTokenExpires,
                       String refreshToken, LocalDateTime refreshTokenExpires) {
        this.user = user;
        this.platformType = platformType;
        this.accessToken = accessToken;
        this.accessTokenExpires = accessTokenExpires;
        this.refreshToken = refreshToken;
        this.refreshTokenExpires = refreshTokenExpires;
    }

    protected Oauth2Token() {
    }
}