package com.ela.ccvoice.common.websocket.domain.vo.request;

import com.ela.ccvoice.common.websocket.domain.enums.WSRequestTypeEnum;
import lombok.Data;

@Data
public class WSBaseRequest {
    /**
     * @see WSRequestTypeEnum
     */
    private int type;
    private String data;
}
