package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionPathDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionPath;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionPathMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileDefinitionPathService extends TenancyBasedService {

  @Autowired
  private ProfileDefinitionPathMapper profileDefinitionPathMapper;

  @Transactional
  public void relateProfileAndSubProfile(Long ancestorId, Set<Long> descendantIds) {
    profileDefinitionPathMapper.relateProfileAndSubProfile(ancestorId, descendantIds);
  }

  /**
   * 判断一组的ProfileId是否有关联关系.
   */
  public boolean isRelated(Set<Long> profileIds) {
    if (ObjectUtil.collectionIsEmptyOrNull(profileIds)) {
      return false;
    }
    return profileDefinitionPathMapper.isRelated(profileIds) > 0;
  }
  
  /**
   * check某一Profile是否是一组Profile的某一个的子Profile.
   * @param profileIds 一组Profile 的Id集合
   * @param profileId  需要被check的ProfileId. 不能为空.
   * @return true. 1. profileIds 为空, 2. Profile Ids的所有子Profile不包括profileId.
   */
  public boolean isSubProfile(Set<Long> profileIds, Long profileId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    if (ObjectUtil.collectionIsEmptyOrNull(profileIds)) {
      return false;
    }
    List<Long> subProfileIds =  profileDefinitionPathMapper.querySubProfileId(profileIds);
    if (ObjectUtil.collectionIsEmptyOrNull(subProfileIds)) {
      return false;
    }
    return subProfileIds.contains(profileId);
  }
  
  /**
   * 重置Profile与子Profile之间的关联关系.
   * @param parentProfileId 指定的ProfileId,作为关系中的父Profile,不能为空.
   * @param subProfileIds 关联关系中的子ProfileId集合.
   */
  @Transactional
  public void resetProfileAndSubProfileRelation(Long parentProfileId, Set<Long> subProfileIds) {
    CheckEmpty.checkEmpty(parentProfileId, "profile definition id");
    // 删除旧的关联关系
    
    
    // 添加新的关联关系
  }

  /**
   * 根据Profile Id获取Profile的所有子profile的关联关系.
   */
  public ProfileDefinitionPathDto getProfilePathTree(Long profileId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    // 获取指定profileId对应的profile关联树结构
    List<ProfileDefinitionPath> profilePathTree =
        profileDefinitionPathMapper.getProfileTreeLinks(profileId);
    if (ObjectUtil.collectionIsEmptyOrNull(profilePathTree)) {
      return null;
    }
    // 构造profile的父子关系
    ProfileDefinitionPath rootProfileDefinitionPath = null;
    Map<Long, List<ProfileDefinitionPath>> pdpMap = Maps.newHashMap();
    for (ProfileDefinitionPath pdp: profilePathTree) {
      if (AppConstants.ZERO_BYTE.equals(pdp.getDeepth())) {
        rootProfileDefinitionPath = pdp;
      } else {
        List<ProfileDefinitionPath> destPdpList = pdpMap.get(pdp.getAncestor());
        if (destPdpList == null) {
          pdpMap.put(pdp.getAncestor(), new ArrayList<ProfileDefinitionPath>());
          destPdpList = pdpMap.get(pdp.getAncestor());
        }
        destPdpList.add(pdp);
      }
    }
    // check root ProfileDefinitionPath
    if (rootProfileDefinitionPath == null) {
      return null;
    }
    ProfileDefinitionPathDto rootPdp = BeanConverter.convert(rootProfileDefinitionPath);
    return rootPdp.setSubProfileInfo(getSubProfileInfo(profileId, pdpMap));
  }
  
  private List<ProfileDefinitionPathDto> getSubProfileInfo(Long profileId, Map<Long, List<ProfileDefinitionPath>> pdpMap) {
    List<ProfileDefinitionPathDto> result = Lists.newArrayList();
    List<ProfileDefinitionPath> pdpList = pdpMap.get(profileId);
    if (ObjectUtil.collectionIsEmptyOrNull(pdpList)) {
      return result;
    }
    for (ProfileDefinitionPath pdp:pdpList) {
      ProfileDefinitionPathDto pdpDto = BeanConverter.convert(pdp);
      pdpDto.setSubProfileInfo(getSubProfileInfo(pdp.getDescendant(), pdpMap));
    }
    return result;
  }
}
