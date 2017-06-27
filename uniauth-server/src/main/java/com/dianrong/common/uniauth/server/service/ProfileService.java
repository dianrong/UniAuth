package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionPathDto;
import com.dianrong.common.uniauth.common.bean.dto.SimpleProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinition;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        AttributeExtend attributeExtend = attributeExtendService
            .addAttributeExtendIfNonExistent(entry.getKey(), null, null, entry.getValue());
        attributeExtends.add(attributeExtend);
      }
    }
    // 添加扩展属性和profile_definition的关联关系, 批量添加处理
    List<ProfileDefinitionAttribute> definitionAttributes =
        BeanConverter.convert(profileDefinition.getId(), attributeExtends);
    profileDefinitionAttributeService.batchInsert(definitionAttributes);

    // 添加profile_definition和子profile_definition的关联关系
    if (!ObjectUtil.collectionIsEmptyOrNull(descendantProfileIds)) {
      profileDefinitionPathService.relateProfileAndSubProfile(profileDefinition.getId(),
          descendantProfileIds);
    }
    SimpleProfileDefinitionDto dpdDto = getProfileTreeByProfileId(profileDefinition);
    return BeanConverter.convert(profileDefinition, attributes, descendantProfileIds,
        dpdDto == null ? null : dpdDto.getSubProfiles());
  }

  /**
   * 根据ProfileId获取一个Profile的定义.
   * 
   * @param id Profile Id
   */
  public ProfileDefinitionDto getProfileDefinition(Long id) {
    CheckEmpty.checkEmpty(id, "profile definition id");
    ProfileDefinition rootProfileDefinition = profileDefinitionMapper.selectByPrimaryKey(id);
    if (rootProfileDefinition == null) {
      log.debug("get profile definition by primary key {}, but not found one!", id);
      return null;
    }
    // get profile attributes
    List<AttributeExtend> attributeExtends = attributeExtendService.getAttributesByProfileId(id);
    Map<String, String> attributes = new LinkedHashMap<>();
    if (!ObjectUtil.collectionIsEmptyOrNull(attributeExtends)) {
      for (AttributeExtend ae : attributeExtends) {
        attributes.put(ae.getCode(), ae.getDescription());
      }
    }
    SimpleProfileDefinitionDto dpdDto = getProfileTreeByProfileId(rootProfileDefinition);
    return BeanConverter.convert(rootProfileDefinition, attributes, null,
        dpdDto == null ? null : dpdDto.getSubProfiles());
  }

  /**
   * 更新一个Profile,并返回更新之后最新Profile.
   */
  @Transactional
  public ProfileDefinitionDto updateProfileDefinition(Long id, String name, String code,
      String description, Map<String, String> attributes, Set<Long> descendantProfileIds) {
    CheckEmpty.checkEmpty(id, "profile definition id");
    // Id 必须要存在.
    dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, id);
    if (!StringUtils.isBlank(code)) {
      dataFilter.updateFieldCheck(StringUtil.translateLongToInteger(id), FieldType.FIELD_TYPE_CODE,
          code);
    }
    // 子profile_definition必须存在
    if (!ObjectUtil.collectionIsEmptyOrNull(descendantProfileIds)) {
      for (Long pdid : descendantProfileIds) {
        dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, pdid);
      }

      // 新设置的子Profile不能与当前Profile形成循环. Profile循环check
      if (profileDefinitionPathService.isSubProfile(descendantProfileIds, id)) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("profile.check.circle.profile.relate", id, descendantProfileIds));
      }
    }

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
          Long getId(ProfileDefinitionAttribute o) {
            return o.getAttributeId();
          }
        }).getProcessList(existProfileDefinitionAttributes, destDefinitionAttributes, true);
    profileDefinitionAttributeService.batchInsert(listToInsert);

    // 删除的扩展属性列表
    List<ProfileDefinitionAttribute> listToDelete =
        (new ProcessListQuery<ProfileDefinitionAttribute, Long>() {
          @Override
          Long getId(ProfileDefinitionAttribute o) {
            return o.getAttributeId();
          }
        }).getProcessList(existProfileDefinitionAttributes, destDefinitionAttributes, false);
    for (ProfileDefinitionAttribute delItem : listToDelete) {
      profileDefinitionAttributeService.delete(id, delItem.getAttributeId());
    }

    // 更新Profile和子Profile的关联关系

    return null;
  }

  /**
   * 扩展Profile的扩展属性和子Profile.
   */
  public ProfileDefinitionDto extendProfileDefinition(Long id, Map<String, String> attributes,
      Set<Long> descendantProfileIds) {
    return null;
  }

  /**
   * 获取各个Profile之间的关联关系.
   */
  private SimpleProfileDefinitionDto getProfileTreeByProfileId(
      ProfileDefinition rootProfileDefinition) {
    ProfileDefinitionPathDto pdpDto =
        profileDefinitionPathService.getProfilePathTree(rootProfileDefinition.getId());
    if (pdpDto == null) {
      return null;
    }
    SimpleProfileDefinitionDto rootPdpDto = new SimpleProfileDefinitionDto();
    rootPdpDto.setId(pdpDto.getDescendant());
    querySubProfile(rootPdpDto, pdpDto.getSubProfileInfo());
    return rootPdpDto;
  }

  /**
   * 获取子Profile与父Profile的关系信息.
   */
  private void querySubProfile(SimpleProfileDefinitionDto parantPdpDto,
      List<ProfileDefinitionPathDto> subProfileInfo) {
    if (ObjectUtil.collectionIsEmptyOrNull(subProfileInfo)) {
      return;
    }
    Set<SimpleProfileDefinitionDto> subProfiles = Sets.newHashSet();
    for (ProfileDefinitionPathDto tpdpDto : subProfileInfo) {
      SimpleProfileDefinitionDto simpleProfileDefinitionDto = new SimpleProfileDefinitionDto();
      simpleProfileDefinitionDto.setId(tpdpDto.getDescendant());
      querySubProfile(simpleProfileDefinitionDto, tpdpDto.getSubProfileInfo());
      subProfiles.add(simpleProfileDefinitionDto);
    }
    parantPdpDto.setSubProfiles(subProfiles);
    return;
  }

  /**
   * 根据存在的list和最终list获取需要处理的list. T 处理的对象类型. E 对象以某种类型为标识.
   */
  private abstract class ProcessListQuery<T, E> {
    /**
     * 根据传入的已存在的列表和最终的列表计算出需要的列表.
     * 
     * @param exsistList 已存在的列表.
     * @param destList 最终需要更新成的列表集合.
     * @param insertList true or false. 是获取插入的列表还是删除的列表.
     * @return 得到的结果.
     */
    List<T> getProcessList(final List<T> exsistList, final List<T> destList,
        boolean insertList) {
      List<T> innerExistList = exsistList == null ? new ArrayList<T>() : exsistList;
      List<T> innerDestList = destList == null ? new ArrayList<T>() : destList;

      List<T> listToMap;
      List<T> listToList;
      if (insertList) {
        listToMap = innerExistList;
        listToList = innerDestList;
      } else {
        listToList = innerExistList;
        listToMap = innerDestList;
      }
      Map<E, T> map = Maps.newHashMap();
      for (T t : listToMap) {
        map.put(getId(t), t);
      }
      List<T> result = Lists.newArrayList();
      for (T t : listToList) {
        E id = getId(t);
        if (map.get(id) == null) {
          result.add(t);
        }
      }
      return result;
    }

    /**
     * 获取对象的唯一标识.
     */
    abstract E getId(T o);
  }
}
