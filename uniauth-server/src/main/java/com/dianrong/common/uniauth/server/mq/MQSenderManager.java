package com.dianrong.common.uniauth.server.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * TODO
 * </pre>
 * @author cwl
 * @created Apr 14, 2016
 */
@Service
public class MQSenderManager {
    
    Logger logger=LoggerFactory.getLogger(MQSenderManager.class);
    
    @Value("#{uniauthConfig['rabbit.switch']}")
    private String mq_switch;
    
    /**
     * 根据template获取消息发送服务
     * @param template
     * @return
     */
    public MQSender getSender(RabbitTemplate template){
        if(mq_switch!=null&&mq_switch.equals("on")){
            return new MQSenderDefaultImpl(template);
        }
        logger.warn("rabbit_mq服务并没有开启，hit：#{uniauthConfig['rabbit.switch']}==on.......");
        return MQSenderNotWorkImpl.getInstance();
    }
}


