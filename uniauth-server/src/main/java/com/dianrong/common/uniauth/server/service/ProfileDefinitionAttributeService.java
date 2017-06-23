package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionAttributeMapper;

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
}
