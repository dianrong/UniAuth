package com.dianrong.common.uniauth.cas.service.support;

/**
 * 员工信息model.
 */
public class StaffInfo {

  private String staffNo;

  private String staffName;

  private String useEmail;

  private String crmEmail;

  private String email;

  public String getStaffNo() {
    return staffNo;
  }

  public void setStaffNo(String staffNo) {
    this.staffNo = staffNo;
  }

  public String getStaffName() {
    return staffName;
  }

  public void setStaffName(String staffName) {
    this.staffName = staffName;
  }

  public String getUseEmail() {
    return useEmail;
  }

  public void setUseEmail(String useEmail) {
    this.useEmail = useEmail;
  }

  public String getCrmEmail() {
    return crmEmail;
  }

  public void setCrmEmail(String crmEmail) {
    this.crmEmail = crmEmail;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
