package com.dianrong.common.uniauth.server.mq.v1.ninfo;

import lombok.ToString;

/**
 * @author wanglin.
 */
@ToString
public class BaseUserNotifyInfo extends BaseNotifyInfo {

  /**
   * 用户id.
   */
  protected Long userId;

  public Long getUserId() {
    return userId;
  }

  public BaseUserNotifyInfo setUserId(Long userId) {
    this.userId = userId;
    return this;
  }
}
