package com.ela.ccvoice.common.user.controller;


import com.ela.ccvoice.common.common.domain.dto.RequestInfo;
import com.ela.ccvoice.common.common.domain.vo.response.ApiResult;
import com.ela.ccvoice.common.common.utils.RequestHolder;
import com.ela.ccvoice.common.user.domain.vo.request.userRelate.ModifyNameReq;
import com.ela.ccvoice.common.user.domain.vo.response.UserInfoResp;
import com.ela.ccvoice.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 用户 如果接口前带public则不需要拦截，否则需要被登录拦截器拦截
 * </p>
 *
 * @author <a href="https://github.com/WartDipara">ela</a>
 * @since 2024-10-29
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理相关接口")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;
    @ApiOperation("用户获取个人信息")
    @GetMapping("/userInfo")
    public ApiResult<UserInfoResp> getUserProfile(){
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }
    @ApiOperation("修改用户名")
    @PutMapping("/name")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq modifyNameReq){
        userService.modifyName(RequestHolder.get().getUid(), modifyNameReq);
        return ApiResult.success();
    }
}

