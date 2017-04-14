package com.dianrong.common.uniauth.server.mq;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author cwl
 * @created Apr 14, 2016
 */
@Service("mqSenderFactory")
@Slf4j
public class MQSenderFactory {
    @Value("#{uniauthConfig['rabbit.switch']}")
    private String mq_switch;

    @Autowired
    @Qualifier("uniauth_mq_template")
    private RabbitTemplate template;

    @Autowired
    @Qualifier("uniauth_admin")
    private RabbitAdmin admin;

    @Value("#{uniauthConfig['rabbit.exchange']}")
    private String exchangeName;

    /**
     * 根据template获取消息发送服务
     * 
     * @param template
     * @return
     */
    public MQSender getSender() {
        if (isOn()) {
            return MQSenderDefaultImpl.build(template, admin, exchangeName);
        }
        log.warn("rabbit_mq服务并没有开启，hit：#{uniauthConfig['rabbit.switch']}==on.......");
        return MQSenderNotWorkImpl.getInstance();
    }

    /**
     * . 判断是否开启了rabbitmq 发送消息的功能
     * 
     * @return true or false
     */
    private boolean isOn() {
        return "on".equalsIgnoreCase(mq_switch);
    }
}
