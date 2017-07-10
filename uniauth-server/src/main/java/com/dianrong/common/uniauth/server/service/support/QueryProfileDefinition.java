package com.dianrong.common.uniauth.server.service.support;

import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;

public interface QueryProfileDefinition {
  
  /**
   * 定义的一个便利接口,用于根据id和租户id获取一个Profile的定义.
   * @param id Profile的id,不能为空.
   */
  ProfileDefinitionDto querySimpleProfileDefinition(Long id);
  
}
