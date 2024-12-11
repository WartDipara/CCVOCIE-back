package com.ela.ccvoice.common.websocket.utils;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * netty 工具类
 */
public class NettyUtil {
    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    public static AttributeKey<String> IP = AttributeKey.valueOf("ip");
    public static AttributeKey<Long> UID = AttributeKey.valueOf("uid");
    public static AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");

    /**
     * 设置
     * @param channel
     * @param attributeKey
     * @param data
     * @param <T>
     */
    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        Attribute<T> attr = channel.attr(attributeKey);
        attr.set(data);
    }

    /**
     * 获取
     * @param channel
     * @param ip
     * @return
     * @param <T>
     */
    public static <T> T getAttr(Channel channel, AttributeKey<T> ip) {
        return channel.attr(ip).get();
    }
}
