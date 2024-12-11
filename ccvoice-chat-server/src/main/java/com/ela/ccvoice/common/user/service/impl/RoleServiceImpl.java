package com.ela.ccvoice.common.user.service.impl;
import com.ela.ccvoice.common.user.domain.enums.RoleEnum;
import com.ela.ccvoice.common.user.service.IRoleService;
import com.ela.ccvoice.common.user.service.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
 * 角色管理
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {//超级管理员无敌的好吧，后期做成权限=》资源模式
        Set<Long> roleSet = userCache.getRoleSet(uid);
        return isAdmin(roleSet) || roleSet.contains(roleEnum.getId());
    }

    private boolean isAdmin(Set<Long> roleSet) {
        return Objects.requireNonNull(roleSet).contains(RoleEnum.ADMIN.getId());
    }
}
