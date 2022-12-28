package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.config.interceptor.JwtHandler;
import com.mimi.w2m.backend.config.interceptor.JwtHandler.TokenInfo;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.repository.BlockedJwtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtHandlerTest {
    @Mock
    private BlockedJwtRepository blockedJwtRepository;
    private JwtHandler jwtHandler;

    @BeforeEach
    public void setup() {
        jwtHandler = new JwtHandler("FkdyWJ/KOfcNnWZ7KOkEbxeayfXzyEsSMeag84VwAeM=", blockedJwtRepository);
    }

    @Test
    void test() {
        final UUID userid = UUID.randomUUID();
        final String token = jwtHandler.createToken(userid, Role.USER);
        Optional<TokenInfo> optionalTokenInfo = jwtHandler.verify(token);

        assertTrue(optionalTokenInfo.isPresent());

        TokenInfo tokenInfo = optionalTokenInfo.get();

        assertEquals(userid, tokenInfo.userId);
    }

}