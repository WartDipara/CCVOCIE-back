package com.ela.ccvoice.common.common.constant;

public class RedisKey {
    private static final String BASE_KEY = "ccvoice:chat";
    /**
     * 用户token的key
     */
    public static final String USER_TOKEN_KEY = "userToken:uid_%d";

    public static String getKey(String key, Object... obj) {
        return BASE_KEY + String.format(key, obj);
    }
}
