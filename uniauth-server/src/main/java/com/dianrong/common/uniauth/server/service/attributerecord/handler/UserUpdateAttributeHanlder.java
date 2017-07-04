package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserAttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;
import com.dianrong.common.uniauth.server.service.inner.UserExtendValInnerService;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserUpdateAttributeHanlder extends AbstractAttributeRecordHandler {

  private UserExtendValInnerService userExtendValInnerService;
  
  public UserUpdateAttributeHanlder(UserExtendValInnerService userExtendValInnerService) {
    Assert.notNull(userExtendValInnerService);
    this.userExtendValInnerService = userExtendValInnerService;
  }
  
  @Override
  public ExtendVal invokeTargetBefore(Object identity, Object extendId) {
    Long userId = StringUtil.translateObjectToLong(identity);
    if (userId == null) {
      throw new InvalidParameterTypeException(identity + " can not translate to a Long!");
    }
    Long attributeExtendId = StringUtil.translateObjectToLong(extendId);
    if (attributeExtendId == null) {
      throw new InvalidParameterTypeException(extendId + " can not translate to a Long!");
    }
    return userExtendValInnerService.queryByUserIdAndExtendId(userId, attributeExtendId);
  }

  @Override
  protected AttributeRecords doInvokeTargetAfter(Object identity, Object extendId,
      ExtendVal originalVal) {
    Long userId = StringUtil.translateObjectToLong(identity);
    Long attributeExtendId = StringUtil.translateObjectToLong(extendId);
    if (!(originalVal instanceof GrpExtendVal)) {
      log.warn(
          "update operate, but original value is not valid!");
      return null;
    }
    UserExtendVal userExtendVal =
        userExtendValInnerService.queryByUserIdAndExtendId(userId, attributeExtendId);
    UserExtendVal originalUserExtendVal = (UserExtendVal)originalVal;
    UserAttributeRecords record = new UserAttributeRecords();
    Date now = new Date();
    record.setCurVal(userExtendVal == null? null: userExtendVal.getValue());
    record.setOptType(RecordOperate.UPDATE.toString());
    record.setOptDate(now);
    record.setExtendId(attributeExtendId);
    record.setTenancyId(originalUserExtendVal.getTenancyId());
    record.setPreVal(originalUserExtendVal.getValue());
    record.setUserId(userId);
    return record;
  }

}
