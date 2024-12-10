package com.ela.ccvoice.common.common.event.listener;

import com.ela.ccvoice.common.common.event.UserRegisterEvent;
import com.ela.ccvoice.common.user.dao.UserDao;
import com.ela.ccvoice.common.user.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisterListener {
    @Autowired
    private UserDao userDao;

    @Async
    @EventListener(classes = UserRegisterEvent.class)
    public void sendCard(UserRegisterEvent evt){
        User user=evt.getUser();
        log.info("用户{}注册成功",user.getName());
    }
}
