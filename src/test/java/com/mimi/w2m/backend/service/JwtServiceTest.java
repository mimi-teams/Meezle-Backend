package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.service.JwtService.TokenInfo;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void test() {
        JwtService service = new JwtService("FkdyWJ/KOfcNnWZ7KOkEbxeayfXzyEsSMeag84VwAeM=");

        final int           userid            = 100;
        final String        token             = service.createToken(userid);
        Optional<TokenInfo> optionalTokenInfo = service.verify(token);

        assertTrue(optionalTokenInfo.isPresent());

        TokenInfo tokenInfo = optionalTokenInfo.get();

        assertEquals(userid, tokenInfo.userId);
    }

}