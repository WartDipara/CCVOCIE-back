package com.ela.ccvoice.common.websocket.domain.vo.response;

import com.ela.ccvoice.common.websocket.domain.enums.WSResponseTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSBaseResponse<T> {
    /**
     * @see WSResponseTypeEnum
     */
    private Integer type;
    private T data;
}
