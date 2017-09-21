package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserAttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.AttributeValIdentity;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.inner.UserExtendValInnerService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class UserUpdateAttributeHanlder extends UserBaseHandler {

  public UserUpdateAttributeHanlder(UserExtendValInnerService userExtendValInnerService) {
    super(userExtendValInnerService);
  }

  @Override
  public ExtendVal invokeTargetBefore(AttributeValIdentity valIdentity) {
    super.invokeTargetBefore(valIdentity);
    return super.query(valIdentity);
  }

  @Override
  protected AttributeRecords doInvokeTargetAfter(AttributeValIdentity valIdentity,
      ExtendVal originalVal) {
    if (!(originalVal instanceof UserExtendVal)) {
      log.warn("update operate, but original value is not valid!");
      return null;
    }
    UserExtendVal originalUserExtendVal = (UserExtendVal) originalVal;
    UserExtendVal userExtendVal = super.query(valIdentity);
    String originalValue = originalUserExtendVal.getValue();
    String currentValue = userExtendVal == null ? null : userExtendVal.getValue();
    // 值没有变动,不记录
    if (ObjectUtil.objectEqual(originalValue, currentValue)) {
      return null;
    }

    UserAttributeRecords record = new UserAttributeRecords();
    Date now = new Date();
    record.setCurVal(currentValue);
    record.setOptType(RecordOperate.UPDATE.toString());
    record.setOptDate(now);
    record.setExtendId(originalUserExtendVal.getExtendId());
    record.setTenancyId(originalUserExtendVal.getTenancyId());
    record.setPreVal(originalValue);
    record.setUserId(originalUserExtendVal.getUserId());
    return record;
  }

}
