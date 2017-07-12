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
public class GrpAddAttributeHanlder extends GrpBaseHanlder {

  public GrpAddAttributeHanlder(GroupExtendValInnerService groupExtendValInnerService) {
    super(groupExtendValInnerService);
  }

  @Override
  protected AttributeRecords doInvokeTargetAfter(AttributeValIdentity valIdentity,
      ExtendVal originalVal) {
    GrpExtendVal grpExtendVal = query(valIdentity);
    if (grpExtendVal == null) {
      log.warn(
          "add grp extend value records, the group extend value is null, "
          + "so ignored. the identity: {}",
          valIdentity);
      return null;
    }
    GrpAttributeRecords record = new GrpAttributeRecords();
    Date now = new Date();
    record.setCurVal(grpExtendVal.getValue());
    record.setOptType(RecordOperate.ADD.toString());
    record.setOptDate(now);
    record.setExtendId(grpExtendVal.getExtendId());
    record.setTenancyId(grpExtendVal.getTenancyId());
    record.setGrpId(grpExtendVal.getGrpId());
    record.setPreVal(null);
    return record;
  }
}
