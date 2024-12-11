package com.ela.ccvoice.common.websocket.service;

import com.ela.ccvoice.common.user.domain.dto.UserLoginInfoDTO;
import io.netty.channel.Channel;

public interface WebSocketService {
    void connect(Channel channel);
    void handleLoginReq(Channel channel);
    boolean scanLoginSuccess(Integer code, UserLoginInfoDTO userLoginInfoDTO);
    void remove(Channel channel);

}
