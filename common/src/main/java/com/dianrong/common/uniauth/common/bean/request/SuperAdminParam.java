package com.dianrong.common.uniauth.common.bean.request;

import java.util.Map;

/**
 * Created by Arc on 31/8/2017.
 */
public class SuperAdminParam extends Operator {

  private static final long serialVersionUID = 2506508045350827766L;

  private String url;
  private String params;
  private String httpMethod;
  private Map<String, String> requestHeaders;

  public String getUrl() {
    return url;
  }

  public SuperAdminParam setUrl(String url) {
    this.url = url;
    return this;
  }

  public String getParams() {
    return params;
  }

  public SuperAdminParam setParams(String params) {
    this.params = params;
    return this;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public SuperAdminParam setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
    return this;
  }

  public Map<String, String> getRequestHeaders() {
    return requestHeaders;
  }

  public SuperAdminParam setRequestHeaders(Map<String, String> requestHeaders) {
    this.requestHeaders = requestHeaders;
    return this;
  }


}
