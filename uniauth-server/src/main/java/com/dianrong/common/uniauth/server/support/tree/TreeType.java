package com.dianrong.common.uniauth.server.support.tree;

import com.dianrong.common.uniauth.common.cons.AppConstants;

/**
 * 定义树结构的类型.
 */
public enum TreeType {
  /**
   * 普通的树关系.
   */
  NORMAL(AppConstants.GRP_ROOT),

  /**
   * 组织关系树关系.
   */
  ORGANIZATION(AppConstants.ORGANIZATION_ROOT);

  private String rootCode;

  private TreeType(String rootCode) {
    this.rootCode = rootCode;
  }

  public String getRootCode() {
    return rootCode;
  }
}
