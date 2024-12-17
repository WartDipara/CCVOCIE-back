package com.ela.ccvoice.common.user.service.impl;

import com.ela.ccvoice.common.common.event.UserRegisterEvent;
import com.ela.ccvoice.common.user.dao.UserDao;
import com.ela.ccvoice.common.user.domain.dto.UserLoginInfoDTO;
import com.ela.ccvoice.common.user.domain.dto.UserRegInfoDTO;
import com.ela.ccvoice.common.user.domain.entity.User;
import com.ela.ccvoice.common.user.domain.vo.request.userRelate.ModifyNameReq;
import com.ela.ccvoice.common.user.domain.vo.response.LoginResp;
import com.ela.ccvoice.common.user.domain.vo.response.RegisterResp;
import com.ela.ccvoice.common.user.domain.vo.response.UserInfoResp;
import com.ela.ccvoice.common.user.service.LoginService;
import com.ela.ccvoice.common.user.service.UserService;
import com.ela.ccvoice.common.user.service.adapter.UserAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MQProducer mqProducer;
    @Autowired
    private LoginService loginService;

    @Transactional
    @Override
    public RegisterResp register(UserRegInfoDTO userRegInfoDTO) {
        if (userRegInfoDTO.getEmail() == null || userRegInfoDTO.getPassword() == null || userRegInfoDTO.getName() == null) {
            return new RegisterResp(false,"参数错误，请检查参数是否完整");
        }
        //reg步骤
        User user = new User();
        user.setName(userRegInfoDTO.getName());
        user.setEmail(userRegInfoDTO.getEmail());
        String encodedPassword = passwordEncoder.encode(userRegInfoDTO.getPassword());
        user.setPassword(encodedPassword);

        try {
            userDao.save(user);
            //用事件监控来执行注册事件，由mq执行
            publisher.publishEvent(new UserRegisterEvent(this, user));
            return new RegisterResp(true,"注册成功");
        }catch (DataAccessException e){
            return new RegisterResp(false,"注册失败，无法保存用户信息");
        }
    }

    @Override
    public LoginResp login(UserLoginInfoDTO userLoginInfoDTO) {
        User user = userDao.getByName(userLoginInfoDTO.getName());
        if(user == null || !passwordEncoder.matches(userLoginInfoDTO.getPassword(),user.getPassword())){
            return new LoginResp(null,false,"用户名或密码错误");
        }
        String token = loginService.login(user.getId());
        log.info("用户{}登录成功",user.getName());
        return new LoginResp(token,true,"登录成功",user.getId());
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        return UserAdapter.buildUserInfoResp(user);
    }

    @Override
    public void modifyName(Long uid, ModifyNameReq req) {
        //判断名字是不是重复
        String newName = req.getName();
        User oldUser = userDao.getByName(newName);
        if(oldUser != null){
            throw new RuntimeException("用户名重复");
        }
        userDao.modifyName(uid, newName);
        log.info("用户{}修改了用户名，新用户名为{}",uid,newName);
    }
}
