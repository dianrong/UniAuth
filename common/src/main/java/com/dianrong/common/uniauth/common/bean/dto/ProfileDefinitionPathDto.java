package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

import lombok.ToString;

@ToString
@ApiModel("Profile的父子关联关系信息")
public class ProfileDefinitionPathDto extends TenancyBaseDto {
  
  private static final long serialVersionUID = 4634701891484759219L;

  @ApiModelProperty("该关联关系对应的父profileId")
  private Long ancestor;

  @ApiModelProperty("该关联关系对应的子profileId")
  private Long descendant;

  @ApiModelProperty("记录创建时间")
  private Date createDate;

  @ApiModelProperty("记录更新的时间")
  private Date lastUpdate;
  
  @ApiModelProperty("以树的结构表现关联关系")
  private List<ProfileDefinitionPathDto> subProfileInfo;

  public Long getAncestor() {
    return ancestor;
  }

  public ProfileDefinitionPathDto setAncestor(Long ancestor) {
    this.ancestor = ancestor;
    return this;
  }

  public Long getDescendant() {
    return descendant;
  }

  public ProfileDefinitionPathDto setDescendant(Long descendant) {
    this.descendant = descendant;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public ProfileDefinitionPathDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public ProfileDefinitionPathDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  public List<ProfileDefinitionPathDto> getSubProfileInfo() {
    return subProfileInfo;
  }

  public ProfileDefinitionPathDto setSubProfileInfo(List<ProfileDefinitionPathDto> subProfileInfo) {
    this.subProfileInfo = subProfileInfo;
    return this;
  }
}
