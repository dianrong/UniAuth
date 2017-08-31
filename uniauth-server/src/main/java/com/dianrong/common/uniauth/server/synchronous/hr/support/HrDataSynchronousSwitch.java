package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.server.synchronous.support.Switcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("hrDataSynchronousSwitch")
public class HrDataSynchronousSwitch implements Switcher {

  /**
   * 开关.
   */
  @Value("#{uniauthConfig['synchronization.hr.switch']}") private String switchFlag;


  @Override public boolean isOn() {
    if (Boolean.TRUE.toString().equalsIgnoreCase(this.switchFlag)) {
      return true;
    }
    return false;
  }
}
