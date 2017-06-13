package com.dianrong.common.uniauth.server.data.entity.ext;

import com.dianrong.common.uniauth.server.data.entity.Permission;

public class PermissionExt extends Permission {

  private Integer domainId;
  protected Integer startIndex;
  protected Integer wantCount;

  public Integer getDomainId() {
    return domainId;
  }

  public void setDomainId(Integer domainId) {
    this.domainId = domainId;
  }

  public Integer getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(Integer startIndex) {
    this.startIndex = startIndex;
  }

  public Integer getWantCount() {
    return wantCount;
  }

  public void setWantCount(Integer wantCount) {
    this.wantCount = wantCount;
  }
}
