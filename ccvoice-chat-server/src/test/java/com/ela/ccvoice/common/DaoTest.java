package com.ela.ccvoice.common;

import cn.hutool.core.util.RandomUtil;
import com.ela.ccvoice.common.user.common.utils.JwtUtils;
import com.ela.ccvoice.common.user.common.utils.RedisUtils;
import com.ela.ccvoice.common.user.dao.UserDao;
import com.ela.ccvoice.common.user.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class DaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Test
    public void test() {
        User byId = userDao.getById(1);
        User insert = new User();
        insert.setName("11");
        insert.setOpenId("123");
        boolean save =userDao.save(insert);
        System.out.println(save);
    }

    @Test
    public void jwt(){
        String token = jwtUtils.createToken(1L);
        String token1 = jwtUtils.createToken(2L);
        System.out.println(token);
        System.out.println(token1);
    }
    @Test
    public void redis(){
//        RedisUtils.set("name","卷心菜");
//        String name= RedisUtils.getStr("name");
//        System.out.println(name);
        // 分布式锁 命名其为rlock
        RLock lock = redissonClient.getLock("rlock");
        lock.lock();
        System.out.println();
        lock.unlock();
    }
    @Test
    public void normalTest(){
        System.out.println(RandomUtil.randomInt(100000, 999999));
    }
}
