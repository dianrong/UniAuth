package com.dianrong.common.uniauth.server.mq.v1.ninfo;

import lombok.ToString;

@ToString
public class BaseGroupNotifyInfo extends BaseNotifyInfo {

  /**
   * 组id.
   */
  private Integer groupId;

  public Integer getGroupId() {
    return groupId;
  }

  public BaseGroupNotifyInfo setGroupId(Integer groupId) {
    this.groupId = groupId;
    return this;
  }
}
