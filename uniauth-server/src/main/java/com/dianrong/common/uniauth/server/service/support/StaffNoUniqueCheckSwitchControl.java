package com.dianrong.common.uniauth.server.service.support;

import com.dianrong.common.uniauth.common.customer.SwitchControl;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 控制是否启用员工号唯一性的检验.
 */
@Component
public class StaffNoUniqueCheckSwitchControl implements SwitchControl {

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  @Override
  public boolean isOn() {
    return Boolean.TRUE.toString().equalsIgnoreCase(allZkNodeMap.get("staff.no.check"));
  }
}
