package com.ela.ccvoice.common.user.service;

import com.ela.ccvoice.common.user.domain.dto.UserLoginInfoDTO;
import com.ela.ccvoice.common.user.domain.dto.UserRegInfoDTO;
import com.ela.ccvoice.common.user.domain.vo.response.LoginResp;
import com.ela.ccvoice.common.user.domain.vo.response.RegisterResp;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/WartDipara">ela</a>
 * @since 2024-10-29
 */
public interface UserService {
    RegisterResp register(UserRegInfoDTO userRegInfoDTO);
    LoginResp login(UserLoginInfoDTO userLoginInfoDTO);
}
