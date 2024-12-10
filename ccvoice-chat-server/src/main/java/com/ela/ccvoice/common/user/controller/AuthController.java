package com.ela.ccvoice.common.user.controller;

import com.ela.ccvoice.common.common.domain.vo.response.ApiResult;
import com.ela.ccvoice.common.common.utils.JwtUtils;
import com.ela.ccvoice.common.user.domain.dto.UserRegInfoDTO;
import com.ela.ccvoice.common.user.service.UserService;
import io.swagger.annotations.Api;
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


    @PostMapping("/reg")
    public ApiResult<?> register(@RequestBody UserRegInfoDTO userRegInfo){
        try{
            userService.register(userRegInfo);
            
        }catch(Exception e){
            return ApiResult.fail(e.getMessage());
        }
        return ApiResult.fail();
    }

    public ApiResult<?> login(){
        return null;
    }

}
