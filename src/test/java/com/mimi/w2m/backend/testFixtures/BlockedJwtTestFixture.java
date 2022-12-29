package com.mimi.w2m.backend.testFixtures;

import com.mimi.w2m.backend.domain.BlockedJwt;

import java.time.LocalDateTime;

/**
 * BlockedJwtTestFixture
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/29
 **/
public class BlockedJwtTestFixture {
    public static BlockedJwt createBlockedJwt(String token, LocalDateTime expiredDate) {
        return new BlockedJwt(token, expiredDate);
    }
}