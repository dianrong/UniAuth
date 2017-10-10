package com.dianrong.common.uniauth.common.cache.switcher;

import com.dianrong.common.uniauth.common.cache.switcher.UseRedisSwitch;

public class SimpleUseRedisSwitch implements UseRedisSwitch {

  private String config;

  public SimpleUseRedisSwitch(){}

  public SimpleUseRedisSwitch(String config){
    this.config = config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  @Override public boolean isOn() {
    return Boolean.TRUE.toString().equalsIgnoreCase(this.config);
  }
}
