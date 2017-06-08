package com.dianrong.common.uniauth.client.support;

import com.dianrong.common.uniauth.client.exp.DomainNotDefinedException;

public class CheckDomainDefine {

  private CheckDomainDefine() {

  }

  /**
   * Check domain code.
   */
  public static void checkDomainDefine(String currentDomainCode) {
    if (currentDomainCode == null || "".equals(currentDomainCode.trim())) {
      throw new DomainNotDefinedException(
          "The bean of class com.dianrong.common.uniauth.client."
          + "DomainDefine not defined in client configuration file.");
    }
  }
}
