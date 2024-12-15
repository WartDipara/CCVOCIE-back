package com.ela.ccvoice.common.websocket.domain.vo.request;

import lombok.Data;

@Data
public class WsLoginRequest{
    private String token;
    private String username;
    private String password;
}
