package com.ela.ccvoice.common.websocket.service;

import io.netty.channel.Channel;

public interface WebSocketService {
    void connect(Channel channel);
    void handleLoginReq(Channel channel);
    boolean scanLoginSuccess(Integer code, Long uid);
    void remove(Channel channel);

}
