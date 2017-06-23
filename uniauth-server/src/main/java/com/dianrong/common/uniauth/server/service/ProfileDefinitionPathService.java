package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionPathMapper;

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
}