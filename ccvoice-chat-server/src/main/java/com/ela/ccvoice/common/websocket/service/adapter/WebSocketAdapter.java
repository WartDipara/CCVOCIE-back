package com.ela.ccvoice.common.websocket.service.adapter;

import com.ela.ccvoice.common.user.domain.entity.User;
import com.ela.ccvoice.common.websocket.domain.enums.WSResponseTypeEnum;
import com.ela.ccvoice.common.websocket.domain.vo.response.WSBaseResponse;
import com.ela.ccvoice.common.websocket.domain.vo.response.WSLoginSuccess;

public class WebSocketAdapter {
    public static WSBaseResponse<?> buildResp(String msg){
        WSBaseResponse<String> resp=new WSBaseResponse<>();
        resp.setType(WSResponseTypeEnum.LOGIN_URL.getType());
        resp.setData(msg);
        return resp;
    }

    public static WSBaseResponse<WSLoginSuccess> buildLoginSuccessResp(User user, String token, boolean hasPower) {
        WSBaseResponse<WSLoginSuccess> wsBaseResp = new WSBaseResponse<>();
        wsBaseResp.setType(WSResponseTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess wsLoginSuccess = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .power(hasPower ? 1 : 0)
                .build();
        wsBaseResp.setData(wsLoginSuccess);
        return wsBaseResp;
    }
    public static WSBaseResponse<?> buildInvalidTokenResp(){
        WSBaseResponse<String> resp=new WSBaseResponse<>();
        resp.setType(WSResponseTypeEnum.INVALIDATE_TOKEN.getType());
        resp.setData(WSResponseTypeEnum.INVALIDATE_TOKEN.getDesc());
        return resp;
    }
}
