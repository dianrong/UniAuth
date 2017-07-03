package com.dianrong.common.uniauth.server.mq;

import com.dianrong.common.uniauth.common.bean.dto.UserDto;

/**
 * Uniauth中的消息发送接口.
 */
public interface UniauthSender {

  /**
   * 添加用户发送消息.
   */
  public void sendUserAdd(UserDto user);

}
