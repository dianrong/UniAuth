package com.dianrong.common.uniauth.common.cache;

import com.dianrong.common.uniauth.common.customer.SwitchControl;

public class SimpleUseRedisSwitch implements SwitchControl {

  private String config;

  public SimpleUseRedisSwitch() {
  }

  public SimpleUseRedisSwitch(String config) {
    this.config = config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  @Override
  public boolean isOn() {
    return Boolean.TRUE.toString().equalsIgnoreCase(this.config);
  }
}
