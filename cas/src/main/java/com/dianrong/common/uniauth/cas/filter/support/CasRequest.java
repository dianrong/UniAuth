package com.dianrong.common.uniauth.cas.filter.support;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import lombok.ToString;

@ToString
public class CasRequest {

  /**
   * 请求的url.
   */
  private String url;

  /**
   * 请求的方法, 默认为all.
   */
  private String method = AppConstants.HTTP_METHOD_ALL;

  public CasRequest(String url) {
    Assert.notNull(url);
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }
}
