package com.ela.ccvoice.common.websocket.domain.enums;

import com.ela.ccvoice.common.websocket.domain.vo.response.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum WSResponseTypeEnum {
    LOGIN_SCAN_SUCCESS(1, "用户扫描成功等待授权", null),
    LOGIN_SUCCESS(2, "用户登录成功返回用户信息", WSLoginSuccess.class),
    MESSAGE(3, "新消息", WSMessage.class),
    ONLINE_OFFLINE_NOTIFY(4, "上下线通知", WSOnlineOfflineNotify.class),
    INVALIDATE_TOKEN(5, "前端的token失效,需要重新登录", null),
    BLACK(6, "拉黑用户", WSBlack.class),
    MARK(7, "消息标记", WSMsgMark.class),
    RECALL(8, "消息撤回", WSMsgRecall.class),
    APPLY(9, "好友申请", WSFriendApply.class),
    MEMBER_CHANGE(10, "成员变动", WSMemberChange.class),
            ;

    private final Integer type;
    private final String desc;
    private final Class dataClass;

    private static Map<Integer, WSResponseTypeEnum> cache;

    static {
        cache = Arrays.stream(WSResponseTypeEnum.values()).collect(Collectors.toMap(WSResponseTypeEnum::getType, Function.identity()));
    }

    public static WSResponseTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
