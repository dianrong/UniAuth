package com.dianrong.common.uniauth.common.bean.dto;

import com.dianrong.common.uniauth.common.bean.ThirdAccountType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.ToString;

@ApiModel("用户和第三方账号的关联")
@ToString
public class UserThirdAccountDto extends TenancyBaseDto {

  private static final long serialVersionUID = -383089391366171367L;

  @ApiModelProperty("主键id")
  private Long id;
  
  @ApiModelProperty("用户id")
  private Long userId;

  @ApiModelProperty("第三方账号")
  private String thirdAccount;

  @ApiModelProperty("第三方账号类型")
  private ThirdAccountType type;

  @ApiModelProperty("第三账号最近一次在Uniauth的登陆时间")
  private Date lastLoginIp;

  @ApiModelProperty("第三方账号最近一次在Uniauth登陆的IP")
  private Date lastLoginTime;

  @ApiModelProperty("创建时间")
  private Date createDate;

  @ApiModelProperty("更新时间")
  private Date lastUpdate;

  public Long getId() {
    return id;
  }

  public UserThirdAccountDto setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public UserThirdAccountDto setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public String getThirdAccount() {
    return thirdAccount;
  }

  public UserThirdAccountDto setThirdAccount(String thirdAccount) {
    this.thirdAccount = thirdAccount;
    return this;
  }

  public ThirdAccountType getType() {
    return type;
  }

  public UserThirdAccountDto setType(ThirdAccountType type) {
    this.type = type;
    return this;
  }
  
  public Date getLastLoginIp() {
    return lastLoginIp;
  }

  public UserThirdAccountDto setLastLoginIp(Date lastLoginIp) {
    this.lastLoginIp = lastLoginIp;
    return this;
  }

  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public UserThirdAccountDto setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public UserThirdAccountDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public UserThirdAccountDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }
}
