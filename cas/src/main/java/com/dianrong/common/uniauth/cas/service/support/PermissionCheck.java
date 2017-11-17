package com.dianrong.common.uniauth.cas.service.support;

/**
 * 权限判断实现接口.
 */
public interface PermissionCheck {

  /**
   * 指定用户是否有权限踢账号.
   * @param userId 踢账号的用户id.
   * @param beKickOutUserId 被踢出账号的用户id.
   */
  boolean canKickOutAccount(Long userId, Long beKickOutUserId);
}
