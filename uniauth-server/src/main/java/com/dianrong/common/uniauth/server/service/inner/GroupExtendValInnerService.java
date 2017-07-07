package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.GrpExtendValMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordType;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.CheckEmpty;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组扩展属性值Service的内部实现.
 */
@Service
public class GroupExtendValInnerService extends TenancyBasedService {

  @Autowired
  private GrpExtendValMapper grpExtendValMapper;

  // Data filter
  @Resource(name = "grpExtendValDataFilter")
  private DataFilter dataFilter;

  /**
   * 新增.
   */
  @Transactional
  @ExtendAttributeRecord(type = RecordType.GROUP, operate = RecordOperate.ADD)
  public GrpExtendVal addNew(Integer grpId, Long extendId, String value) {
    CheckEmpty.checkEmpty(grpId, "grpId");
    CheckEmpty.checkEmpty(extendId, "extendId");

    // 数据过滤
    dataFilter.addFieldsCheck(FilterType.EXSIT_DATA,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_GRP_ID, grpId),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_EXTEND_ID, extendId));

    GrpExtendVal record = new GrpExtendVal();
    record.setExtendId(extendId);
    record.setGrpId(grpId);
    record.setValue(value);
    record.setTenancyId(tenancyService.getTenancyIdWithCheck());
    grpExtendValMapper.insert(record);
    return record;
  }

  /**
   * 更新.
   */
  @Transactional
  @ExtendAttributeRecord(type = RecordType.GROUP, operate = RecordOperate.UPDATE)
  public void update(Integer grpId, Long extendId, String value) {
    CheckEmpty.checkEmpty(grpId, "grpId");
    CheckEmpty.checkEmpty(extendId, "extendId");
    grpExtendValMapper.updateValue(grpId, extendId, value);
  }

  /**
   * 根据GrpId和扩展属性Id查找信息.
   */
  public GrpExtendVal queryByGrpIdAndExtendId(Integer grpId, Long extendId) {
    GrpExtendValExample grpExtendValExample = new GrpExtendValExample();
    GrpExtendValExample.Criteria criteria = grpExtendValExample.createCriteria();
    criteria.andGrpIdEqualTo(grpId).andExtendIdEqualTo(extendId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<GrpExtendVal> grpExtendValList = grpExtendValMapper.selectByExample(grpExtendValExample);
    if (ObjectUtil.collectionIsEmptyOrNull(grpExtendValList)) {
      return null;
    }
    return grpExtendValList.get(0);
  }

  /**
   * 根据主键id查找.
   */
  public GrpExtendVal queryByPrimaykey(Long primaryId) {
    CheckEmpty.checkEmpty(primaryId, "primaryId");
    return grpExtendValMapper.selectByPrimaryKey(primaryId);
  }
}
