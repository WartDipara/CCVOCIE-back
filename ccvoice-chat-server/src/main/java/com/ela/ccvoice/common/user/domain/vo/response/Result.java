package com.ela.ccvoice.common.user.domain.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("基础APi返回类型")
public class Result<T> {
    @ApiModelProperty("成功或失败 标识符")
    private boolean success;
    @ApiModelProperty("错误代码")
    private Integer code;
    @ApiModelProperty("错误信息")
    private String msg;
    @ApiModelProperty("返回对象")
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(200);
        result.setData(null);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(200);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setMsg(msg);
        result.setCode(code);
        result.setSuccess(Boolean.FALSE);
        return result;
    }

    //TODO 后续更新错误常量,应该是传一个错误类型进来，ErrEnums而不是传一个string
    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<>();
        result.setMsg(msg);
        result.setCode(500);
        return result;
    }

    public boolean isSuccess() {
        return this.success;
    }
}
