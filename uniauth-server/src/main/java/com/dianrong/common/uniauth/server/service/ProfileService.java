package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.dto.SimpleProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinition;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.cache.ProfileCache;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.inner.AttributeExtendInnerService;
import com.dianrong.common.uniauth.server.service.inner.ProfileDefinitionAttributeInnerService;
import com.dianrong.common.uniauth.server.service.inner.ProfileDefinitionPathInnerService;
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

  @Resource(name = "profileDefinitionDataFilter")
  private DataFilter dataFilter;

  @Autowired
  private ProfileDefinitionMapper profileDefinitionMapper;

  @Autowired
  private AttributeExtendInnerService attributeExtendInnerService;

  @Autowired
  private ProfileDefinitionAttributeInnerService profileDefinitionAttributeInnerService;

  @Autowired
  private ProfileDefinitionPathInnerService profileDefinitionPathInnerService;

  @Autowired
  private ProfileCache profileCache;

  /**
   * 添加一个新的Profile.
   * @param name 名称
   * @param code 编码,不能重复.
   * @param description 描述.
   * @param attributes 对应的所有属性的Map.
   * @param descendantProfileIds 子Profile Id 集合.
   * @return 生成的Profile对象.
   */
  @Transactional
  public ProfileDefinitionDto addNewProfileDefinition(String name, String code, String description,
      Map<String, AttributeValModel> attributes, Set<Long> descendantProfileIds) {
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
      for (Map.Entry<String, AttributeValModel> entry : attributes.entrySet()) {
        AttributeExtend attributeExtend = attributeExtendInnerService
            .addAttributeExtendIfNonExistent(entry.getKey(), entry.getValue());
        attributeExtends.add(attributeExtend);
      }
    }
    // 添加扩展属性和profile_definition的关联关系, 批量添加处理
    List<ProfileDefinitionAttribute> definitionAttributes =
        BeanConverter.convert(profileDefinition.getId(), attributeExtends);
    profileDefinitionAttributeInnerService.batchInsert(definitionAttributes);

    // 添加profile_definition和子profile_definition的关联关系
    if (!ObjectUtil.collectionIsEmptyOrNull(descendantProfileIds)) {
      profileDefinitionPathInnerService.addNewProfilePath(profileDefinition.getId(),
          descendantProfileIds);
    }
    SimpleProfileDefinitionDto dpdDto =
        profileDefinitionPathInnerService.getProfilePathTree(profileDefinition.getId());
    return BeanConverter.convert(profileDefinition, BeanConverter.convertToDto(attributes),
        descendantProfileIds, dpdDto == null ? null : dpdDto.getSubProfiles());
  }

  /**
   * 根据ProfileId获取一个Profile的定义.
   * 
   * @param id Profile Id
   */
  public ProfileDefinitionDto getProfileDefinition(Long id) {
    CheckEmpty.checkEmpty(id, "profile definition id");
    return profileCache.getProfileDefinition(id);
  }

  /**
   * 更新一个Profile,并返回更新之后最新Profile.
   */
  @Transactional
  public ProfileDefinitionDto updateProfileDefinition(Long id, String name, String code,
      String description, Map<String, AttributeValModel> attributes,
      Set<Long> descendantProfileIds) {
    CheckEmpty.checkEmpty(id, "profile definition id");
    profileCache.updateProfileDefinition(id,name, code,
        description, attributes, descendantProfileIds);
    // 返回最新结果.
    return getProfileDefinition(id);
  }

  /**
   * 扩展Profile的扩展属性和子Profile.
   */
  public ProfileDefinitionDto extendProfileDefinition(Long id,
      Map<String, AttributeValModel> attributes, Set<Long> descendantProfileIds) {
    CheckEmpty.checkEmpty(id, "profile definition id");
    profileCache.extendProfileDefinition(id, tenancyService.getTenancyIdWithCheck(), attributes,
        descendantProfileIds);
    // 返回最新结果.
    return getProfileDefinition(id);
  }
}
