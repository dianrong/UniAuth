package com.dianrong.common.uniauth.server.service.support;

import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.dto.SimpleProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Profile处理相关的一些辅助方法.
 */
public final class ProfileSupport {

  /**
   * 拼装出Profile的所有属性值.
   * 
   * @param profileDefinition Profile的根Profile定义,以及与子Profile的关联关系等信息.
   * @param extendValMap 属性编码与属性值的关联关系,不能为空.
   * @param queryProfileDefinition
   *        一个QueryProfileDefinition实现,用于遍历的根据ProfileDefinitionId获取Profile定义的详细信息. 不能为空.
   */
  public static Map<String, Object> getProfileAttributes(ProfileDefinitionDto profileDefinition,
      Map<String, ExtendVal> extendValMap, QueryProfileDefinition queryProfileDefinition) {
    // 扩展属性值Map
    Map<String, Object> profileAttributes = Maps.newHashMap();
    Map<String, AttributeExtendDto> profileAttributeDefine = profileDefinition.getAttributes();
    // 处理根Profile的属性值
    if (profileAttributeDefine != null && !profileAttributeDefine.isEmpty()) {
      Map<String, String> currentProfileAttributes = Maps.newHashMap();
      for (Entry<String, AttributeExtendDto> entry : profileAttributeDefine.entrySet()) {
        String attributeCode = entry.getKey();
        ExtendVal extendVal = extendValMap.get(attributeCode);
        if (extendVal != null) {
          currentProfileAttributes.put(attributeCode, extendVal.getValue());
        } else {
          currentProfileAttributes.put(attributeCode, null);
        }
      }
      profileAttributes.put(ProfileDefinitionDto.ATTRIBUTES, currentProfileAttributes);
    }

    // 处理子Profile
    Set<SimpleProfileDefinitionDto> subProfiles = profileDefinition.getSubProfiles();
    if (!ObjectUtil.collectionIsEmptyOrNull(subProfiles)) {
      Map<String, Object> subProfileDefine = Maps.newHashMap();
      for (SimpleProfileDefinitionDto subProfile : subProfiles) {
        Long profileId = subProfile.getId();
        ProfileDefinitionDto subProfileDefinition = 
            queryProfileDefinition.querySimpleProfileDefinition(profileId);
        subProfileDefinition.setSubProfiles(subProfile.getSubProfiles());
        Map<String, Object> subProfileAttributes =
            getProfileAttributes(subProfileDefinition, extendValMap, queryProfileDefinition);
        subProfileDefine.put(subProfileDefinition.getCode(), subProfileAttributes);
      }
      profileAttributes.put(ProfileDefinitionDto.SUB_PROFILE, subProfileDefine);
    }
    return profileAttributes;
  }

  /**
   * 获取ProfileDefinitionDto树形结构中所有的ProfileId的集合.
   */
  public static Set<Long> collectSubProfileId(ProfileDefinitionDto pdDto) {
    Set<Long> profileIdSet = Sets.newHashSet();
    collectSubProfileId(pdDto, profileIdSet);
    return profileIdSet;
  }

  /**
   * 获取ProfileDefinitionDto树形结构中所有的ProfileId的集合.
   */
  public static void collectSubProfileId(ProfileDefinitionDto pdDto, Set<Long> profileIdSet) {
    Assert.notNull(profileIdSet);
    if (pdDto == null) {
      return;
    }
    profileIdSet.add(pdDto.getId());
    if (ObjectUtil.collectionIsEmptyOrNull(pdDto.getSubProfiles())) {
      return;
    }
    for (SimpleProfileDefinitionDto spdp : pdDto.getSubProfiles()) {
      collectSubProfileId(spdp, profileIdSet);
    }
    return;
  }


  /**
   * 获取SimpleProfileDefinitionDto树形结构中所有的ProfileId的集合.
   */
  public static Set<Long> collectSubProfileId(SimpleProfileDefinitionDto spdp) {
    Set<Long> profileIdSet = Sets.newHashSet();
    collectSubProfileId(spdp, profileIdSet);
    return profileIdSet;
  }

  /**
   * 获取SimpleProfileDefinitionDto树形结构中所有的ProfileId的集合.
   */
  public static void collectSubProfileId(SimpleProfileDefinitionDto spdp, Set<Long> profileIdSet) {
    Assert.notNull(profileIdSet);
    if (spdp == null) {
      return;
    }
    profileIdSet.add(spdp.getId());
    Set<SimpleProfileDefinitionDto> subProfiles = spdp.getSubProfiles();
    if (ObjectUtil.collectionIsEmptyOrNull(subProfiles)) {
      return;
    }
    for (SimpleProfileDefinitionDto tspdp : subProfiles) {
      collectSubProfileId(tspdp, profileIdSet);
    }
  }
}
