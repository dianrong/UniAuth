package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserAttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.AttributeValIdentity;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.inner.UserExtendValInnerService;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAddAttributeHanlder extends UserBaseHanlder {

  public UserAddAttributeHanlder(UserExtendValInnerService userExtendValInnerService) {
    super(userExtendValInnerService);
  }

  @Override
  protected AttributeRecords doInvokeTargetAfter(AttributeValIdentity valIdentity,
      ExtendVal originalVal) {
    UserExtendVal userExtendVal = super.query(valIdentity);
    if (userExtendVal == null) {
      log.warn(
          "add user extend value records, the user extend value is null, so ignored. identity: {}",
          valIdentity);
      return null;
    }
    UserAttributeRecords record = new UserAttributeRecords();
    Date now = new Date();
    record.setPreVal(null);
    record.setCurVal(userExtendVal.getValue());
    record.setOptType(RecordOperate.ADD.toString());
    record.setOptDate(now);
    record.setExtendId(userExtendVal.getExtendId());
    record.setTenancyId(userExtendVal.getTenancyId());
    record.setUserId(userExtendVal.getUserId());
    return record;
  }
}
