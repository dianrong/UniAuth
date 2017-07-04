package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserAttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;
import com.dianrong.common.uniauth.server.service.inner.UserExtendValInnerService;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAddAttributeHanlder extends AbstractAttributeRecordHandler {

  private UserExtendValInnerService userExtendValInnerService;
  
  public UserAddAttributeHanlder(UserExtendValInnerService userExtendValInnerService) {
    Assert.notNull(userExtendValInnerService);
    this.userExtendValInnerService = userExtendValInnerService;
  }
  
  @Override
  public ExtendVal invokeTargetBefore(Object identity, Object extendId) {
    if (StringUtil.translateObjectToLong(identity) == null) {
      throw new InvalidParameterTypeException(identity + " can not translate to a Long!");
    }
    if (StringUtil.translateObjectToLong(extendId) == null) {
      throw new InvalidParameterTypeException(extendId + " can not translate to a Long!");
    }
    return null;
  }

  @Override
  protected AttributeRecords doInvokeTargetAfter(Object identity, Object extendId,
      ExtendVal originalVal) {
    Long userId = StringUtil.translateObjectToLong(identity);
    Long attributeExtendId = StringUtil.translateObjectToLong(extendId);
    UserExtendVal userExtendVal =
        userExtendValInnerService.queryByUserIdAndExtendId(userId, attributeExtendId);
    if (userExtendVal == null) {
      log.warn(
          "add user extend value records, the user extend value is null, so ignored. userId: {}, extendId:{}",
          userId, attributeExtendId);
      return null;
    }
    UserAttributeRecords record = new UserAttributeRecords();
    Date now = new Date();
    record.setCurVal(userExtendVal.getValue());
    record.setOptType(RecordOperate.ADD.toString());
    record.setOptDate(now);
    record.setExtendId(attributeExtendId);
    record.setTenancyId(userExtendVal.getTenancyId());
    record.setPreVal(null);
    record.setUserId(userId);
    return record;
  }

}
