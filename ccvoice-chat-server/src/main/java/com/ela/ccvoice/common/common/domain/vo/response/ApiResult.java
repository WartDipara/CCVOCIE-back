package com.ela.ccvoice.common.common.domain.vo.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlType;

/**
 * 返回给前端的统一返回值接口
 */
@Data
public class ApiResult<T> {
    private Boolean isSuccess;
    private Integer code;
    private String errMsg;
    private T data;

    /**
     * 空返回
     * code = 200
     *
     * @return
     */
    public static <T> ApiResult<T> success() {
        ApiResult<T> result = new ApiResult<>();
        result.setIsSuccess(Boolean.TRUE);
        result.setCode(200);
        return result;
    }

    /**
     * 带参返回
     * code = 201
     *
     * @param msg
     * @return
     */
    public static <T> ApiResult<T> success(T msg) {
        ApiResult<T> result = new ApiResult<>();
        result.setIsSuccess(Boolean.TRUE);
        result.setCode(201);
        result.setData(msg);
        return result;
    }

    /**
     * 空错误返回
     * code = -1
     *
     * @return
     */
    public static <T> ApiResult<T> fail() {
        ApiResult<T> result = new ApiResult<>();
        result.setIsSuccess(Boolean.FALSE);
        result.setCode(-1);
        return result;
    }

    public static <T> ApiResult<T> fail(T msg){
        return fail(msg.toString());
    }
    public static <T> ApiResult<T> fail(String error){
        return fail(-2,error);
    }
    /**
     * 带参错误返回
     * code = -2
     *
     * @param error
     * @return
     */
    public static <T> ApiResult<T> fail(Integer code,String error) {
        ApiResult<T> result = new ApiResult<>();
        result.setIsSuccess(Boolean.FALSE);
        result.setCode(code);
        result.setErrMsg(error);
        return result;
    }

    public boolean checkSuccess() {
        return this.isSuccess;
    }
}
