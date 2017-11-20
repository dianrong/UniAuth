package com.dianrong.common.uniauth.server.service.support;

import com.dianrong.common.uniauth.common.customer.SwitchControl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 控制:是否将IPA登录应用到多租户.
 */
@Component
public class IpaLoginForDRCheckSwitchControl implements SwitchControl {

  @Value("${uniauthConfig[ipa.login.for.tenancy]}")
  private String tag;

  @Override
  public boolean isOn() {
    return Boolean.TRUE.toString().equalsIgnoreCase(tag);
  }
}
