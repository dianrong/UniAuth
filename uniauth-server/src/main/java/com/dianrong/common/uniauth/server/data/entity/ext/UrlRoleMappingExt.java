package com.dianrong.common.uniauth.server.data.entity.ext;

public class UrlRoleMappingExt {

  private String permUrl;
  private String roleCode;
  private String permType;
  private String httpMethod;
  private Long tenancyId;

  public String getPermUrl() {
    return permUrl;
  }

  public void setPermUrl(String permUrl) {
    this.permUrl = permUrl;
  }

  public String getRoleCode() {
    return roleCode;
  }

  public void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }

  public String getPermType() {
    return permType;
  }

  public void setPermType(String permType) {
    this.permType = permType;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  public Long getTenancyId() {
    return tenancyId;
  }

  public void setTenancyId(Long tenancyId) {
    this.tenancyId = tenancyId;
  }
}
