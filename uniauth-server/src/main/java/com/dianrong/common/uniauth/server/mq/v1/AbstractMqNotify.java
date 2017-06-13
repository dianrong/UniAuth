package com.dianrong.common.uniauth.server.mq.v1;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.mq.MqSender;
import lombok.extern.slf4j.Slf4j;

/**
 * 通知模板.
 *
 * @author wanglin
 */
@Slf4j
public abstract class AbstractMqNotify implements UniauthNotify {

  @Override
  public void notify(NotifyInfo notifyInfo) {
    Assert.notNull(notifyInfo);
    String routingKey = getRoutingKey();
    Assert.notNull(routingKey);
    MqSender mqSender = getMqSender();
    Assert.notNull(mqSender);
    try {
      log.info("notify message: {}", notifyInfo);
      mqSender.send(routingKey, notifyInfo);
    } catch (Throwable t) {
      log.error("failed to notify, the key: {} , the content: {}", routingKey, notifyInfo);
      // ignore exception
      // throw new NotifyFailedException("failed to notify", t);
    }
  }

  /**
   * 获取发送的routineKey.
   *
   * @return 不能为空
   */
  public abstract String getRoutingKey();

  /**
   * 获取发送的实现.
   *
   * @return 不能为空
   */
  protected abstract MqSender getMqSender();
}
