package com.ela.ccvoice.common.common.exception;

import com.ela.ccvoice.common.common.domain.vo.response.ApiResult;
import com.ela.ccvoice.common.common.utils.JsonUtils;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public enum HttpErrorEnum {
    ACCESS_DENIED(401, "登陆失败，需要重新登陆");

    private Integer code;
    private String msg;

    public void sendHttpError(javax.servlet.http.HttpServletResponse response) throws IOException {
        response.setStatus(code);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(JsonUtils.toStr(ApiResult.fail(code, msg)));
    }
}
