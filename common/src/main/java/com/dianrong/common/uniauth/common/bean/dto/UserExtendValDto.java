package com.dianrong.common.uniauth.common.bean.dto;

import com.dianrong.common.uniauth.common.cons.AppConstants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.ToString;

/**
 * @author wenlongchen.
 * @since May 16, 2016
 */
@ToString
@ApiModel("用户扩展值信息")
public class UserExtendValDto extends TenancyBaseDto {

  private static final long serialVersionUID = -4191944113318490773L;

  @ApiModelProperty("主键id")
  private Long id;

  @ApiModelProperty("用户id")
  private Long userId;

  @ApiModelProperty("扩展值")
  private String value;

  /**
   * 删除该字段. 为了兼容以前的API,在DTO中存在的status都为启用
   */
  @Deprecated
  @ApiModelProperty("状态(0:启用,1:禁用)")
  private Byte status = AppConstants.STATUS_ENABLED;

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

  public UserExtendValDto setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public UserExtendValDto setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Long getExtendId() {
    return extendId;
  }

  public UserExtendValDto setExtendId(Long extendId) {
    this.extendId = extendId;
    return this;
  }

  public String getValue() {
    return value;
  }

  public UserExtendValDto setValue(String value) {
    this.value = value;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public UserExtendValDto setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public String getExtendCode() {
    return extendCode;
  }

  public UserExtendValDto setExtendCode(String extendCode) {
    this.extendCode = extendCode;
    return this;
  }

  public String getExtendDescription() {
    return extendDescription;
  }

  public UserExtendValDto setExtendDescription(String extendDescription) {
    this.extendDescription = extendDescription;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public UserExtendValDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public UserExtendValDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }
}

