package com.ela.ccvoice.common.websocket.service;

import com.ela.ccvoice.common.user.domain.dto.UserLoginInfoDTO;
import com.ela.ccvoice.common.websocket.domain.vo.request.WsLoginRequest;
import io.netty.channel.Channel;

public interface WebSocketService {
    void connect(Channel channel);
    void handleLoginReq(Channel channel, WsLoginRequest wsLoginRequest);
    boolean scanLoginSuccess(Integer code, UserLoginInfoDTO userLoginInfoDTO);
    void remove(Channel channel);
}
