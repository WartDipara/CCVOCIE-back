package com.ela.ccvoice.common.chat.testGroup;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(topic = "test-topic", consumerGroup = "test-consumer-group")
@Component
public class TestSendConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String msg) {
        System.out.println("receive msg:{} " + msg);
    }
}
