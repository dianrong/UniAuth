package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

import lombok.ToString;

/**
 * Created by Arc on 7/4/2016.
 */
@ToString
@ApiModel("标签信息")
public class TagDto extends TenancyBaseDto {

  private static final long serialVersionUID = -6998161636801183621L;

  @ApiModelProperty("主键id")
  private Integer id;
  @ApiModelProperty("编码")
  private String code;
  @ApiModelProperty("状态(0:启用,1:禁用)")
  private Byte status;
  @ApiModelProperty("描述")
  private String description;
  @ApiModelProperty("标签类型id")
  private Integer tagTypeId;
  @ApiModelProperty("标签类型code")
  private String tagTypeCode;
  @ApiModelProperty("创建时间")
  private Date createDate;
  @ApiModelProperty("最近更新时间")
  private Date lastUpdate;
  @ApiModelProperty("对应的域信息")
  private DomainDto domain;

  // whether this tag directly connected with a user
  @ApiModelProperty("辅助字段,判断标签是否与某个用户有关联关系")
  private Boolean tagUserChecked;

  // whether this tag directly connected with a group
  @ApiModelProperty("辅助字段,判断标签是否与某个组有关联关系")
  private Boolean tagGrouprChecked;

  public Boolean getTagUserChecked() {
    return tagUserChecked;
  }

  public TagDto setTagUserChecked(Boolean tagUserChecked) {
    this.tagUserChecked = tagUserChecked;
    return this;
  }

  public Boolean getTagGrouprChecked() {
    return tagGrouprChecked;
  }

  public TagDto setTagGrouprChecked(Boolean tagGrouprChecked) {
    this.tagGrouprChecked = tagGrouprChecked;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public TagDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public TagDto setCode(String code) {
    this.code = code;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public TagDto setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public TagDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public TagDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public TagDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public Integer getTagTypeId() {
    return tagTypeId;
  }

  public TagDto setTagTypeId(Integer tagTypeId) {
    this.tagTypeId = tagTypeId;
    return this;
  }

  public String getTagTypeCode() {
    return tagTypeCode;
  }

  public TagDto setTagTypeCode(String tagTypeCode) {
    this.tagTypeCode = tagTypeCode;
    return this;
  }

  public DomainDto getDomain() {
    return domain;
  }

  public TagDto setDomain(DomainDto domain) {
    this.domain = domain;
    return this;
  }
}
