package com.dianrong.common.uniauth.server.mq;

import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;

/**
 * <pre>
 * mqSender默认实现.
 * </pre>
 *
 * @author cwl
 * @created Apr 11, 2016
 */
@Slf4j
public class MqSenderDefaultImpl implements MqSender {

  private RabbitTemplate template;

  // for declare exchange
  private RabbitAdmin admin;

  private String exchangeName;

  private AtomicBoolean init = new AtomicBoolean(false);

  // 私有构造函数
  private MqSenderDefaultImpl() {
  }

  @Override
  public void send(String key, Object msgObj) {
    try {
      template.convertAndSend(key, msgObj);
    } catch (Exception e) {
      log.error("消息发送失败：key【" + key + '】', e);
    }
  }

  // init
  private void init() {
    if (init.compareAndSet(false, true)) {
      Assert.notNull(exchangeName, "rabbitmq exchange name can not be null");
      Assert.notNull(template, "rabbitmq template can not be null");
      Assert.notNull(admin, "rabbitmq admin can not be null");
      // declare
      TopicExchange exchange = new TopicExchange(exchangeName);
      admin.declareExchange(exchange);
    }
  }

  public RabbitTemplate getTemplate() {
    return template;
  }

  public void setTemplate(RabbitTemplate template) {
    this.template = template;
  }

  public RabbitAdmin getAdmin() {
    return admin;
  }

  public void setAdmin(RabbitAdmin admin) {
    this.admin = admin;
  }

  public String getExchangeName() {
    return exchangeName;
  }

  public void setExchangeName(String exchangeName) {
    this.exchangeName = exchangeName;
  }

  /**
   * build a UNIAUTH standard MQSender implementation.
   *
   * @param template RabbitTemplate can not be null
   * @param admin RabbitAdmin can not be null
   * @param exchangeName String exchange name
   * @return MQSender instance
   * @throws IllegalArgumentException if any parameter is null
   */
  public static MqSender build(RabbitTemplate template, RabbitAdmin admin, String exchangeName) {
    MqSenderDefaultImpl instance = new MqSenderDefaultImpl();
    instance.setAdmin(admin);
    instance.setExchangeName(exchangeName);
    instance.setTemplate(template);
    instance.init();
    return instance;
  }

}
