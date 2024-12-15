package com.ela.ccvoice.common.user.controller;

import com.ela.ccvoice.common.common.domain.vo.response.ApiResult;
import com.ela.ccvoice.common.common.utils.JwtUtils;
import com.ela.ccvoice.common.user.domain.dto.UserLoginInfoDTO;
import com.ela.ccvoice.common.user.domain.dto.UserRegInfoDTO;
import com.ela.ccvoice.common.user.domain.vo.response.LoginResp;
import com.ela.ccvoice.common.user.domain.vo.response.RegisterResp;
import com.ela.ccvoice.common.user.service.LoginService;
import com.ela.ccvoice.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户认证相关接口")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;


    @ApiOperation("用户注册")
    @PostMapping("/reg")
    public ApiResult<?> register(@RequestBody UserRegInfoDTO userRegInfo) {
        RegisterResp resp = userService.register(userRegInfo);
        if (!resp.isSuccess()) {
            return ApiResult.fail(resp.getMessage());
        }
        return ApiResult.success(resp.getMessage());
    }

    /**
     *
     * 改写了ws模式，这个接口不一定用的上
     * @param userLoginInfo
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ApiResult<?> login(@RequestBody UserLoginInfoDTO userLoginInfo) {
        LoginResp resp = userService.login(userLoginInfo);
        if (!resp.isSuccess()) {
            return ApiResult.fail(resp.getMessage());
        }
        return ApiResult.success(resp);
    }

}
