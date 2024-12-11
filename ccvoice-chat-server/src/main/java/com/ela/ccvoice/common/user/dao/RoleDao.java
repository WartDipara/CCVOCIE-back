package com.ela.ccvoice.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ela.ccvoice.common.user.domain.entity.Role;
import com.ela.ccvoice.common.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * 角色表 服务实现类
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> {

}
