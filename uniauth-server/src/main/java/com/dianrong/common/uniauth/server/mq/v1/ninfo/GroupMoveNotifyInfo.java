package com.dianrong.common.uniauth.server.mq.v1.ninfo;

import com.dianrong.common.uniauth.server.mq.v1.NotifyInfoType;
import lombok.ToString;

@ToString
public class GroupMoveNotifyInfo extends BaseGroupNotifyInfo {

  /**
   * 目标组id.
   */
  private Integer targetGroupId;

  public GroupMoveNotifyInfo() {
    super.setType(NotifyInfoType.GROUP_MOVE);
  }

  public Integer getTargetGroupId() {
    return targetGroupId;
  }

  public GroupMoveNotifyInfo setTargetGroupId(Integer targetGroupId) {
    this.targetGroupId = targetGroupId;
    return this;
  }
}
