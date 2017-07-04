package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.dto.GrpExtendValDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.GrpExtendValMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.support.AtrributeDefine;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组Profile操作的service实现.
 */
@Slf4j
@Service
public class GroupProfileInnerService extends TenancyBasedService {

  @Autowired
  private GroupExtendValInnerService groupExtendValInnerService;

  @Autowired
  private AttributeExtendInnerService attributeExtendInnerService;
  
  @Autowired
  private GrpExtendValMapper grpExtendValMapper;

  // Data filter
  @Resource(name = "grpExtendValDataFilter")
  private DataFilter grpExtendValDataFilter;

  /**
   * 更新组的扩展属性值.
   */
  @Transactional
  public void addOrUpdateUserAttributes(Integer grpId, Map<String, String> attributes) {
    CheckEmpty.checkEmpty(grpId, "groupId");
    if (attributes != null && !attributes.isEmpty()) {
      for (Entry<String, String> entry : attributes.entrySet()) {
        String attributeCode = entry.getKey();
        String value = entry.getValue();
        AttributeExtend attributeExtend = attributeExtendInnerService.queryAttributeExtendByCode(attributeCode);
        if (attributeExtend != null) {
          addOrUpdate(grpId, attributeExtend.getId(), value);
        } else {
          log.error("System define group attribute {} is not exist!", attributeCode);
        }
      }
    }
  }
  
  /**
   * 更新组的扩展属性.
   */
  @Transactional
  public void addOrUpdateGrpProfile(Integer grpId, Map<String, AttributeValModel> attributes) {
    CheckEmpty.checkEmpty(grpId, "groupId");
    if (attributes != null && !attributes.isEmpty()) {
      for (Entry<String, AttributeValModel> entry : attributes.entrySet()) {
        String attributeCode = entry.getKey();
        AttributeValModel attributeVal = entry.getValue();
        // attributes 中的属性如果不存在则需要先添加
        AttributeExtend attributeExtend = attributeExtendInnerService
            .addAttributeExtendIfNonExistent(attributeCode, attributeVal);
        String value = attributeVal != null ? attributeVal.getValue() : null;
        // 判断如果是System定义的Code,则需要通过其他方式去更新
        AtrributeDefine sysGrpAtrributeDefine =
            AtrributeDefine.getSystemDefineGroupAttribute(attributeCode);
        if (sysGrpAtrributeDefine != null) {
          // 系统预定义的扩展属性. 比如Grp表中定义好的属性.
          if (sysGrpAtrributeDefine.isWritable()) {
            groupExtendValInnerService.updateSystemDefineGrpAttribute(grpId,
                sysGrpAtrributeDefine.getDefineTable().getIdentityFieldName(),
                sysGrpAtrributeDefine.getDefineTable().getTableName(),
                sysGrpAtrributeDefine.getFieldName(), value);
          } else {
            log.debug("System define attribute {} is not writable, so update just ignore!");
          }
        } else {
          addOrUpdate(grpId, attributeExtend.getId(), value);
        }
      }
    }
  }
  

  /**
   * 添加或者更新用户属性.
   */
  private GrpExtendValDto addOrUpdate(Integer groupId, Long extendId, String value) {
    CheckEmpty.checkEmpty(groupId, "groupId");
    CheckEmpty.checkEmpty(extendId, "extendId");

    // 数据过滤
    grpExtendValDataFilter.addFieldsCheck(FilterType.EXSIT_DATA,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_GRP_ID, groupId),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_EXTEND_ID, extendId));

    GrpExtendValExample grpExtendValExample = new GrpExtendValExample();
    GrpExtendValExample.Criteria criteria = grpExtendValExample.createCriteria();
    criteria.andGrpIdEqualTo(groupId).andExtendIdEqualTo(extendId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<GrpExtendVal> existGrpExtendVal = grpExtendValMapper.selectByExample(grpExtendValExample);
    GrpExtendVal record;
    if (ObjectUtil.collectionIsEmptyOrNull(existGrpExtendVal)) {
      // add
      record = groupExtendValInnerService.addNew(groupId, extendId, value);
    } else {
      // update
      record = groupExtendValInnerService.update(groupId, extendId, value);
      record.setExtendId(extendId);
      record.setGrpId(groupId);
      record.setTenancyId(tenancyService.getTenancyIdWithCheck());
    }
    return BeanConverter.convert(record, GrpExtendValDto.class);
  }
}
