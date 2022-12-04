package com.mimi.w2m.backend;

import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

import java.security.SecureRandom;

public class SimpleTest {

    @Test
    void testKeyGenerator() {
        SecureRandom secRandom = new SecureRandom();

        byte[] key = new byte[32];
        secRandom.nextBytes(key);
        System.out.println(new String(Base64Utils.encode(key)));
    }

}
