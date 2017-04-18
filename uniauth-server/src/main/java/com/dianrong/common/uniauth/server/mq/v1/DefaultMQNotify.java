package com.dianrong.common.uniauth.server.mq.v1;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.server.mq.MQSender;

@Component
public class DefaultMQNotify extends AbstractMQNotify {

    @Resource(name = "mqSender")
    private MQSender sender;
    
    @Value("#{uniauthConfig['rabbit.notify.default_key']?:'uniauth.notify.default_key'}")
    private String routingKey;
    
    @PostConstruct
    private void initCheck() {
        Assert.notNull(sender);
    }
    
    @Override
    public String getRoutingKey() {
        return this.routingKey;
    }

    @Override
    protected MQSender getMQSender() {
        return this.sender;
    }
}
