package com.ela.ccvoice.common.user.domain.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(value = "注册响应对象")
public class RegisterResp {
    @ApiModelProperty(value = "判断是否注册登录")
    private boolean success;
    @ApiModelProperty(value = "注册消息")
    private String message;
}
