package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.ToString;

@ToString
@ApiModel("组扩展值信息")
public class GrpExtendValDto extends TenancyBaseDto {

  private static final long serialVersionUID = -4191944113318490773L;

  @ApiModelProperty("主键id")
  private Long id;

  @ApiModelProperty("用户id")
  private Integer grpId;

  @ApiModelProperty("扩展值")
  private String value;

  @ApiModelProperty("扩展属性id")
  private Long extendId;

  @ApiModelProperty("扩展属性code")
  private String extendCode;

  @ApiModelProperty("扩展属性的描述信息")
  private String extendDescription;
  
  @ApiModelProperty("创建时间")
  private Date createDate;

  @ApiModelProperty("最近更新时间")
  private Date lastUpdate;

  public Long getId() {
    return id;
  }

  public GrpExtendValDto setId(Long id) {
    this.id = id;
    return this;
  }

  public Integer getGrpId() {
    return grpId;
  }

  public GrpExtendValDto setGrpId(Integer grpId) {
    this.grpId = grpId;
    return this;
  }

  public String getValue() {
    return value;
  }

  public GrpExtendValDto setValue(String value) {
    this.value = value;
    return this;
  }

  public Long getExtendId() {
    return extendId;
  }

  public GrpExtendValDto setExtendId(Long extendId) {
    this.extendId = extendId;
    return this;
  }

  public String getExtendCode() {
    return extendCode;
  }

  public GrpExtendValDto setExtendCode(String extendCode) {
    this.extendCode = extendCode;
    return this;
  }

  public String getExtendDescription() {
    return extendDescription;
  }

  public GrpExtendValDto setExtendDescription(String extendDescription) {
    this.extendDescription = extendDescription;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public GrpExtendValDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public GrpExtendValDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }
}

