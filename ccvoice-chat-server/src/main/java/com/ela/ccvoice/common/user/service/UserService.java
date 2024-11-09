package com.ela.ccvoice.common.user.service;

import com.ela.ccvoice.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/WartDipara">ela</a>
 * @since 2024-10-29
 */
public interface UserService {
    void register(User user);
}
