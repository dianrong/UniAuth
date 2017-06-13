package com.dianrong.common.uniauth.server.mq.v1.ninfo;

import com.dianrong.common.uniauth.server.mq.v1.NotifyInfoType;
import lombok.ToString;

@ToString
public class GroupAddNotifyInfo extends BaseGroupNotifyInfo {

  /**
   * 组code.
   */
  private String code;

  /**
   * 组名称.
   */
  private String name;

  /**
   * 组描述.
   */
  private String description;

  /**
   * 父组id.
   */
  private Integer parentGroupId;

  public GroupAddNotifyInfo() {
    super.setType(NotifyInfoType.GROUP_ADD);
  }

  public String getCode() {
    return code;
  }

  public GroupAddNotifyInfo setCode(String code) {
    this.code = code;
    return this;
  }

  public String getName() {
    return name;
  }

  public GroupAddNotifyInfo setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public GroupAddNotifyInfo setDescription(String description) {
    this.description = description;
    return this;
  }

  public Integer getParentGroupId() {
    return parentGroupId;
  }

  public GroupAddNotifyInfo setParentGroupId(Integer parentGroupId) {
    this.parentGroupId = parentGroupId;
    return this;
  }
}
