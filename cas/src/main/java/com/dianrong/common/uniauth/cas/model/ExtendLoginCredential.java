package com.dianrong.common.uniauth.cas.model;

import com.dianrong.common.uniauth.common.util.Assert;
import java.io.Serializable;
import org.jasig.cas.authentication.Credential;

public class ExtendLoginCredential implements Credential, Serializable {

  private static final long serialVersionUID = -5552074507929396707L;

  /**
   * 用户id.
   */
  private Long userId;

  /**
   * 延续登录次数.
   */
  private Integer extendLoginTimes;

  /**
   * 构造一个CasUsernamePasswordCredential.
   */
  public ExtendLoginCredential(Long userId, Integer extendLoginTimes) {
    Assert.notNull(userId, "UserId can not be null");
    Assert.notNull(extendLoginTimes, "extendLoginTimes can not be null");
    this.userId = userId;
    this.extendLoginTimes = extendLoginTimes;
  }

  @Override
  public String getId() {
    return this.userId.toString();
  }

  public Long getUserId() {
    return userId;
  }

  public Integer getExtendLoginTimes() {
    return extendLoginTimes;
  }

  /**
   * 再登录一次之后的登录次数.
   */
  public Integer loginAgainExtendLoginTimes() {
    return extendLoginTimes + 1;
  }
}
