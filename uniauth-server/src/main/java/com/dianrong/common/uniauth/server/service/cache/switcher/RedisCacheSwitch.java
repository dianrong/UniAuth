package com.dianrong.common.uniauth.server.service.cache.switcher;

import com.dianrong.common.uniauth.common.util.Assert;

/**
 * Redis缓存的开关控制.
 */
public class RedisCacheSwitch implements Switch {

  private String configKey;
  
  public RedisCacheSwitch(String configKey) {
    Assert.notNull(configKey);
    this.configKey = configKey;
  }
  
  /**
   * 默认开关为开.
   */
  @Override
  public boolean on() {
    return !Boolean.FALSE.toString().equalsIgnoreCase(configKey);
  }
}
