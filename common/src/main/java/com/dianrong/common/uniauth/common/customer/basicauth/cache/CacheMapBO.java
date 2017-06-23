package com.dianrong.common.uniauth.common.customer.basicauth.cache;

/**
 * Created by denghb on 6/22/17.
 */
public class CacheMapBO {

  private Object value;

  private Long expires;

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Long getExpires() {
    return expires;
  }

  public void setExpires(Long expires) {
    this.expires = expires;
  }

}
