package com.ela.ccvoice.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ela.ccvoice.common.user.domain.entity.UserRole;
import com.ela.ccvoice.common.user.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * 用户角色关系表 服务实现类
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> {
    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getUid, Objects.requireNonNull(uid))
                .list();
    }
}
