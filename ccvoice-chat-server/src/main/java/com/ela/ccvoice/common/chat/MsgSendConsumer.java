package com.ela.ccvoice.common.chat;

import com.ela.ccvoice.common.chat.dao.MessageDao;
import com.ela.ccvoice.common.chat.domain.entity.Message;
import com.ela.ccvoice.common.common.constant.MqConstant;
import com.ela.ccvoice.common.common.domain.dto.MsgSendDTO;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(consumerGroup = MqConstant.SEND_MSG_GROUP,topic = MqConstant.SEND_MSG_TOPIC)
@Component
public class MsgSendConsumer implements RocketMQListener<MsgSendDTO> {
    @Autowired
    private MessageDao messageDao;
    @Override
    public void onMessage(MsgSendDTO dto){
        Message message = messageDao.getById(dto.getMsgId());
    }
}
