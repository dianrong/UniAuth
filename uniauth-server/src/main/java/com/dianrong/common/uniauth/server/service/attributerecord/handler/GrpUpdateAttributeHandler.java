package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpAttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.AttributeValIdentity;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.inner.GroupExtendValInnerService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class GrpUpdateAttributeHandler extends GrpBaseHandler {

  public GrpUpdateAttributeHandler(GroupExtendValInnerService groupExtendValInnerService) {
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
    String originalValue = originalGrpExtendVal.getValue();
    String currentValue = grpExtendVal == null ? null : grpExtendVal.getValue();
    // 值没有变动,不记录
    if (ObjectUtil.objectEqual(originalValue, currentValue)) {
      return null;
    }
    GrpAttributeRecords record = new GrpAttributeRecords();
    Date now = new Date();
    record.setCurVal(currentValue);
    record.setOptType(RecordOperate.UPDATE.toString());
    record.setOptDate(now);
    record.setExtendId(originalGrpExtendVal.getExtendId());
    record.setTenancyId(originalGrpExtendVal.getTenancyId());
    record.setPreVal(originalValue);
    record.setGrpId(originalGrpExtendVal.getGrpId());
    return record;
  }

}
