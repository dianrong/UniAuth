package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinition;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService extends TenancyBasedService {

  /**
   * 进行角色数据过滤的filter.
   */
  @Resource(name = "profileDefinitionDataFilter")
  private DataFilter dataFilter;

  @Autowired
  private ProfileDefinitionMapper profileDefinitionMapper;

  @Autowired
  private AttributeExtendService attributeExtendService;
  
  @Autowired
  private ProfileDefinitionAttributeService profileDefinitionAttributeService;
  
  @Autowired
  private ProfileDefinitionPathService profileDefinitionPathService;

  @Transactional
  public ProfileDefinitionDto addNewProfileDefinition(String name, String code, String description,
      Map<String, String> attributes, Set<Long> descendantProfileIds) {
    CheckEmpty.checkEmpty(code, "profile definition code");
    // code不能重复
    dataFilter.addFieldCheck(FilterType.EXSIT_DATA, FieldType.FIELD_TYPE_CODE, code);
    // 子profile_definition必须存在
    if (!ObjectUtil.collectionIsEmptyOrNull(descendantProfileIds)) {
      for (Long pdid : descendantProfileIds) {
        dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, pdid);
      }
    }
    
    // 添加profile_definition
    ProfileDefinition profileDefinition = new ProfileDefinition();
    profileDefinition.setCode(code);
    profileDefinition.setName(name);
    profileDefinition.setDescription(description);
    profileDefinition.setTenancyId(tenancyService.getTenancyIdWithCheck());
    profileDefinitionMapper.insertSelective(profileDefinition);

    List<AttributeExtend> attributeExtends = Lists.newArrayList();
    // 添加扩展属性
    if (attributes != null && !attributes.isEmpty()) {
      for (Map.Entry<String, String> entry : attributes.entrySet()) {
        AttributeExtend attributeExtend = attributeExtendService.addAttributeExtendIfNonExistent(entry.getKey(),
            null, null, entry.getValue());
        attributeExtends.add(attributeExtend);
      }
    }
    // 添加扩展属性和profile_definition的关联关系, 批量添加处理
    List<ProfileDefinitionAttribute> definitionAttributes = Lists.newArrayList();
    profileDefinitionAttributeService.batchInsert(definitionAttributes);
    
    // 添加profile_definition和子profile_definition的关联关系
    if (!ObjectUtil.collectionIsEmptyOrNull(descendantProfileIds)) {
      profileDefinitionPathService.relateProfileAndSubProfile(profileDefinition.getId(), descendantProfileIds);
    }
    return BeanConverter.convert(profileDefinition, attributes, descendantProfileIds);
  }
}
