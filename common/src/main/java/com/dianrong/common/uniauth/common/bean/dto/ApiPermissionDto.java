package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;

import lombok.ToString;

@ToString
public class ApiPermissionDto implements Serializable {

  private static final long serialVersionUID = -1636150266370761167L;

  // 私有权限
  public static final Byte PRIVATE_PERMISSION = 0;
  // 共有权限
  public static final Byte PUBLIC_PERMISSION = 1;

  private Integer id;
  private String uri;
  private Byte status;
  private UriMethod method;
  private Byte type;

  public Integer getId() {
    return id;
  }

  public ApiPermissionDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getUri() {
    return uri;
  }

  public ApiPermissionDto setUri(String uri) {
    this.uri = uri;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public ApiPermissionDto setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public UriMethod getMethod() {
    return method;
  }

  /**
   * 设置权限访问的方法,比如GET,POST等.
   */
  public ApiPermissionDto setMethod(String method) {
    String methodUpperCase = method == null ? "" : method.toUpperCase();
    this.method = UriMethod.valueOf(methodUpperCase);
    return this;
  }

  public Byte getType() {
    return type;
  }

  public ApiPermissionDto setType(Byte type) {
    this.type = type;
    return this;
  }

  /**
   * Uri 对应的方法类型.
   *
   * @author wanglin
   */
  public enum UriMethod {
    POST, GET, ALL,;
  }
}
