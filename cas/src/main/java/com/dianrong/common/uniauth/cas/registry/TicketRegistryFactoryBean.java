package com.dianrong.common.uniauth.cas.registry;

import com.dianrong.common.uniauth.common.cache.SimpleUseRedisSwitch;
import com.dianrong.common.uniauth.common.customer.SwitchControl;
import com.dianrong.common.uniauth.common.util.Assert;
import org.jasig.cas.ticket.registry.TicketRegistry;

public class TicketRegistryFactoryBean {

  private TgtIdentityTicketRegistry defaultTicketRegistry;

  private TgtIdentityTicketRegistry redisTicketRegistry;

  /**
   * 配置字符串.
   */
  private SwitchControl redisSwitch = new SimpleUseRedisSwitch();

  public TgtIdentityTicketRegistry getDefaultTicketRegistry() {
    return defaultTicketRegistry;
  }

  public void setDefaultTicketRegistry(
      TgtIdentityTicketRegistry defaultTicketRegistry) {
    this.defaultTicketRegistry = defaultTicketRegistry;
  }

  public TgtIdentityTicketRegistry getRedisTicketRegistry() {
    return redisTicketRegistry;
  }

  public void setRedisTicketRegistry(
      TgtIdentityTicketRegistry redisTicketRegistry) {
    this.redisTicketRegistry = redisTicketRegistry;
  }

  public void setRedisSwitch(SwitchControl redisSwitch) {
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
