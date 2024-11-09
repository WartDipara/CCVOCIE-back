package com.ela.ccvoice.common.user.dao;

import com.ela.ccvoice.common.user.domain.entity.User;
import com.ela.ccvoice.common.user.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/WartDipara">ela</a>
 * @since 2024-10-29
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

}
