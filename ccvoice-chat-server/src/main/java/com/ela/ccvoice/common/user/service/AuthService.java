package com.ela.ccvoice.common.user.service;

import com.ela.ccvoice.common.user.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

}
