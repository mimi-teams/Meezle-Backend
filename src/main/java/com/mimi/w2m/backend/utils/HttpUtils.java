package com.mimi.w2m.backend.utils;

public class HttpUtils {

    public static String withBearerToken(String token) {
        return "Bearer " + token;
    }
}
