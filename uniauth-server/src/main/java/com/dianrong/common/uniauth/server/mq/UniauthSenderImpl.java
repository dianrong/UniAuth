package com.dianrong.common.uniauth.server.mq;

import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.server.mq.v1.UniauthNotify;
import com.dianrong.common.uniauth.server.mq.v1.ninfo.UserAddNotifyInfo;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author cwl
 * @created Apr 11, 2016.
 */
@Service
@Slf4j
public class UniauthSenderImpl implements UniauthSender {

  @Resource(name = "mqSender")
  private MqSender sender;

  @Value("#{uniauthConfig['rabbit.user.add.key']}")
  private String userAddKey;

  @Autowired
  private UniauthNotify uniauthNotify;

  @Override
  public void sendUserAdd(UserDto user) {
    log.info("添加用户发送mq：用户ID【" + user.getId() + '】');
    sender.send(userAddKey, user);

    // 调用v1版本的添加用户通知
    uniauthNotify.notify(new UserAddNotifyInfo().setEmail(user.getEmail()).setName(user.getName())
        .setPhone(user.getPhone()).setUserId(user.getId()));
  }
}
