package com.dianrong.common.uniauth.cas.registry;

import com.dianrong.common.uniauth.common.cache.switcher.SimpleUseRedisSwitch;
import com.dianrong.common.uniauth.common.cache.switcher.UseRedisSwitch;
import com.dianrong.common.uniauth.common.util.Assert;
import org.jasig.cas.ticket.registry.DefaultTicketRegistry;
import org.jasig.cas.ticket.registry.TicketRegistry;

public class TicketRegistryFactoryBean {

  private DefaultTicketRegistry defaultTicketRegistry;

  private RedisTicketRegistry redisTicketRegistry;

  /**
   * 配置字符串.
   */
  private UseRedisSwitch redisSwitch = new SimpleUseRedisSwitch();

  public DefaultTicketRegistry getDefaultTicketRegistry() {
    return defaultTicketRegistry;
  }

  public void setDefaultTicketRegistry(DefaultTicketRegistry defaultTicketRegistry) {
    this.defaultTicketRegistry = defaultTicketRegistry;
  }

  public RedisTicketRegistry getRedisTicketRegistry() {
    return redisTicketRegistry;
  }

  public void setRedisTicketRegistry(RedisTicketRegistry redisTicketRegistry) {
    this.redisTicketRegistry = redisTicketRegistry;
  }

  public void setRedisSwitch(UseRedisSwitch redisSwitch) {
    Assert.notNull(redisSwitch, "redisSwitch must not be null.");
    this.redisSwitch = redisSwitch;
  }

  /**
   * 根据控制开关决定TicketRegistry的具体实现.
   */
  public TicketRegistry buildTicketRegistry() {
    if (this.redisSwitch.isOn()) {
      return redisTicketRegistry;
    } else {
      return defaultTicketRegistry;
    }
  }
}
