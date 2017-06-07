package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.ToString;

@ToString
public class DomainDto implements Serializable {

  private static final long serialVersionUID = -7455679107780186680L;
  private Integer id;
  private String code;
  private String displayName;
  private String description;
  private Byte status;
  private Date createDate;
  private Date lastUpdate;


  private List<RoleDto> roleList;
  private List<StakeholderDto> stakeholderList;

  private String zkDomainUrl;
  private String zkDomainUrlEncoded;
  // 是否自定义登陆页面（默认没有）
  private Boolean isCustomizedLoginPage = Boolean.FALSE;

  public String getZkDomainUrlEncoded() {
    return zkDomainUrlEncoded;
  }

  public void setZkDomainUrlEncoded(String zkDomainUrlEncoded) {
    this.zkDomainUrlEncoded = zkDomainUrlEncoded;
  }

  public String getZkDomainUrl() {
    return zkDomainUrl;
  }

  public void setZkDomainUrl(String zkDomainUrl) {
    this.zkDomainUrl = zkDomainUrl;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public DomainDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public DomainDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public DomainDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public DomainDto setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public List<StakeholderDto> getStakeholderList() {
    return stakeholderList;
  }

  public DomainDto setStakeholderList(List<StakeholderDto> stakeholderList) {
    this.stakeholderList = stakeholderList;
    return this;
  }

  public List<RoleDto> getRoleList() {
    return roleList;
  }

  public DomainDto setRoleList(List<RoleDto> roleList) {
    this.roleList = roleList;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public DomainDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public DomainDto setCode(String code) {
    this.code = code;
    return this;
  }

  public String getDisplayName() {
    return displayName;
  }

  public DomainDto setDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public Boolean getIsCustomizedLoginPage() {
    return isCustomizedLoginPage;
  }

  public DomainDto setIsCustomizedLoginPage(Boolean isCustomizedLoginPage) {
    this.isCustomizedLoginPage = isCustomizedLoginPage;
    return this;
  }
}
