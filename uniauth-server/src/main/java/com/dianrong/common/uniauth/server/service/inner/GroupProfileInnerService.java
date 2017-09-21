package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.GrpExtendValDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.GrpExtendValMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.attribute.exp.InvalidPropertyValueException;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.support.AttributeDefine;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 组Profile操作的service实现.
 */
@Slf4j
@Service
public class GroupProfileInnerService extends TenancyBasedService {

  @Autowired
  private GroupExtendValInnerService groupExtendValInnerService;

  @Autowired
  private ExtendValInnerService extendValInnerService;

  @Autowired
  private AttributeExtendInnerService attributeExtendInnerService;

  @Autowired
  private GrpExtendValMapper grpExtendValMapper;

  @Resource(name = "grpExtendValDataFilter")
  private DataFilter dataFilter;

  @Resource(name = "groupDataFilter")
  private DataFilter groupDataFilter;

  /**
   * 更新组的扩展属性值.
   */
  @Transactional
  public void addOrUpdateUserAttributes(Integer grpId, Map<String, String> attributes) {
    CheckEmpty.checkEmpty(grpId, "groupId");
    groupDataFilter.addFieldCheck(FilterType.EXIST, FieldType.FIELD_TYPE_ID, grpId);
    if (!CollectionUtils.isEmpty(attributes)) {
      for (Entry<String, String> entry : attributes.entrySet()) {
        String attributeCode = entry.getKey();
        String value = entry.getValue();
        AttributeExtend attributeExtend =
            attributeExtendInnerService.addAttributeExtendIfNonExistent(attributeCode, null);
        addOrUpdate(grpId, attributeExtend.getId(), value);
      }
    }
  }

  /**
   * 更新组的扩展属性.
   */
  @Transactional
  public void addOrUpdateGrpProfile(Integer grpId, Map<String, AttributeValModel> attributes) {
    CheckEmpty.checkEmpty(grpId, "groupId");
    groupDataFilter.addFieldCheck(FilterType.EXIST, FieldType.FIELD_TYPE_ID, grpId);
    if (!CollectionUtils.isEmpty(attributes)) {
      for (Entry<String, AttributeValModel> entry : attributes.entrySet()) {
        String attributeCode = entry.getKey();
        AttributeValModel attributeVal = entry.getValue();
        // attributes 中的属性如果不存在则需要先添加
        AttributeExtend attributeExtend = attributeExtendInnerService
            .addAttributeExtendIfNonExistent(attributeCode, attributeVal);
        String value = attributeVal != null ? attributeVal.getValue() : null;
        // 判断如果是System定义的Code,则需要通过其他方式去更新
        AttributeDefine sysGrpAttributeDefine =
            AttributeDefine.getSystemDefineGroupAttribute(attributeCode);
        if (sysGrpAttributeDefine != null) {
          // 系统预定义的扩展属性. 比如Grp表中定义好的属性.
          if (sysGrpAttributeDefine.isWritable()) {
            try {
              extendValInnerService.addOrUpdateSystemDefineAttribute(grpId,
                  sysGrpAttributeDefine.getDefineTable().getIdentityFieldName(),
                  sysGrpAttributeDefine.getDefineTable().getTableName(),
                  sysGrpAttributeDefine.getFieldName(),
                  sysGrpAttributeDefine.getTypeTranslator().toDatabaseType(value),
                  sysGrpAttributeDefine.isUniqueField(),
                  sysGrpAttributeDefine.getDefineTable().isUpdateAttributeCheck());
            } catch (InvalidPropertyValueException e) {
              throw new AppException(InfoName.VALIDATE_FAIL, UniBundle
                  .getMsg("data.filter.extend.value.invalid", e.getInvalidValue(), e.getType()));
            }
            // 同时更新在扩展属性表中的属性
            addOrUpdate(grpId, attributeExtend.getId(), value);
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
    groupDataFilter.addFieldCheck(FilterType.EXIST, FieldType.FIELD_TYPE_ID, groupId);
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
      groupExtendValInnerService.update(groupId, extendId, value);
      record = groupExtendValInnerService.queryByGrpIdAndExtendId(groupId, extendId);
    }
    return BeanConverter.convert(record, GrpExtendValDto.class);
  }
}
