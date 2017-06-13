package com.dianrong.common.uniauth.server.mq.v1.ninfo;

import com.dianrong.common.uniauth.server.mq.v1.NotifyInfoType;
import lombok.ToString;

@ToString
public class UsersToGroupExchangeNotifyInfo extends UsersToGroupNotifyInfo {

  /**
   * 关联关系中的组id.
   */
  private Integer targetGroupId;

  public UsersToGroupExchangeNotifyInfo() {
    super.setType(NotifyInfoType.USERS_TO_GROUP_EXCHANGE);
  }

  public Integer getTargetGroupId() {
    return targetGroupId;
  }

  public UsersToGroupExchangeNotifyInfo setTargetGroupId(Integer targetGroupId) {
    this.targetGroupId = targetGroupId;
    return this;
  }
}
