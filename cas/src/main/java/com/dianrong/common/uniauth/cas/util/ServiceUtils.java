package com.dianrong.common.uniauth.cas.util;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;

public final class ServiceUtils {

  /**
   * 获取一个规整的RUL字符串.
   * <p>
   * 比如:https://techops-dev.dianrong.com//login/cas. 转化为:https://techops-dev.dianrong.com
   * </p>
   *
   * @param serviceUrl 需要处理的url,不能为空
   */
  public static String getRegularServiceUrl(final String serviceUrl) {
    Assert.notNull(serviceUrl);
    String regularServiceUrl = serviceUrl.trim();
    if (regularServiceUrl.endsWith(AppConstants.SERVICE_LOGIN_SUFFIX)) {
      regularServiceUrl = regularServiceUrl
          .substring(0, regularServiceUrl.length() - AppConstants.SERVICE_LOGIN_SUFFIX.length());
    }
    while (regularServiceUrl.endsWith("/")) {
      regularServiceUrl = regularServiceUrl.substring(0, regularServiceUrl.length() - 1);
    }
    return regularServiceUrl;
  }
}
