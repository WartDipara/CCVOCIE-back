package com.ela.ccvoice.common.websocket.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ws前端请求数据类型
 */
@AllArgsConstructor
@Getter
public enum WSRequestTypeEnum {
    LOGIN(1, "请求登录二维码"),
    HEARTBEAT(2, "心跳包"),
    AUTHORIZE(3, "登录认证"),
            ;
    private int type;
    private String desc;
    private static Map<Integer, WSRequestTypeEnum> cache;

    static {
        cache = Arrays.stream(WSRequestTypeEnum.values()).collect(Collectors.toMap(WSRequestTypeEnum::getType, Function.identity()));
    }

    public static WSRequestTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
