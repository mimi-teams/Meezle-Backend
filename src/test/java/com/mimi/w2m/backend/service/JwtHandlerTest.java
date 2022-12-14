package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.config.interceptor.JwtHandler;
import com.mimi.w2m.backend.config.interceptor.JwtHandler.TokenInfo;
import com.mimi.w2m.backend.domain.type.Role;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JwtHandlerTest {

    @Test
    void test() {
        JwtHandler service = new JwtHandler("FkdyWJ/KOfcNnWZ7KOkEbxeayfXzyEsSMeag84VwAeM=");

        final int userid = 100;
        final String token = service.createToken(userid, Role.USER);
        Optional<TokenInfo> optionalTokenInfo = service.verify(token);

        assertTrue(optionalTokenInfo.isPresent());

        TokenInfo tokenInfo = optionalTokenInfo.get();

        assertEquals(userid, tokenInfo.userId);
    }

}