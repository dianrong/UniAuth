package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttributeExample;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionAttributeMapper;
import com.dianrong.common.uniauth.server.util.CheckEmpty;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileDefinitionAttributeService extends TenancyBasedService {

  @Autowired
  private ProfileDefinitionAttributeMapper profileDefinitionAttributeMapper;
  
  /**
   * 批量添加Profile与扩展属性的关联关系.
   */
  public void batchInsert(List<ProfileDefinitionAttribute> infoes) {
    if (ObjectUtil.collectionIsEmptyOrNull(infoes)) {
      log.debug("empty list to insert, just ignore!");
      return;
    }
    profileDefinitionAttributeMapper.batchInsert(infoes);
  }
  
  /**
   * 根据条件删除记录.
   * @param profileId Profile的id, 不能为空.
   * @param attributeId 扩展属性的id, 可为空.
   */
  public void delete(Long profileId, Long attributeId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    ProfileDefinitionAttributeExample example = new ProfileDefinitionAttributeExample();
    ProfileDefinitionAttributeExample.Criteria criteria =  example.createCriteria();
    criteria.andProfileIdEqualTo(profileId);
    if (attributeId != null) {
      criteria.andAttributeIdEqualTo(attributeId);
    }
    profileDefinitionAttributeMapper.deleteByExample(example);
  }
  
  /**
   * 根据条件查询Profile和Attribute的关联关系信息.
   * @param profileId Profile的id, 不能为空.
   * @param attributeId 扩展属性的id, 可为空.
   */
  public List<ProfileDefinitionAttribute> query(Long profileId, Long attributeId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    ProfileDefinitionAttributeExample example = new ProfileDefinitionAttributeExample();
    ProfileDefinitionAttributeExample.Criteria criteria =  example.createCriteria();
    criteria.andProfileIdEqualTo(profileId);
    if (attributeId != null) {
      criteria.andAttributeIdEqualTo(attributeId);
    }
    return profileDefinitionAttributeMapper.selectByExample(example);
  }
}
