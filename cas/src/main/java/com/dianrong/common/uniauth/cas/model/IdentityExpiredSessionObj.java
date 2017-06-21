package com.dianrong.common.uniauth.cas.model;

import com.dianrong.common.uniauth.common.util.Assert;

import java.io.Serializable;

/**
 * 绑定了Identity信息的Session过期对象.
 */
public class IdentityExpiredSessionObj<T extends Serializable> extends ExpiredSessionObj<T> {

  private static final long serialVersionUID = -5592978188308898593L;
  
  private final String identity;
  
  public String getIdentity() {
    return identity;
  }
  
  public IdentityExpiredSessionObj(T content, long lifeMilles, String identity) {
    super(content, lifeMilles);
    Assert.notNull(identity);
    this.identity = identity;
  }

  /**
   * 构造一个IdentityExpiredSessionObj
   */
  public static <E extends Serializable> IdentityExpiredSessionObj<E> build(E content, long lifeMilles, String identity) {
    return new IdentityExpiredSessionObj<E>(content, lifeMilles, identity);
  }
}
