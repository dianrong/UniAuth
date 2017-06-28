package com.dianrong.common.uniauth.server.service.support;

import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.dto.SimpleProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Profile处理相关的一些辅助方法.
 */
public final class ProfileSupport {

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
    if (pdDto == null){
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
