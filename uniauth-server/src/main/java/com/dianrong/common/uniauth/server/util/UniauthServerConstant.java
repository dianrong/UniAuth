package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import org.springframework.util.StringUtils;

/**
 * 定义Uniauth Server专用的常量.
 *
 * @author wanglin
 */
public final class UniauthServerConstant {
  /**
   * 超级管理员角色Code.
   */
  public static final String ROLE_CODE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

  /**
   * 批量插入数据的List大小.
   */
  public static final int BATCH_INSERT_NUM = 2048;

  /**
   * 判断传入的编码是否为根组.
   */
  public static boolean isRootGrp(String code) {
    if (!StringUtils.hasText(code)) {
      return false;
    }
    if (code.trim().equalsIgnoreCase(AppConstants.GRP_ROOT) || code.trim()
        .equalsIgnoreCase(AppConstants.ORGANIZATION_ROOT)) {
      return true;
    }
    return false;
  }
}
