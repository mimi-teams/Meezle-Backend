package com.mimi.w2m.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.util.Optional;

/**
 * JWT를 생성하고 인증을 담당하는 서비스
 *
 * @author yeh35
 * @since 2022-12-04
 */
@Service
public class JwtService {

    private final static String ISSUER = "mezzle";
    private final static String CLAIM_USER = "userid";

    private final Algorithm algorithm;

    public JwtService(
            @Value("${auth.token.secret}") String secretKeyString
    ) {
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
    public String createToken(long userid) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(CLAIM_USER, userid)
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
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify an specific claim validations
                    .withIssuer(ISSUER)
                    // reusable verifier instance
                    .build();

            decodedJWT = verifier.verify(token);

            final var userId = decodedJWT.getClaim(CLAIM_USER)
                    .asLong();

            return Optional.of(new TokenInfo(userId));
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            return Optional.empty();
        }

    }

    @Getter
    public static class TokenInfo {
        public final long userId;

        public TokenInfo(long id) {
            userId = id;
        }
    }

}
