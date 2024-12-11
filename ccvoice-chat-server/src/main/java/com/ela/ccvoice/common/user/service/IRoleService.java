package com.ela.ccvoice.common.user.service;

import com.ela.ccvoice.common.user.domain.enums.RoleEnum;

/**
 * 角色表 服务类
 */
public interface IRoleService {

    /**
     * 是否有某个权限，临时做法
     *
     * @return
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);

}
