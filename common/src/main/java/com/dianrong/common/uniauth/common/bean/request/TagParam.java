package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

/**
 * Created by Arc on 12/4/2016.
 */
public class TagParam extends Operator {

  private static final long serialVersionUID = 8630107970908020377L;
  private Integer id;
  private String code;
  private Byte status;
  private Integer tagTypeId;
  private Integer domainId;
  private String description;
  private List<Integer> grpIds;
  private List<Long> userIds;

  // 默认是全部都要处理
  private Boolean needProcessGoupIds = Boolean.TRUE;
  private Boolean needProcessUserIds = Boolean.TRUE;

  public Boolean getNeedProcessGoupIds() {
    return needProcessGoupIds;
  }

  public void setNeedProcessGoupIds(Boolean needProcessGoupIds) {
    this.needProcessGoupIds = needProcessGoupIds;
  }

  public Boolean getNeedProcessUserIds() {
    return needProcessUserIds;
  }

  public void setNeedProcessUserIds(Boolean needProcessUserIds) {
    this.needProcessUserIds = needProcessUserIds;
  }

  public Integer getId() {
    return id;
  }

  public TagParam setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public TagParam setCode(String code) {
    this.code = code;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public TagParam setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public Integer getTagTypeId() {
    return tagTypeId;
  }

  public TagParam setTagTypeId(Integer tagTypeId) {
    this.tagTypeId = tagTypeId;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public TagParam setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public TagParam setDescription(String description) {
    this.description = description;
    return this;
  }

  public List<Integer> getGrpIds() {
    return grpIds;
  }

  public TagParam setGrpIds(List<Integer> grpIds) {
    this.grpIds = grpIds;
    return this;
  }

  public List<Long> getUserIds() {
    return userIds;
  }

  public TagParam setUserIds(List<Long> userIds) {
    this.userIds = userIds;
    return this;
  }

  @Override
  public String toString() {
    return "TagParam [id=" + id + ", code=" + code + ", status=" + status + ", tagTypeId="
        + tagTypeId + ", domainId=" + domainId + ", description=" + description + ", grpIds="
        + grpIds + ", userIds=" + userIds + ", needProcessGoupIds=" + needProcessGoupIds
        + ", needProcessUserIds=" + needProcessUserIds + "]";
  }
}
