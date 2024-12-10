package com.ela.ccvoice.common.user.service.impl;

import com.ela.ccvoice.common.common.event.UserRegisterEvent;
import com.ela.ccvoice.common.user.dao.UserDao;
import com.ela.ccvoice.common.user.domain.dto.UserRegInfoDTO;
import com.ela.ccvoice.common.user.domain.entity.User;
import com.ela.ccvoice.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailabilityBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public void register(UserRegInfoDTO userRegInfoDTO) {
        if (userRegInfoDTO.getEmail() == null || userRegInfoDTO.getPassword() == null || userRegInfoDTO.getName() == null) {
            throw new RuntimeException("参数错误，传入的注册信息不完整");
        }
        User user = new User();
        user.setName(userRegInfoDTO.getName());
        user.setEmail(userRegInfoDTO.getEmail());
        user.setPassword(userRegInfoDTO.getPassword());
        userDao.save(user);
        //用事件监控来执行注册事件，由mq执行
        publisher.publishEvent(new UserRegisterEvent(this, user));
    }
}
