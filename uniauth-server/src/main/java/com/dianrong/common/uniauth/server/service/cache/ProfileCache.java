package com.dianrong.common.uniauth.server.service.cache;

import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.dto.SimpleProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinition;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionExample;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.service.AttributeExtendService;
import com.dianrong.common.uniauth.server.service.ProfileDefinitionAttributeService;
import com.dianrong.common.uniauth.server.service.ProfileDefinitionPathService;
import com.dianrong.common.uniauth.server.service.support.ProcessListQuery;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Profile的Cache操作.
 * 
 * @author wanglin
 *
 */
@Slf4j
@Component
@CacheConfig(cacheNames = {"profile"})
public class ProfileCache {

  /**
   * 进行角色数据过滤的filter.
   */
  @Resource(name = "profileDefinitionDataFilter")
  private DataFilter dataFilter;

  @Autowired
  private ProfileDefinitionMapper profileDefinitionMapper;

  @Autowired
  private ProfileDefinitionPathService profileDefinitionPathService;

  @Autowired
  private AttributeExtendService attributeExtendService;

  @Autowired
  private ProfileDefinitionAttributeService profileDefinitionAttributeService;

  /**
   * 只获取指定Profile的定义,包括其关联的属性集合,但是不包括子Profile的相关信息.
   */
  @Cacheable(key = "#tenancyId + :simple':' + #id")
  public ProfileDefinitionDto getSimpleProfileDefinition(Long id, Long tenancyId) {
    ProfileDefinitionExample queryExample = new ProfileDefinitionExample();
    ProfileDefinitionExample.Criteria criteria = queryExample.createCriteria();
    criteria.andIdEqualTo(id).andTenancyIdEqualTo(tenancyId);
    List<ProfileDefinition> pdList = profileDefinitionMapper.selectByExample(queryExample);
    if (ObjectUtil.collectionIsEmptyOrNull(pdList)) {
      log.debug("get profile definition by primary key {}, but not found one!", id);
      return null;
    }
    ProfileDefinition rootProfileDefinition = pdList.get(0);
    // get profile attributes
    List<AttributeExtend> attributeExtends = attributeExtendService.getAttributesByProfileId(id);
    Map<String, String> attributes = new LinkedHashMap<>();
    if (!ObjectUtil.collectionIsEmptyOrNull(attributeExtends)) {
      for (AttributeExtend ae : attributeExtends) {
        attributes.put(ae.getCode(), ae.getDescription());
      }
    }
    return BeanConverter.convert(rootProfileDefinition, attributes, null, null);
  }

  /**
   * 根据Id获取Profile的定义.
   */
  @Cacheable(key = "#tenancyId + ':' + #id")
  public ProfileDefinitionDto getProfileDefinition(Long id, Long tenancyId) {
    ProfileDefinitionExample queryExample = new ProfileDefinitionExample();
    ProfileDefinitionExample.Criteria criteria = queryExample.createCriteria();
    criteria.andIdEqualTo(id).andTenancyIdEqualTo(tenancyId);
    List<ProfileDefinition> pdList = profileDefinitionMapper.selectByExample(queryExample);
    if (ObjectUtil.collectionIsEmptyOrNull(pdList)) {
      log.debug("get profile definition by primary key {}, but not found one!", id);
      return null;
    }
    ProfileDefinition rootProfileDefinition = pdList.get(0);
    // get profile attributes
    List<AttributeExtend> attributeExtends = attributeExtendService.getAttributesByProfileId(id);
    Map<String, String> attributes = new LinkedHashMap<>();
    if (!ObjectUtil.collectionIsEmptyOrNull(attributeExtends)) {
      for (AttributeExtend ae : attributeExtends) {
        attributes.put(ae.getCode(), ae.getDescription());
      }
    }
    SimpleProfileDefinitionDto dpdDto = profileDefinitionPathService.getProfilePathTree(id);
    return BeanConverter.convert(rootProfileDefinition, attributes, null,
        dpdDto == null ? null : dpdDto.getSubProfiles());
  }

  /**
   * 更新一个Profile.
   */
  @Transactional
  @Caching(evict = {@CacheEvict(key = "#tenancyId + ':' + #id"),
      @CacheEvict(key = "#tenancyId + :simple':' + #id")})
  public void updateProfileDefinition(Long id, Long tenancyId, String name, String code,
      String description, Map<String, String> attributes, Set<Long> descendantProfileIds) {
    // Id 必须要存在.
    dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, id);
    if (!StringUtils.isBlank(code)) {
      dataFilter.updateFieldCheck(StringUtil.translateLongToInteger(id), FieldType.FIELD_TYPE_CODE,
          code);
    }

    // 更新Profile和子Profile的关联关系
    profileDefinitionPathService.updateSubProfilePath(id, descendantProfileIds);

    // 更新Profile Definition 自身基础属性
    ProfileDefinition record = new ProfileDefinition();
    record.setId(id);
    record.setCode(code);
    record.setName(name);
    profileDefinitionMapper.updateByPrimaryKeySelective(record);

    List<AttributeExtend> attributeExtends = Lists.newArrayList();
    // 关联Profile_definition和扩展属性
    if (attributes != null && !attributes.isEmpty()) {
      for (Map.Entry<String, String> entry : attributes.entrySet()) {
        AttributeExtend attributeExtend = attributeExtendService
            .addAttributeExtendIfNonExistent(entry.getKey(), null, null, entry.getValue());
        attributeExtends.add(attributeExtend);
      }
    }

    List<ProfileDefinitionAttribute> destDefinitionAttributes =
        BeanConverter.convert(id, attributeExtends);
    // 获取所有的关联关系
    List<ProfileDefinitionAttribute> existProfileDefinitionAttributes =
        profileDefinitionAttributeService.query(id, null);

    // 添加的扩展属性列表
    List<ProfileDefinitionAttribute> listToInsert =
        (new ProcessListQuery<ProfileDefinitionAttribute, Long>() {
          @Override
          public Long getId(ProfileDefinitionAttribute o) {
            return o.getExtendId();
          }
        }).getProcessList(existProfileDefinitionAttributes, destDefinitionAttributes, true);
    profileDefinitionAttributeService.batchInsert(listToInsert);

    // 删除的扩展属性列表
    List<ProfileDefinitionAttribute> listToDelete =
        (new ProcessListQuery<ProfileDefinitionAttribute, Long>() {
          @Override
          public Long getId(ProfileDefinitionAttribute o) {
            return o.getExtendId();
          }
        }).getProcessList(existProfileDefinitionAttributes, destDefinitionAttributes, false);
    for (ProfileDefinitionAttribute delItem : listToDelete) {
      profileDefinitionAttributeService.delete(id, delItem.getExtendId());
    }
  }

  /**
   * 扩展Profile的扩展属性和子Profile.
   */
  @Transactional
  @Caching(evict = {@CacheEvict(key = "#tenancyId + ':' + #id"),
      @CacheEvict(key = "#tenancyId + :simple':' + #id")})
  public void extendProfileDefinition(Long id, Long tenancyId, Map<String, String> attributes,
      Set<Long> descendantProfileIds) {
    dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, id);
    // 处理Profile的关联关系.
    profileDefinitionPathService.extendSubProfilePath(id, descendantProfileIds);
    // 处理扩展属性
    profileDefinitionAttributeService.extendProfileAttributes(id, attributes);
  }
}
