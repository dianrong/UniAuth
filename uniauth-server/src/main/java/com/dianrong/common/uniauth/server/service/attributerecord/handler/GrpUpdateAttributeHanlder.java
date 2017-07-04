package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpAttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;
import com.dianrong.common.uniauth.server.service.inner.GroupExtendValInnerService;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpUpdateAttributeHanlder extends AbstractAttributeRecordHandler {

  private GroupExtendValInnerService groupExtendValInnerService;

  public GrpUpdateAttributeHanlder(GroupExtendValInnerService groupExtendValInnerService) {
    Assert.notNull(groupExtendValInnerService);
    this.groupExtendValInnerService = groupExtendValInnerService;
  }

  @Override
  public ExtendVal invokeTargetBefore(Object identity, Object extendId) {
    Integer grpId = StringUtil.translateObjectToInteger(identity);
    if (grpId == null) {
      throw new InvalidParameterTypeException(identity + " can not translate to a Integer!");
    }
    Long attributeExtendId = StringUtil.translateObjectToLong(extendId);
    if (attributeExtendId == null) {
      throw new InvalidParameterTypeException(extendId + " can not translate to a Long!");
    }
    return groupExtendValInnerService.queryByGrpIdAndExtendId(grpId, attributeExtendId);
  }

  @Override
  protected AttributeRecords doInvokeTargetAfter(Object identity, Object extendId,
      ExtendVal originalVal) {
    Integer grpId = StringUtil.translateObjectToInteger(identity);
    Long attributeExtendId = StringUtil.translateObjectToLong(extendId);
    if (!(originalVal instanceof GrpExtendVal)) {
      log.warn(
          "update operate, but original value is not valid!");
      return null;
    }
    GrpExtendVal grpExtendVal =
        groupExtendValInnerService.queryByGrpIdAndExtendId(grpId, attributeExtendId);
    GrpExtendVal originalGrpExtendVal = (GrpExtendVal)originalVal;
    GrpAttributeRecords record = new GrpAttributeRecords();
    Date now = new Date();
    record.setCurVal(grpExtendVal == null? null: grpExtendVal.getValue());
    record.setOptType(RecordOperate.UPDATE.toString());
    record.setOptDate(now);
    record.setExtendId(attributeExtendId);
    record.setTenancyId(originalGrpExtendVal.getTenancyId());
    record.setPreVal(originalGrpExtendVal.getValue());
    record.setGrpId(grpId);
    return record;
  }

}
