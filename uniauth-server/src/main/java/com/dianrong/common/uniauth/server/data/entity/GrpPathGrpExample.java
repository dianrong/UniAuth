package com.dianrong.common.uniauth.server.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrpPathGrpExample extends GrpExample {

  private String grpCode;

  private Integer grpId;

  private Long tenancyId;

  private Byte status;

  public boolean isLinkedQuery() {
    if (grpCode != null || grpCode != null) {
      return true;
    }
    return false;
  }

  public String getGrpCode() {
    return grpCode;
  }

  public void setGrpCode(String grpCode) {
    this.grpCode = grpCode;
  }

  public Integer getGrpId() {
    return grpId;
  }

  public void setGrpId(Integer grpId) {
    this.grpId = grpId;
  }

  public Long getTenancyId() {
    return tenancyId;
  }

  public void setTenancyId(Long tenancyId) {
    this.tenancyId = tenancyId;
  }

  public Byte getStatus() {
    return status;
  }

  public void setStatus(Byte status) {
    this.status = status;
  }
}
