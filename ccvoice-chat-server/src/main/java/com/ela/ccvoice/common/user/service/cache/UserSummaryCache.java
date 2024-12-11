package com.ela.ccvoice.common.user.service.cache;

import com.ela.ccvoice.common.common.constant.RedisKey;
import com.ela.ccvoice.common.common.service.AbstractRedisStringCache;
import com.ela.ccvoice.common.user.domain.dto.SummeryInfoDTO;
import com.ela.ccvoice.common.user.domain.entity.IpDetail;
import com.ela.ccvoice.common.user.domain.entity.IpInfo;
import com.ela.ccvoice.common.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户所有信息的缓存
 */
@Component
public class UserSummaryCache extends AbstractRedisStringCache<Long, SummeryInfoDTO> {
    @Autowired
    private UserInfoCache userInfoCache;

    @Override
    protected String getKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_SUMMARY_STRING, uid);
    }

    @Override
    protected Long getExpireSeconds() {
        return 10 * 60L;
    }

    @Override
    protected Map<Long, SummeryInfoDTO> load(List<Long> uidList) {//后续可优化徽章信息也异步加载
        //用户基本信息
        Map<Long, User> userMap = userInfoCache.getBatch(uidList);
        //用户最后一次更新时间
        return uidList.stream().map(uid -> {
            SummeryInfoDTO summeryInfoDTO = new SummeryInfoDTO();
            User user = userMap.get(uid);
            if (Objects.isNull(user)) {
                return null;
            }
            summeryInfoDTO.setUid(user.getId());
            summeryInfoDTO.setName(user.getName());
            summeryInfoDTO.setAvatar(user.getAvatar());
//            summeryInfoDTO.setLocPlace(Optional.ofNullable(user.getIpInfo()).map(IpInfo::getUpdateIpDetail).map(IpDetail::getCity).orElse(null));
            return summeryInfoDTO;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SummeryInfoDTO::getUid, Function.identity()));
    }
}
