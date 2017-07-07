package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpAttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.AttributeValIdentity;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.inner.GroupExtendValInnerService;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpUpdateAttributeHanlder extends GrpBaseHanlder {

  public GrpUpdateAttributeHanlder(GroupExtendValInnerService groupExtendValInnerService) {
    super(groupExtendValInnerService);
  }

  @Override
  public ExtendVal invokeTargetBefore(AttributeValIdentity valIdentity) {
    super.invokeTargetBefore(valIdentity);
    return super.query(valIdentity);
  }

  @Override
  protected AttributeRecords doInvokeTargetAfter(AttributeValIdentity valIdentity,
      ExtendVal originalVal) {
    if (!(originalVal instanceof GrpExtendVal)) {
      log.warn("update operate, but original value is not valid!");
      return null;
    }
    GrpExtendVal grpExtendVal = super.query(valIdentity);
    GrpExtendVal originalGrpExtendVal = (GrpExtendVal) originalVal;
    GrpAttributeRecords record = new GrpAttributeRecords();
    Date now = new Date();
    record.setCurVal(grpExtendVal == null ? null : grpExtendVal.getValue());
    record.setOptType(RecordOperate.UPDATE.toString());
    record.setOptDate(now);
    record.setExtendId(originalGrpExtendVal.getExtendId());
    record.setTenancyId(originalGrpExtendVal.getTenancyId());
    record.setPreVal(originalGrpExtendVal.getValue());
    record.setGrpId(originalGrpExtendVal.getGrpId());
    return record;
  }

}
