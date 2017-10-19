package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

import java.util.List;

@ApiModel(value = "权限查询参数")
@ToString
public class PermissionQuery extends PageParam {
  private static final long serialVersionUID = 4131252066057083988L;

  @ApiModelProperty(value = "id")
  private Integer id;

  @ApiModelProperty(value = "value")
  private String value;
  private String description;
  private Byte status;
  private Integer permTypeId;
  private Integer domainId;
  private List<Integer> permIds;
  private String valueExt;

  private Boolean includePermRole;

  private Boolean includeRoleUser;

  private Boolean includeDisableUser;

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

  public Boolean getIncludePermRole() {
    return includePermRole;
  }

  /**
   * 指定通过权限Id查询关联用户的时候,返回结果是否包含权限和角色的关联关系信息.
   * <p>默认为false</p>
   */
  public PermissionQuery setIncludePermRole(Boolean includePermRole) {
    this.includePermRole = includePermRole;
    return this;
  }

  public Boolean getIncludeRoleUser() {
    return includeRoleUser;
  }

  /**
   * 指定通过权限Id查询关联用户的时候,返回结果是否包含角色和用户的关联关系信息.
   * <p>默认为false</p>
   */
  public PermissionQuery setIncludeRoleUser(Boolean includeRoleUser) {
    this.includeRoleUser = includeRoleUser;
    return this;
  }

  public Boolean getIncludeDisableUser() {
    return includeDisableUser;
  }

  /**
   * 是否将关联的禁用用户查询出来.
   * <p>默认为false</p>
   */
  public PermissionQuery setIncludeDisableUser(Boolean includeDisableUser) {
    this.includeDisableUser = includeDisableUser;
    return this;
  }
}
