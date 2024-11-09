package com.ela.ccvoice.common.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.ela.ccvoice.common.user.common.utils.JsonUtils;
import com.ela.ccvoice.common.user.dao.UserDao;
import com.ela.ccvoice.common.user.domain.entity.User;
import com.ela.ccvoice.common.user.service.LoginService;
import com.ela.ccvoice.common.user.service.WebSocketService;
import com.ela.ccvoice.common.user.service.adapter.WebSocketAdapter;
import com.ela.ccvoice.common.websocket.domain.enums.WSResponseTypeEnum;
import com.ela.ccvoice.common.websocket.domain.vo.response.WSBaseResponse;
import com.ela.ccvoice.common.websocket.domain.vo.response.WSLoginUrl;
import com.ela.ccvoice.common.websocket.dto.WSChannelExtraDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    private LoginService loginService;

    public static final Duration DURATION = Duration.ofHours(1);
    public static final int MAXIMUM_PEOPLE_SIZE = 1000;
    /**
     * 临时保存登录code和channel的映射关系
     */
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_PEOPLE_SIZE)
            .expireAfterWrite(DURATION)
            .build();
    /**
     * 管理全部用户的链接，包括游客/登录态
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    private final UserDao userDao;

    public WebSocketServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void connect(Channel channel) {
        //链接时保存空对象，等认证完成后才去设置WSChannelExtraDTO中的uid值
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    @Override
    public void handleLoginReq(Channel channel) {
        //采用咖啡因的缓存，避免内存溢出，做过期策略

        //生成随机码
        Integer code =generateLoginCode(channel);
        //TODO 模拟登录传入的url
        String customerLoginUrl = "customerLoginUrl";
        //把url发送给前端
        sendMsg(channel, WebSocketAdapter.buildResp(customerLoginUrl));
    }

    private void sendMsg(Channel channel, WSBaseResponse<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    public synchronized Integer generateLoginCode(Channel channel) {
        Integer code;
        do{
            code = RandomUtil.randomInt(100000, 999999); //生成六位数随机码
        }while(Objects.isNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }

    public boolean scanLoginSuccess(Integer code, Long uid) {
//        //确认连接在该机器
//        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
//        if(Objects.isNull(channel)){
//            return Boolean.FALSE;
//        }
//        User user=userDao.getById(uid);
//        //移除code
//        WAIT_LOGIN_MAP.invalidate(code);
//        //调用用户登录模块
//        String token = loginService.login(uid);
//        //用户登录
//        loginSuccess(channel, user, token);
        return Boolean.TRUE;
    }

}
