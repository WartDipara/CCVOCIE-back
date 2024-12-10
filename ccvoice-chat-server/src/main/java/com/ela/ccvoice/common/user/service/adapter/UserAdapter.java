package com.ela.ccvoice.common.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.ela.ccvoice.common.user.domain.dto.UserInfoDto;
import com.ela.ccvoice.common.user.domain.entity.User;
import com.ela.ccvoice.common.user.domain.vo.response.UserInfoResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAdapter {

    /**
     * 授权用户
     * @param id
     * @param userInfo
     * @return
     */
    public static User buildAuthorizeUser(Long id, UserInfoDto userInfo) {
        User user = new User();
        user.setId(id);
        user.setAvatar(userInfo.getAvatar());
        user.setName(userInfo.getNickName());
        user.setSex(userInfo.getSex());
        if (userInfo.getNickName().length() > 6) {
            user.setName("名字过长" + RandomUtil.randomInt(100000));
        } else {
            user.setName(userInfo.getNickName());
        }
        return user;
    }

    /**
     *
     * @param user
     * @param countByValidItemId
     * @return
     */
    public static UserInfoResp buildUserInfoResp(User user,Integer countByValidItemId){
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtil.copyProperties(user,userInfoResp);
        userInfoResp.setModifyNameChange(countByValidItemId);
        return userInfoResp;
    }

    /**
     * TODO
     * 成就系统，不一定用得到
     */
}
