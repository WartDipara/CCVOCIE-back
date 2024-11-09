package com.ela.ccvoice.common.websocket.service.adapter;

import com.ela.ccvoice.common.websocket.domain.enums.WSResponseTypeEnum;
import com.ela.ccvoice.common.websocket.domain.vo.response.WSBaseResponse;
import com.ela.ccvoice.common.websocket.domain.vo.response.WSLoginUrl;

public class WebSocketAdapter {
    public static WSBaseResponse<?> buildResp(String customerLoginUrl){
        WSBaseResponse<WSLoginUrl> resp=new WSBaseResponse<>();
        resp.setType(WSResponseTypeEnum.LOGIN_URL.getType());
        resp.setData(new WSLoginUrl(customerLoginUrl));
        return resp;
    }
}
