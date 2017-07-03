package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.cache.ProfileCache;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.inner.GroupExtendValInnerService;
import com.dianrong.common.uniauth.server.service.inner.GroupProfileInnerService;
import com.dianrong.common.uniauth.server.service.support.ProfileSupport;
import com.dianrong.common.uniauth.server.service.support.QueryProfileDefinition;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;

import java.util.ArrayList;
import java.util.Collections;
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
  private GroupExtendValInnerService groupExtendValInnerService;

  @Autowired
  private ProfileCache profileCache;
  
  @Autowired
  private GroupProfileInnerService groupProfileInnerService;

  /**
   * 根据groupId和profileId获取组的属性集合.
   * 
   * @param groupId 组的Id
   * @param profileId profileId
   * @return 组的属性集合
   */
  public Map<String, Object> getGroupProfile(Integer groupId, Long profileId) {
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
    // 根据extend_attribute_id 获取所有的属性.
    Map<String, ExtendVal> extendValMap =
        groupExtendValInnerService.queryAttributeVal(groupId, new ArrayList<>(profileIds));
    if (extendValMap.isEmpty()) {
      return Collections.emptyMap();
    }
    final Long tenancyId = tenancyService.getTenancyIdWithCheck();
    return ProfileSupport.getProfileAttributes(pdDto, extendValMap, new QueryProfileDefinition() {
      @Override
      public ProfileDefinitionDto querySimpleProfileDefinition(Long id) {
        return profileCache.getSimpleProfileDefinition(id, tenancyId);
      }
    });
  }

  /**
   * 更新组的扩展属性.并根据ProfileId返回更新后的Profile.
   */
  public Map<String, Object> addOrUpdateGrpProfile(Integer grpId, Long profileId,
      Map<String, AttributeValModel> attributes) {
    groupProfileInnerService.addOrUpdateGrpProfile(grpId, attributes);
    return getGroupProfile(grpId, profileId);
  }
}
