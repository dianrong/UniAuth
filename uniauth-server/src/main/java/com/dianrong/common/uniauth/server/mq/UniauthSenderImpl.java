package com.dianrong.common.uniauth.server.mq;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.server.data.entity.User;

/**
 * <pre>
 * TODO
 * </pre>
 * @author cwl
 * @created Apr 11, 2016
 */
@Service
public class UniauthSenderImpl implements UniauthSender {

    private final Logger log = LoggerFactory.getLogger(UniauthSenderImpl.class);

    @Autowired
    @Qualifier("uniauth_mq_template")
    private RabbitTemplate template;
    
    private MQSender sender;

    @Value("#{uniauthConfig['rabbit.user.add.key']}")
    private String userAddKey;
    
    @Override
    public void sendUserAdd(User user) {
        log.info("添加用户发送mq：用户ID【" + user.getId() + '】');
        sender.send(userAddKey, user);
    }
    
    @PostConstruct
    private void init(){
        sender=new MQSenderDefaultImpl(template);
    }
}


