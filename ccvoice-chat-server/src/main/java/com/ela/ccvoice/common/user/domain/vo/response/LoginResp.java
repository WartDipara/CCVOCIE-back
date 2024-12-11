package com.ela.ccvoice.common.user.domain.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "登录响应对象")
public class LoginResp {
    @ApiModelProperty(value = "token")
    private String token;
    @ApiModelProperty(value = "判断是否成功登录")
    private boolean success;
    @ApiModelProperty(value = "登录消息")
    private String message;
    @ApiModelProperty(value = "用户id")
    private Long uid;

    public LoginResp(String token, boolean success, String message){
        this.token = token;
        this.success = success;
        this.message = message;
    }
}
