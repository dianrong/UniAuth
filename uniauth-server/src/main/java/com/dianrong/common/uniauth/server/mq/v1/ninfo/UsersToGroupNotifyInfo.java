package com.dianrong.common.uniauth.server.mq.v1.ninfo;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.ToString;

@ToString
public class UsersToGroupNotifyInfo extends BaseNotifyInfo {

  /**
   * 关联关系中的用户id.
   */
  protected List<Long> userIds;

  /**
   * 关联关系中的组id.
   */
  protected Integer groupId;

  public Integer getGroupId() {
    return groupId;
  }

  public UsersToGroupNotifyInfo setGroupId(Integer groupId) {
    this.groupId = groupId;
    return this;
  }

  public List<Long> getUserIds() {
    return userIds;
  }

  /**
   * 设置用户id.
   */
  public UsersToGroupNotifyInfo setUserId(Long userId) {
    if (this.userIds == null) {
      this.userIds = Lists.newArrayList();
    }
    this.userIds.add(userId);
    return this;
  }

  public UsersToGroupNotifyInfo setUserIds(List<Long> userIds) {
    this.userIds = userIds;
    return this;
  }
}
