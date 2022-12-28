package com.mimi.w2m.backend.config.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mimi.w2m.backend.domain.BlockedJwt;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.repository.BlockedJwtRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * JWT를 생성하고 인증을 담당하는 서비스
 *
 * @author yeh35
 * @since 2022-12-04
 */
@Service
@EnableAsync
@EnableScheduling
@Transactional(readOnly = true)
public class JwtHandler {

    private final static String ISSUER = "mezzle";
    private final static String CLAIM_USER = "USER_ID";
    private final static String CLAIM_USER_ROLE = "USER_ROLE";

    private final Algorithm algorithm;
    private final BlockedJwtRepository blockedJwtRepository;

    public JwtHandler(
            @Value("${auth.token.secret}") String secretKeyString,
            BlockedJwtRepository blockedJwtRepository) {
        this.blockedJwtRepository = blockedJwtRepository;
        byte[] secretKey = Base64Utils.decodeFromString(secretKeyString);
        assert secretKey.length == 32 : "secret key 길이는 32byte 여야 합니다.";
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    /**
     * 토큰 생성
     *
     * @author yeh35
     * @since 2022-12-04
     */
    public String createToken(UUID userid, Role role) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(CLAIM_USER, userid.toString())
                .withClaim(CLAIM_USER_ROLE, role.getKey())
                .sign(algorithm);
    }

    /**
     * 토큰을 검증해서 토큰 정보를 넘겨준다.
     *
     * @return 토큰이 올바르지 않은 경우 empty
     * @author yeh35
     * @since 2022-12-04
     */
    public Optional<TokenInfo> verify(String token) {
        final DecodedJWT decodedJWT;

        try {
            blockedJwtRepository.findById(token).ifPresent((BlockedJwt jwt) -> {
                throw new JWTVerificationException("Blocked Jwt Token");
            });

            JWTVerifier verifier = JWT.require(algorithm)
                    // specify a specific claim validations
                    .withIssuer(ISSUER)
                    // reusable verifier instance
                    .build();

            decodedJWT = verifier.verify(token);

            final var userId = UUID.fromString(decodedJWT.getClaim(CLAIM_USER).asString());
            final var role = Role.ofKey(decodedJWT.getClaim(CLAIM_USER_ROLE).asString());

            return Optional.of(new TokenInfo(userId, role));
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            return Optional.empty();
        }
    }

    /**
     * Logout 된 Jwt Token 을 정기적으로 삭제시키기
     *
     * @author teddy
     * @since 2022/12/28
     **/
    @Async
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 30)
    @Transactional
    public void deleteBlockedToken() {
        var tokens = blockedJwtRepository.findAll().stream()
                .filter((BlockedJwt blockedJwt) ->
                        LocalDateTime.now().isAfter(
                                blockedJwt.getCreatedDate().plus(25, ChronoUnit.MINUTES)
                        ))
                .toList();
        blockedJwtRepository.deleteAll(tokens);
    }


    public static class TokenInfo {
        public final UUID userId;
        public final Role role;

        public TokenInfo(UUID id, Role role) {
            this.userId = id;
            this.role = role;
        }
    }

}
