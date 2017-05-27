package com.dianrong.common.uniauth.server.data.entity.ext;

import java.util.Date;

/**
 * Created by Arc on 26/1/16.
 */
public class UserExt {

  private Long id;
  private String email;
  private String name;
  private Integer groupId;
  private Byte userGroupType;
  private String phone;
  private Date createDate;
  private Byte status;

  public Integer getGroupId() {
    return groupId;
  }

  public UserExt setGroupId(Integer groupId) {
    this.groupId = groupId;
    return this;
  }

  public Long getId() {
    return id;
  }

  public UserExt setId(Long id) {
    this.id = id;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public UserExt setEmail(String email) {
    this.email = email;
    return this;
  }

  public Byte getUserGroupType() {
    return userGroupType;
  }

  public UserExt setUserGroupType(Byte userGroupType) {
    this.userGroupType = userGroupType;
    return this;
  }

  public String getName() {
    return name;
  }

  public UserExt setName(String name) {
    this.name = name;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public UserExt setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public UserExt setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public UserExt setStatus(Byte status) {
    this.status = status;
    return this;
  }

  @Override
  public String toString() {
    return "UserExt [id=" + id + ", email=" + email + ", name=" + name + ", groupId=" + groupId
        + ", userGroupType=" + userGroupType + ", phone=" + phone + ", createDate=" + createDate
        + ", status=" + status + "]";
  }
}
