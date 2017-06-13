package com.dianrong.common.uniauth.server.mq.v1;

import com.dianrong.common.uniauth.server.mq.MqSender;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DefaultMqNotify extends AbstractMqNotify {

  @Resource(name = "mqSender")
  private MqSender sender;

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
  protected MqSender getMqSender() {
    return this.sender;
  }
}
