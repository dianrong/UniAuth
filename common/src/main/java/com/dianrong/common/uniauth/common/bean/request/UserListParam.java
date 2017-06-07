package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.bean.Linkage;
import java.util.List;

public class UserListParam extends Operator {

  private static final long serialVersionUID = -950457653366655079L;
  private Integer groupId;
  private List<Long> userIds;

  private List<Linkage<Long, Integer>> userIdGroupIdPairs;

  // true normal member, false owner member
  private Boolean normalMember;

  public List<Linkage<Long, Integer>> getUserIdGroupIdPairs() {
    return userIdGroupIdPairs;
  }

  public UserListParam setUserIdGroupIdPairs(List<Linkage<Long, Integer>> userIdGroupIdPairs) {
    this.userIdGroupIdPairs = userIdGroupIdPairs;
    return this;
  }

  public Boolean getNormalMember() {
    return normalMember;
  }

  public UserListParam setNormalMember(Boolean normalMember) {
    this.normalMember = normalMember;
    return this;
  }

  public List<Long> getUserIds() {
    return userIds;
  }

  public UserListParam setUserIds(List<Long> userIds) {
    this.userIds = userIds;
    return this;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public UserListParam setGroupId(Integer groupId) {
    this.groupId = groupId;
    return this;
  }

  @Override
  public String toString() {
    return "UserListParam [groupId=" + groupId + ", userIds=" + userIds + ", userIdGroupIdPairs="
        + userIdGroupIdPairs + ", normalMember=" + normalMember + "]";
  }
}
