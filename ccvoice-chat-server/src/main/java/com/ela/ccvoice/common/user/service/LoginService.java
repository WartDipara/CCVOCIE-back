package com.ela.ccvoice.common.user.service;

public interface LoginService {
    /**
     * 刷新token有效期,如果有需要的话
     * @param token
     */
    void renewalToken(String token);

    /**
     * 登陆成功 获得token
     * @param uid
     * @return token
     */
    String login(Long uid);

    /**
     * 如果token有效，返回uid
     * @param token
     * @return uid
     */
    Long getValidUid(String token);
}
