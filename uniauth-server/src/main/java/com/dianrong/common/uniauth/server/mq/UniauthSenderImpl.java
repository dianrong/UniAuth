package com.dianrong.common.uniauth.server.mq;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.dto.UserDto;

/**
 * @author cwl
 * @created Apr 11, 2016
 */
@Service
public class UniauthSenderImpl implements UniauthSender {

    private final Logger log = Logger.getLogger(UniauthSenderImpl.class);

    @Resource(name="mqSender")
    private MQSender sender;

    @Value("#{uniauthConfig['rabbit.user.add.key']}")
    private String userAddKey;
    
    @Override
    public void sendUserAdd(UserDto user) {
        log.info("添加用户发送mq：用户ID【" + user.getId() + '】');
        sender.send(userAddKey, user);
    }
}