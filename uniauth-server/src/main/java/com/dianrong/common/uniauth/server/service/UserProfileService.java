package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.UserIdentityType;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.cache.ProfileCache;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.inner.ProfileDefinitionAttributeInnerService;
import com.dianrong.common.uniauth.server.service.inner.UserProfileInnerService;
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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户Profile操作的service实现.
 */
@Slf4j
@Service
public class UserProfileService extends TenancyBasedService {

  @Autowired
  private ProfileService profileService;

  @Autowired
  private UserExtendValService userExtendValService;

  @Autowired
  private ProfileCache profileCache;

  @Autowired
  private UserService userService;

  @Autowired
  private UserProfileInnerService userProfileInnerService;

  @Autowired
  private ProfileDefinitionAttributeInnerService profileDefinitionAttributeInnerService;

  /**
   * 实现类似于getUserProfile.判定用户的身份是通过identity来判断.
   * 
   * @param identity 用户的身份识别标识. 比如:Email, Phone, Staff_no,Ldap_dn, User_guid等.
   * @param profileId Profile定义的Id.
   * @param tenancyId 租户Id.
   * @param identityType 登陆类型,对应于枚举:UserIdentityType
   * @param time 如果该参数传值,则代表获取对应时间点用户的Profile.
   * @return 用户的属性集合.
   */
  public Map<String, Object> getUserProfileByIdentity(String identity, Long profileId,
      Long tenancyId, UserIdentityType identityType, Long time) {
    CheckEmpty.checkEmpty(profileId, "profileId");
    CheckEmpty.checkEmpty(identity, "identity");
    Long uniauthId = StringUtil.translateStringToLong(identity);
    if (identityType == null && uniauthId != null) {
      log.debug("get user profile by uniauthId.UniauthId:{}", uniauthId);
      return getUserProfile(uniauthId, profileId, time);
    }
    log.debug("get user profile by identity.identity:{}, identityType:{}, tenancyId:{}", uniauthId,
        identityType, tenancyId);
    User user = userService.getUserByIdentity(identity, tenancyId, identityType);
    if (user == null) {
      throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg(
          "common.profile.parameter.user.identity.not.found", identityType.getType(), identity));
    }
    uniauthId = user.getId();
    log.debug("find one user by {} = {}", identityType.getType(), identity);
    return getUserProfile(uniauthId, profileId, time);
  }

  /**
   * 根据UniauthId和profileId获取用户的属性.
   * 
   * @param uniauthId uniauthId
   * @param profileId profileId
   * @return 用户的属性集合
   */
  public Map<String, Object> getUserProfile(Long uniauthId, Long profileId) {
    return getUserProfile(uniauthId, profileId, null);
  }
  
  /**
   * 根据UniauthId和profileId获取用户的属性.
   * 
   * @param time 过去的某个时间点,用于查询历史Profile.
   * @return 用户的属性集合
   */
  public Map<String, Object> getUserProfile(Long uniauthId, Long profileId, Long time) {
    CheckEmpty.checkEmpty(uniauthId, "uniauthId");
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
        userExtendValService.queryAttributeVal(uniauthId, extendIds, time);
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
   * 更新用户的扩展属性.并根据ProfileId返回更新后的Profile.
   */
  public Map<String, Object> addOrUpdateUserProfile(Long uniauthId, Long profileId,
      Map<String, AttributeValModel> attributes) {
    userProfileInnerService.addOrUpdateUserProfile(uniauthId, attributes);
    return getUserProfile(uniauthId, profileId);
  }
}
