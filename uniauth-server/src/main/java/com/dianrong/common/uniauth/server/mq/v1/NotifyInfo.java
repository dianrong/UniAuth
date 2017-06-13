package com.dianrong.common.uniauth.server.mq.v1;

/**
 * 通知信息.
 *
 * @author wanglin
 */
public interface NotifyInfo {

  /**
   * 获取通知信息类型.
   *
   * @return 通知信息类型
   */
  NotifyInfoType getType();
}
