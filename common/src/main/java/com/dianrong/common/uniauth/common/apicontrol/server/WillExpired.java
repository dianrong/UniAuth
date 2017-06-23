package com.dianrong.common.uniauth.common.apicontrol.server;

/**
 * 会过期对象.
 *
 * @author wanglin
 */
public interface WillExpired {

  /**
   * 获取创建时间.
   *
   * @return 创建时间
   */
  long getCreateTime();

  /**
   * 获取过期时间.
   *
   * @return 过期时间
   */
  long getExpireTime();
}
