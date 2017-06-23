package com.dianrong.common.uniauth.common.util;

import com.dianrong.common.uniauth.common.exp.CfgMissingException;

public class CheckSdkCfg {

  private CheckSdkCfg() {
  }

  /**
   * Check uniauth endpoint 参数.
   */
  public static void checkSdkCfg(String bindValue) {
    if (bindValue == null || "".equals(bindValue.toString())) {
      throw new CfgMissingException("Missing uniauth endpoint.");
    }
  }
}
