package com.ela.ccvoice.common;

import cn.hutool.core.util.RandomUtil;
import com.ela.ccvoice.common.common.utils.JwtUtils;
import com.ela.ccvoice.common.user.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.messaging.Message;

@SpringBootTest
@Slf4j
public class DaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

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
    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Test
    public void thread() throws InterruptedException {
        executor.execute(()->{
            if(true){
                log.error("error");
                throw new RuntimeException("1234");
            }
        });
        Thread.sleep(200);
    }
    @Test
    public void normalTest(){
        System.out.println(RandomUtil.randomInt(100000, 999999));
    }

    @Test
    public void sendMQ() {
        Message<String> build = MessageBuilder.withPayload("123").build();
        rocketMQTemplate.send("test-topic", build);
    }
}
