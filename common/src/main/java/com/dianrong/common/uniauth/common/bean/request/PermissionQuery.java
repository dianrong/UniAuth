package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

public class PermissionQuery extends PageParam {

  private static final long serialVersionUID = 4131252066057083988L;
  private Integer id;
  private String value;
  private String description;
  private Byte status;
  private Integer permTypeId;
  private Integer domainId;
  private List<Integer> permIds;
  private String valueExt;

  public Integer getId() {
    return id;
  }

  public PermissionQuery setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getValue() {
    return value;
  }

  public PermissionQuery setValue(String value) {
    this.value = value;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public PermissionQuery setDescription(String description) {
    this.description = description;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public PermissionQuery setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public Integer getPermTypeId() {
    return permTypeId;
  }

  public PermissionQuery setPermTypeId(Integer permTypeId) {
    this.permTypeId = permTypeId;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public PermissionQuery setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public List<Integer> getPermIds() {
    return permIds;
  }

  public PermissionQuery setPermIds(List<Integer> permIds) {
    this.permIds = permIds;
    return this;
  }

  public String getValueExt() {
    return valueExt;
  }

  public PermissionQuery setValueExt(String valueExt) {
    this.valueExt = valueExt;
    return this;
  }

  @Override
  public String toString() {
    return "PermissionQuery [id=" + id + ", value=" + value + ", description=" + description
        + ", status=" + status + ", permTypeId=" + permTypeId + ", domainId=" + domainId
        + ", permIds=" + permIds + ", valueExt=" + valueExt + "]";
  }
}
