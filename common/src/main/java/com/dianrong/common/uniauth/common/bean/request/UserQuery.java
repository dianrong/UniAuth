package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;
import lombok.ToString;

/**
 * Created by Arc on 15/1/16.
 */
@ToString
public class UserQuery extends PageParam {

  private static final long serialVersionUID = -9186367883822216088L;
  private Long userId;
  private List<Long> userIds;
  private String name;
  private String phone;
  private String email;
  private Byte status;
  private Integer tagId;
  private Integer groupId;
  // 需要在groupId != null的情况下才生效
  private Boolean needDescendantGrpUser;
  // 是否需要被禁用掉的组的user
  private Boolean needDisabledGrpUser;

  /**
   * 根据账号来匹配(phone, email, ldap_no...)
   */
  private String account;

  private Integer roleId;
  private Boolean needTag;

  private List<Long> excludeUserIds;

  public Integer getGroupId() {
    return groupId;
  }

  public UserQuery setGroupId(Integer groupId) {
    this.groupId = groupId;
    return this;
  }

  public Boolean getNeedTag() {
    return needTag;
  }

  public UserQuery setNeedTag(Boolean needTag) {
    this.needTag = needTag;
    return this;
  }

  public Integer getTagId() {
    return tagId;
  }

  public UserQuery setTagId(Integer tagId) {
    this.tagId = tagId;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public UserQuery setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public List<Long> getUserIds() {
    return userIds;
  }

  public UserQuery setUserIds(List<Long> userIds) {
    this.userIds = userIds;
    return this;
  }

  public String getName() {
    return name;
  }

  public UserQuery setName(String name) {
    this.name = name;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public UserQuery setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public UserQuery setEmail(String email) {
    this.email = email;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public UserQuery setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public UserQuery setRoleId(Integer roleId) {
    this.roleId = roleId;
    return this;
  }

  public Boolean getNeedDescendantGrpUser() {
    return needDescendantGrpUser;
  }

  public UserQuery setNeedDescendantGrpUser(Boolean needDescendantGrpUser) {
    this.needDescendantGrpUser = needDescendantGrpUser;
    return this;
  }

  public Boolean getNeedDisabledGrpUser() {
    return needDisabledGrpUser;
  }

  public UserQuery setNeedDisabledGrpUser(Boolean needDisabledGrpUser) {
    this.needDisabledGrpUser = needDisabledGrpUser;
    return this;
  }

  public List<Long> getExcludeUserIds() {
    return excludeUserIds;
  }

  public UserQuery setExcludeUserIds(List<Long> excludeUserIds) {
    this.excludeUserIds = excludeUserIds;
    return this;
  }

  public String getAccount() {
    return account;
  }

  public UserQuery setAccount(String account) {
    this.account = account;
    return this;
  }
}
