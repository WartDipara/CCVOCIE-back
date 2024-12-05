package com.ela.ccvoice.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ela.ccvoice.common.common.constant.RedisKey;
import com.ela.ccvoice.common.common.utils.JwtUtils;
import com.ela.ccvoice.common.common.utils.RedisUtils;
import com.ela.ccvoice.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {
    public static final int TOKEN_EXPIRE_DAYS = 3;
    public static final int TOKEN_RENEWAL_DAYS = 1;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    @Async
    public void renewalToken(String token) {
        Long validUid = getValidUid(token);
        String userTokenKey = getUserTokenKey(validUid);
        Long expireDays= RedisUtils.getExpire(userTokenKey,TimeUnit.DAYS);
        if(expireDays==-2){
            //key不存在
            return;
        }else if(expireDays< TOKEN_RENEWAL_DAYS){
            //续期
            RedisUtils.expire(userTokenKey,TOKEN_EXPIRE_DAYS,TimeUnit.DAYS);
        }
    }

    @Override
    public String login(Long uid) {
        String key = getUserTokenKey(uid);
        String token = RedisUtils.getStr(key);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        //获取用户token
        token = jwtUtils.createToken(uid);
        RedisUtils.set(key, token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);//token过期用redis中心化控制，初期采用3天过期，剩1天自动续期的方案。后续可以用双token实现
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUid(token);
        if (Objects.isNull(uid)) {
            return null;
        }
        String oldToken = RedisUtils.get(getUserTokenKey(uid)); //老token值
        if (StringUtils.isBlank(oldToken)) {
            return null;
        }
        //老token和新token得一样才返回
        return Objects.equals(oldToken, token) ? uid : null;
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_KEY, uid);
    }
}
