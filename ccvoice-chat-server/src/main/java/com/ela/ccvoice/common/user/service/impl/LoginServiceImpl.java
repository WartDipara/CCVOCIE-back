package com.ela.ccvoice.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ela.ccvoice.common.constant.RedisKey;
import com.ela.ccvoice.common.user.common.utils.JwtUtils;
import com.ela.ccvoice.common.user.common.utils.RedisUtils;
import com.ela.ccvoice.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {
    public static final int TOKEN_EXPIRE_DAYS = 3;
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public void renewalToken(String token) {

    }
    @Override
    public String login(Long uid){
        String key =RedisKey.getKey(RedisKey.USER_TOKEN_KEY, uid);
        String token =RedisUtils.getStr(key);
        if(StrUtil.isNotBlank(token)){
            return token;
        }
        //获取用户token
        token = jwtUtils.createToken(uid);
        RedisUtils.set(key, token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);//token过期用redis中心化控制，初期采用3天过期，剩1天自动续期的方案。后续可以用双token实现
        return token;
    }
    @Override
    public Long getValidUid(String token){
        return null;
    }
}
