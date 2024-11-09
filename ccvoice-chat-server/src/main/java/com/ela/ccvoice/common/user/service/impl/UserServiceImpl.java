package com.ela.ccvoice.common.user.service.impl;

import com.ela.ccvoice.common.user.dao.UserDao;
import com.ela.ccvoice.common.user.domain.entity.User;
import com.ela.ccvoice.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Transactional
    @Override
    public void register(User user) {
        boolean save = userDao.save(user);
        //todo 用户注册事件

    }
}
