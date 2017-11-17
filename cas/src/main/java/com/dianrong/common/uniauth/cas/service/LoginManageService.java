package com.dianrong.common.uniauth.cas.service;

import com.dianrong.common.uniauth.cas.exp.InvalidPermissionException;
import com.dianrong.common.uniauth.cas.registry.TgtIdentityRedisTicketRegistry;
import com.dianrong.common.uniauth.cas.service.support.PermissionCheck;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.util.Assert;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 对登录状态进行管理.
 *
 * @author wanglin
 */

@Slf4j
@Service
public class LoginManageService {

  @Autowired
  private PermissionCheck permissionCheck;

  @Resource(name = "ticketRegistry")
  private TgtIdentityRedisTicketRegistry tgtIdentityRedisTicketRegistry;

  @Autowired
  private UserInfoManageService userInfoManageService;

  /**
   * 主动登出某个登录账号.
   *
   * @throws InvalidPermissionException 没有权限操作.
   **/
  public void kickOutAccount(Long userId, String identity, String tenancyCode)
      throws InvalidPermissionException {
    Assert.notNull(userId, "userId can not be null");
    Assert.notNull(identity, "identity can not be null");
    Assert.notNull(tenancyCode, "tenancyCode can not be null");
    UserDto userDto = userInfoManageService.queryUserByIdentity(identity, tenancyCode);
    if (userDto == null) {
      return;
    }
    kickOutAccount(userId, userDto.getId());
  }

  /**
   * 主动登出某个登录账号.
   *
   * @throws InvalidPermissionException 没有权限操作.
   **/
  public void kickOutAccount(Long userId, Long beKickOutUserId) throws InvalidPermissionException {
    Assert.notNull(userId, "userId can not be null");
    Assert.notNull(beKickOutUserId, "beKickOutUserId can not be null");

    if (!permissionCheck.canKickOutAccount(userId, beKickOutUserId)) {
      throw new InvalidPermissionException(
          userId + " has permission to kick out " + beKickOutUserId);
    }
    // 查找被踢出用户所在的tgt并干掉
    tgtIdentityRedisTicketRegistry.removeTgtByUserId(beKickOutUserId);
  }
}
