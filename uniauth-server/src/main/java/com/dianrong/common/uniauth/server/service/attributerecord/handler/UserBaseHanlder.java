package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.AttributeValIdentity;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;
import com.dianrong.common.uniauth.server.service.inner.UserExtendValInnerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class UserBaseHanlder extends AbstractAttributeRecordHandler {

  private UserExtendValInnerService userExtendValInnerService;

  public UserBaseHanlder(UserExtendValInnerService userExtendValInnerService) {
    Assert.notNull(userExtendValInnerService);
    this.userExtendValInnerService = userExtendValInnerService;
  }

  @Override
  public ExtendVal invokeTargetBefore(AttributeValIdentity valIdentity) {
    if (valIdentity == null) {
      throw new InvalidParameterTypeException("The identity can not be null!");
    }
    if (valIdentity.getPrimaryId() == null) {
      if (StringUtil.translateObjectToLong(valIdentity.getIdentity()) == null) {
        throw new InvalidParameterTypeException(
            valIdentity.getIdentity() + " can not translate to a Long!");
      }
    }
    return null;
  }

  /**
   * 查找记录.
   */
  protected UserExtendVal query(AttributeValIdentity valIdentity) {
    Long primaryId = valIdentity.getPrimaryId();
    if (primaryId != null) {
      log.debug("primary id is exist, so get user extend value record by primary id. Primary id {}",
          primaryId);
      return userExtendValInnerService.queryByPrimaykey(primaryId);
    }
    Long userId = StringUtil.translateObjectToLong(valIdentity.getIdentity());
    Long extendId = valIdentity.getExtendId();
    log.debug("Get group extend value by userId: {} and extendId: {}.", userId, extendId);
    return userExtendValInnerService.queryByUserIdAndExtendId(userId, extendId);
  }

}
