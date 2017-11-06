package com.dianrong.common.uniauth.cas.model;

import java.io.Serializable;

/**
 * 用户属性.
 */
public class UserAttribute implements Serializable {

  /**
   * 员工号.
   */
  private String staffNo;

  public String getStaffNo() {
    return staffNo;
  }

  public void setStaffNo(String staffNo) {
    this.staffNo = staffNo;
  }
}
