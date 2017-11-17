package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.bean.ThirdAccountType;

public class LoginParam extends Operator {

  /**
   * 定义常量未知的IP.
   */
  public static final String UNKNOWN_IP = "unknown_ip";

  private static final long serialVersionUID = 7180777080519450378L;
  // domainCode may does not need involve in login process
  /**
   * Private String domainCode;.
   */
  private String account;
  // only apply to cas sso
  private String password;
  private String ip;

  // 租户id
  private Long tenancyId;

  private String tenancyCode;

  /**
   * 某些登录会指定三方账号类型.
   */
  private ThirdAccountType thirdAccountType;

  /**
   * 返回的用户信息是否包含三方账号信息.
   */
  private Boolean includeThirdAccount;

  public Long getTenancyId() {
    return tenancyId;
  }

  public LoginParam setTenancyId(Long tenancyId) {
    this.tenancyId = tenancyId;
    return this;
  }

  public String getAccount() {
    return account;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public LoginParam setAccount(String account) {
    this.account = account;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public LoginParam setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getTenancyCode() {
    return tenancyCode;
  }

  public LoginParam setTenancyCode(String tenancyCode) {
    this.tenancyCode = tenancyCode;
    return this;
  }

  public Boolean getIncludeThirdAccount() {
    return includeThirdAccount;
  }

  public LoginParam setIncludeThirdAccount(Boolean includeThirdAccount) {
    this.includeThirdAccount = includeThirdAccount;
    return this;
  }

  public ThirdAccountType getThirdAccountType() {
    return thirdAccountType;
  }

  public LoginParam setThirdAccountType(ThirdAccountType thirdAccountType) {
    this.thirdAccountType = thirdAccountType;
    return this;
  }
}
