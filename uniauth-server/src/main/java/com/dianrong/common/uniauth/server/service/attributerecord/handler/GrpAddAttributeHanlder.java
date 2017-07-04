package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;
import com.dianrong.common.uniauth.server.service.inner.GroupExtendValInnerService;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpAddAttributeHanlder extends AbstractAttributeRecordHandler {

  private GroupExtendValInnerService groupExtendValInnerService;

  public GrpAddAttributeHanlder(GroupExtendValInnerService groupExtendValInnerService) {
    Assert.notNull(groupExtendValInnerService);
    this.groupExtendValInnerService = groupExtendValInnerService;
  }

  @Override
  public ExtendVal invokeTargetBefore(Object identity, Object extendId) {
    if (StringUtil.translateObjectToInteger(identity) == null) {
      throw new InvalidParameterTypeException(identity + " can not translate to a Integer!");
    }
    if (StringUtil.translateObjectToLong(extendId) == null) {
      throw new InvalidParameterTypeException(extendId + " can not translate to a Long!");
    }
    return null;
  }

  @Override
  protected AttributeRecords doInvokeTargetAfter(Object identity, Object extendId,
      ExtendVal originalVal) {
    Integer grpId = StringUtil.translateObjectToInteger(identity);
    Long attributeExtendId = StringUtil.translateObjectToLong(extendId);
    GrpExtendVal grpExtendVal =
        groupExtendValInnerService.queryByGrpIdAndExtendId(grpId, attributeExtendId);
    if (grpExtendVal == null) {
      log.warn(
          "add grp extend value records, the group extend value is null, so ignored. grpId: {}, extendId:{}",
          grpId, attributeExtendId);
      return null;
    }
    AttributeRecords record = new AttributeRecords();
    Date now = new Date();
    record.setCurVal(grpExtendVal.getValue());
    record.setOptType(RecordOperate.ADD.toString());
    record.setOptDate(now);
    record.setExtendId(attributeExtendId);
    record.setTenancyId(grpExtendVal.getTenancyId());
    record.setPreVal(null);
    return record;
  }
}
