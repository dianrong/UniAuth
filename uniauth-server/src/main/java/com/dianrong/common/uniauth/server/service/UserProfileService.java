package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.support.ProfileSupport;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户Profile操作的service实现.
 */
@Service
public class UserProfileService extends TenancyBasedService {

  @Autowired
  private ProfileService profileService;
  
  /**
   * 根据UniauthId和profileId获取用户的属性.
   * @param uniauthId uniauthId
   * @param profileId profileId
   * @return 用户的属性集合
   */
  public Map<String, Object> getUserProfile(Long uniauthId, Long profileId) {
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
    return null;
  }

  public Response<Map<String, Object>> getUserProfileByIdentity(String identity, Long profileId,
      Long identityType) {
    return null;
  }

  public Response<Map<String, Object>> addOrUpdateUserProfile(Long userId, Long profileId,
      ProfileParam param) {
    return null;
  }
}
