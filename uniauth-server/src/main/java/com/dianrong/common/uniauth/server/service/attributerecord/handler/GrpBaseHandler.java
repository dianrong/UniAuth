package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.AttributeValIdentity;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;
import com.dianrong.common.uniauth.server.service.inner.GroupExtendValInnerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GrpBaseHandler extends AbstractAttributeRecordHandler {

  private GroupExtendValInnerService groupExtendValInnerService;

  public GrpBaseHandler(GroupExtendValInnerService groupExtendValInnerService) {
    Assert.notNull(groupExtendValInnerService);
    this.groupExtendValInnerService = groupExtendValInnerService;
  }

  @Override
  public ExtendVal invokeTargetBefore(AttributeValIdentity valIdentity) {
    if (valIdentity == null) {
      throw new InvalidParameterTypeException("The identity can not be null!");
    }
    if (valIdentity.getPrimaryId() == null) {
      if (StringUtil.translateObjectToInteger(valIdentity.getIdentity()) == null) {
        throw new InvalidParameterTypeException(
            valIdentity.getIdentity() + " can not translate to a Integer!");
      }
    }
    return null;
  }

  /**
   * 查找记录.
   */
  protected GrpExtendVal query(AttributeValIdentity valIdentity) {
    Long primaryId = valIdentity.getPrimaryId();
    if (primaryId != null) {
      log.debug(
          "primary id is exist, so get group extend value record by primary id. Primary id {}",
          primaryId);
      return groupExtendValInnerService.queryByPrimaryKey(primaryId);
    }
    Integer grpId = StringUtil.translateObjectToInteger(valIdentity.getIdentity());
    Long extendId = valIdentity.getExtendId();
    log.debug("Get group extend value by groupId: {} and extendId: {}.", grpId, extendId);
    return groupExtendValInnerService.queryByGrpIdAndExtendId(grpId, extendId);
  }
}
