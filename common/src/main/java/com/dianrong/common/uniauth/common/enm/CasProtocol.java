package com.dianrong.common.uniauth.common.enm;

// Cas协议
public enum CasProtocol {
  DianRongCas("service", "ticket", "tenancy", "tenancyid", "user_id", "login_account");
  private final String serviceName;
  private final String ticketName;
  private final String tenancyCodeName;
  private final String tenancyIdName;
  private final String userIdName;
  private final String loginAccountName;

  private CasProtocol(final String serviceName, final String ticketName,
      final String tenancyCodeName, final String tenancyIdName, final String userIdName,
      final String loginAccountName) {
    this.serviceName = serviceName;
    this.ticketName = ticketName;
    this.tenancyCodeName = tenancyCodeName;
    this.tenancyIdName = tenancyIdName;
    this.userIdName = userIdName;
    this.loginAccountName = loginAccountName;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getTicketName() {
    return ticketName;
  }

  public String getTenancyCodeName() {
    return tenancyCodeName;
  }

  public String getTenancyIdName() {
    return tenancyIdName;
  }

  public String getUserIdName() {
    return userIdName;
  }

  public String getLoginAccountName() {
    return loginAccountName;
  }
}
