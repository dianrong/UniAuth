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
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

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
   * 更新系统自定义的组属性值.
   * 
   * @param grpId 组Id
   * @param idFieldName 在表中用户id所在的字段名
   * @param tableName 需要更新的表名.
   * @param fieldName 需要更新的字段名.
   * @param value 属性需要更新成的值.
   */
  @Transactional
  public void updateSystemDefineGrpAttribute(Integer grpId, String idFiledName, String tableName,
      String fieldName, String value) {
    CheckEmpty.checkEmpty(grpId, "grpId");
    CheckEmpty.checkEmpty(idFiledName, "idFiledName");
    CheckEmpty.checkEmpty(tableName, "tableName");
    CheckEmpty.checkEmpty(fieldName, "fieldName");
    Map<String, Object> params = Maps.newHashMap();
    params.put("grpId", grpId);
    params.put("tableName", tableName);
    params.put("idFiledName", idFiledName);
    params.put("fieldName", fieldName);
    params.put("value", value);
    grpExtendValMapper.updateSystemDefineGrpAttribute(params);
  }
  
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
