package com.ela.ccvoice.common.websocket.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.ela.ccvoice.common.common.constant.RedisKey;
import com.ela.ccvoice.common.common.event.UserOfflineEvent;
import com.ela.ccvoice.common.common.event.UserOnlineEvent;
import com.ela.ccvoice.common.common.utils.RedisUtils;
import com.ela.ccvoice.common.user.dao.UserDao;
import com.ela.ccvoice.common.user.domain.dto.UserLoginInfoDTO;
import com.ela.ccvoice.common.user.domain.entity.User;
import com.ela.ccvoice.common.user.domain.enums.RoleEnum;
import com.ela.ccvoice.common.user.service.IRoleService;
import com.ela.ccvoice.common.user.service.LoginService;
import com.ela.ccvoice.common.user.service.cache.UserCache;
import com.ela.ccvoice.common.websocket.service.WebSocketService;
import com.ela.ccvoice.common.websocket.service.adapter.WebSocketAdapter;
import com.ela.ccvoice.common.websocket.domain.vo.response.WSBaseResponse;
import com.ela.ccvoice.common.websocket.dto.WSChannelExtraDTO;
import com.ela.ccvoice.common.websocket.utils.NettyUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserCache userCache;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public static final Duration DURATION = Duration.ofHours(1);
    public static final int MAXIMUM_PEOPLE_SIZE = 1000;
    private static final Duration EXPIRE_TIME = Duration.ofHours(1);
    private static final String LOGIN_CODE = "loginCode";
    /**
     * 所有在线的用户和对应的socket
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Channel, WSChannelExtraDTO> getOnlineMap() {
        return ONLINE_WS_MAP;
    }

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
    /**
     * 存储用户uid和channel的映射关系，方便根据uid找到对应的channel
     */
    private final UserDao userDao;

    public WebSocketServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void connect(Channel channel) {
        //链接时保存空对象，等认证完成后才去设置WSChannelExtraDTO中的uid值
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    /**
     * 使用ws登录，传入type1的情况
     * 采用咖啡因的缓存，避免内存溢出，做过期策略
     *
     * @param channel
     */
    @Override
    public void handleLoginReq(Channel channel) {
        //生成随机码
        Integer code = generateLoginCode(channel);
        //传入登录的接口
        String customerLoginUrl = "http://localhost:8080/auth/login";
        //把url发送给前端
        sendMsg(channel, WebSocketAdapter.buildResp(customerLoginUrl));
    }

    private void sendMsg(Channel channel, WSBaseResponse<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    /**
     * 获取不重复的登录 code，确保不超过 int 的存储极限。
     * 防止并发问题，可通过本地缓存时间和 Redis key 的过期时间控制。
     *
     * @param channel 通道
     * @return 不重复的登录 code
     */
    private Integer generateLoginCode(Channel channel) {
        Integer code;
        // 循环生成，直到获取到一个不重复的登录 code
        do{
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        }while(Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        // 本地缓存存储一份
        WAIT_LOGIN_MAP.put(code, channel);
        return code;
    }



    /**
     * 检测登录是否成功
     *
     * @param code
     * @param uid
     * @return
     */
    public boolean scanLoginSuccess(Integer code, UserLoginInfoDTO userLoginInfoDTO) {
        //确认连接在该机器
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return Boolean.FALSE;
        }
        User user = userDao.getByName(userLoginInfoDTO.getName());
        //移除code
        WAIT_LOGIN_MAP.invalidate(code);
        //调用用户登录模块
        String token = loginService.login(user.getId());
        //用户登录
        loginSuccess(channel, user, token);
        return Boolean.TRUE;
    }

    /**
     * 如果在线列表不存在，就先把该channel放进在线列表
     *
     * @param channel
     * @return
     */
    private WSChannelExtraDTO getOrInitChannelExt(Channel channel) {
        WSChannelExtraDTO wsChannelExtraDTO =
                ONLINE_WS_MAP.getOrDefault(channel, new WSChannelExtraDTO());
        WSChannelExtraDTO old = ONLINE_WS_MAP.putIfAbsent(channel, wsChannelExtraDTO);
        return ObjectUtil.isNull(old) ? wsChannelExtraDTO : old;
    }

    /**
     * 用户上线
     */
    private void online(Channel channel, Long uid) {
        getOrInitChannelExt(channel).setUid(uid);
        ONLINE_UID_MAP.putIfAbsent(uid, new CopyOnWriteArrayList<>());
        ONLINE_UID_MAP.get(uid).add(channel);
        NettyUtil.setAttr(channel, NettyUtil.UID, uid);
    }

    /**
     * (channel必在本地)登录成功，并更新状态
     */
    private void loginSuccess(Channel channel, User user, String token) {
        //更新上线列表
        online(channel, user.getId());
        //返回给用户登录成功
        boolean hasPower = iRoleService.hasPower(user.getId(), RoleEnum.CHAT_MANAGER);
        //发送给对应的用户
        sendMsg(channel, WebSocketAdapter.buildLoginSuccessResp(user, token, hasPower));
        //发送用户上线事件
        boolean online = userCache.isOnline(user.getId());
        if (!online) {
            user.setLastOptTime(new Date());
//            user.refreshIp(NettyUtil.getAttr(channel, NettyUtil.IP));
            applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
        }
    }

    /**
     * 用户下线
     *
     * @param channel     通道
     * @param uidOptional 用户ID（可选）
     * @return 是否全部下线成功
     */
    public boolean offline(Channel channel, Optional<Long> uidOptional) {
        ONLINE_WS_MAP.remove(channel);
        return uidOptional.map(uid -> {
            // 获取用户的通道列表
            CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uid);
            if (CollectionUtil.isNotEmpty(channels)) {
                channels.removeIf(ch -> Objects.equals(ch, channel));
            }
            return CollectionUtil.isEmpty(channels);
        }).orElse(true);
    }

    /**
     * 处理断开连接
     *
     * @param channel
     */
    @Override
    public void remove(Channel channel) {
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        Optional<Long> uidOptional = Optional.ofNullable(wsChannelExtraDTO).map(WSChannelExtraDTO::getUid);
//        ONLINE_WS_MAP.remove(channel);//移除
        boolean isAllOffline = offline(channel, uidOptional);
        if (uidOptional.isPresent() && isAllOffline) {
            User user = new User();
            user.setId(uidOptional.get());
            user.setLastOptTime(new Date());
            applicationEventPublisher.publishEvent(new UserOfflineEvent(this, user));
        }
    }

}
