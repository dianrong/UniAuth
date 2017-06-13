package com.dianrong.common.uniauth.server.data.query;

import java.util.Date;

/**
 * . UserPwdLog 配套的查询条件model
 *
 * @author wanglin
 */
public class UserPwdLogQueryParam {

  private Long id;

  private Long userId;

  private Date createDate;

  private Long tenancyId;

  private Date createDateBegin;

  private Date createDateEnd;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getCreateDateBegin() {
    return createDateBegin;
  }

  public void setCreateDateBegin(Date createDateBegin) {
    this.createDateBegin = createDateBegin;
  }

  public Date getCreateDateEnd() {
    return createDateEnd;
  }

  public void setCreateDateEnd(Date createDateEnd) {
    this.createDateEnd = createDateEnd;
  }

  public Long getTenancyId() {
    return tenancyId;
  }

  public void setTenancyId(Long tenancyId) {
    this.tenancyId = tenancyId;
  }
}
