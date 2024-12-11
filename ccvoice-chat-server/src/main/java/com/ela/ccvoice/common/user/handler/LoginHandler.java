package com.ela.ccvoice.common.user.handler;

import com.ela.ccvoice.common.user.domain.dto.UserLoginInfoDTO;
import com.ela.ccvoice.common.user.domain.vo.response.LoginResp;
import com.ela.ccvoice.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginHandler {
    @Autowired
    private UserService userService;
    public LoginResp handle(UserLoginInfoDTO userLoginInfoDTO){
        return userService.login(userLoginInfoDTO);
    }
}
