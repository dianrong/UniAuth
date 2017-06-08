package com.dianrong.common.uniauth.cas.registry;

import org.jasig.cas.ticket.registry.DefaultTicketRegistry;
import org.jasig.cas.ticket.registry.TicketRegistry;

public class TicketRegistryFactoryBean {

  private DefaultTicketRegistry defaultTicketRegistry;

  private RedisTicketRegistry redisTicketRegistry;

  private String casIsCluster;

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

  public String getCasIsCluster() {
    return casIsCluster;
  }

  public void setCasIsCluster(String casIsCluster) {
    this.casIsCluster = casIsCluster;
  }

  /**
   * 根据控制开关决定TicketRegistry的具体实现.
   */
  public TicketRegistry buildTicketRegistry() {
    if (casIsCluster != null && "true".equalsIgnoreCase(casIsCluster)) {
      return redisTicketRegistry;
    } else {
      return defaultTicketRegistry;
    }
  }
}
