package com.dianrong.common.uniauth.common.enm;

// cas协议
public enum CasProtocal {
  DianRongCas("service", "ticket", "tenancy", "tenancyid");
  private final String serviceName;
  private final String ticketName;
  private final String tenancyCodeName;
  private final String tenancyIdName;

  private CasProtocal(final String serviceName, final String ticketName,
      final String tenancyCodeName, final String tenancyIdName) {
    this.serviceName = serviceName;
    this.ticketName = ticketName;
    this.tenancyCodeName = tenancyCodeName;
    this.tenancyIdName = tenancyIdName;
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
}
