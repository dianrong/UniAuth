package com.dianrong.common.uniauth.server.mq;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 简单实现MQSender接口，没有实际发送mq消息.
 * </pre>
 *
 * @author cwl
 * @created Apr 14, 2016
 */
@Slf4j
public class MqSenderNotWorkImpl implements MqSender {

  private MqSenderNotWorkImpl() {
  }

  private static final MqSenderNotWorkImpl instance = new MqSenderNotWorkImpl();

  @Override
  public void send(String key, Object msgObj) {
    log.info("没有发送消息，消息key：【" + key + "】，请检查是否启用mq...");
  }

  /**
   * 返回实例.
   */
  public static final MqSenderNotWorkImpl getInstance() {
    return instance;
  }
}
