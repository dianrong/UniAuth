package com.dianrong.common.uniauth.common.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class ZooKeeperConfig {

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  @Autowired(required = false)
  private DomainDefine domainDefine;

  public String getParam(String nodeName) {
    return allZkNodeMap.get(nodeName);
  }

  public String getCasServerUrl() {
    return getParam("cas_server");
  }

  public String getTechOpsServerUrl() {
    return getParam("domains.techops");
  }

  /**
   * 获取domians.xxx配置.
   */
  public String getDomainUrl() {
    if (domainDefine != null) {
      String domainCode = domainDefine.getDomainCode();

      return getParam("domains." + domainCode);
    }
    return null;
  }
}
