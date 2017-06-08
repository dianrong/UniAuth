package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.bean.ThirdAccountType;

import java.util.Date;

import lombok.ToString;

@ToString
public class UserThirdAccountParam extends Operator {

  private static final long serialVersionUID = -383089391366171367L;

  private Long id;
  
  private Long userId;

  private String thirdAccount;

  private ThirdAccountType type;

  private Date lastLoginIp;

  private Date lastLoginTime;

  private Date createDate;

  private Date lastUpdate;

  public Long getId() {
    return id;
  }

  public UserThirdAccountParam setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public UserThirdAccountParam setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public String getThirdAccount() {
    return thirdAccount;
  }

  public UserThirdAccountParam setThirdAccount(String thirdAccount) {
    this.thirdAccount = thirdAccount;
    return this;
  }

  public ThirdAccountType getType() {
    return type;
  }

  public UserThirdAccountParam setType(ThirdAccountType type) {
    this.type = type;
    return this;
  }
  
  public Date getLastLoginIp() {
    return lastLoginIp;
  }

  public UserThirdAccountParam setLastLoginIp(Date lastLoginIp) {
    this.lastLoginIp = lastLoginIp;
    return this;
  }

  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public UserThirdAccountParam setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public UserThirdAccountParam setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public UserThirdAccountParam setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }
}
