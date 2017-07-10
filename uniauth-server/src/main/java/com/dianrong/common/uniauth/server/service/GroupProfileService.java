package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.cache.ProfileCache;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.inner.GroupProfileInnerService;
import com.dianrong.common.uniauth.server.service.inner.ProfileDefinitionAttributeInnerService;
import com.dianrong.common.uniauth.server.service.support.ProfileSupport;
import com.dianrong.common.uniauth.server.service.support.QueryProfileDefinition;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 组Profile操作的service实现.
 */
@Service
public class GroupProfileService extends TenancyBasedService {

  @Autowired
  private ProfileService profileService;

  @Autowired
  private GroupExtendValService groupExtendValService;

  @Autowired
  private ProfileCache profileCache;

  @Autowired
  private GroupProfileInnerService groupProfileInnerService;

  @Autowired
  private ProfileDefinitionAttributeInnerService profileDefinitionAttributeInnerService;
  
  /**
   * 根据groupId和profileId获取组的属性集合.
   * 
   * @param groupId 组的Id
   * @param profileId profileId
   * @param time 指定查询历史Profile
   * @return 组的属性集合
   */
  public Map<String, Object> getGroupProfile(Integer groupId, Long profileId, Long time) {
    CheckEmpty.checkEmpty(groupId, "groupId");
    CheckEmpty.checkEmpty(profileId, "profileId");
    ProfileDefinitionDto pdDto = profileService.getProfileDefinition(profileId);
    if (pdDto == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.entity.not.found", "profileId", profileId));
    }
    Set<Long> profileIds = ProfileSupport.collectSubProfileId(pdDto);
    if (ObjectUtil.collectionIsEmptyOrNull(profileIds)) {
      return Collections.emptyMap();
    }

    // 获取profile对应的所有的extendId
    List<ProfileDefinitionAttribute> profileDefinitionAttributeList =
        profileDefinitionAttributeInnerService.queryByProfileIds(new ArrayList<Long>(profileIds));
    if (ObjectUtil.collectionIsEmptyOrNull(profileDefinitionAttributeList)) {
      return Collections.emptyMap();
    }
    List<Long> extendIds = Lists.newArrayList();
    for (ProfileDefinitionAttribute pda : profileDefinitionAttributeList) {
      extendIds.add(pda.getExtendId());
    }

    // 根据extend_attribute_id 获取所有的属性.
    Map<String, ExtendVal> extendValMap =
        groupExtendValService.queryAttributeVal(groupId, extendIds, time);
    if (extendValMap.isEmpty()) {
      return Collections.emptyMap();
    }
    return ProfileSupport.getProfileAttributes(pdDto, extendValMap, new QueryProfileDefinition() {
      @Override
      public ProfileDefinitionDto querySimpleProfileDefinition(Long id) {
        return profileCache.getSimpleProfileDefinition(id);
      }
    });
  }

  /**
   * 更新组的扩展属性.并根据ProfileId返回更新后的Profile.
   */
  public Map<String, Object> addOrUpdateGrpProfile(Integer grpId, Long profileId,
      Map<String, AttributeValModel> attributes) {
    groupProfileInnerService.addOrUpdateGrpProfile(grpId, attributes);
    return getGroupProfile(grpId, profileId, null);
  }
}
