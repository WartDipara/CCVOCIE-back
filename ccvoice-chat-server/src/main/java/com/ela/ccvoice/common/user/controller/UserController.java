package com.ela.ccvoice.common.user.controller;


import com.ela.ccvoice.common.common.domain.vo.response.ApiResult;
import com.ela.ccvoice.common.user.domain.vo.response.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("用户获取个人信息")
    @GetMapping("/userInfo")
    public ApiResult<UserInfoResp> getUserProfile(@RequestParam Long uid){
        if(uid == null){
            return ApiResult.fail("空的uid你也传？");
        }
        //TODO 未完成的接口
        return null;
    }
}

