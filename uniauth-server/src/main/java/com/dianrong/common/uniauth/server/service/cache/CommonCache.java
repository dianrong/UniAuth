package com.dianrong.common.uniauth.server.service.cache;

import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.server.data.entity.PermType;
import com.dianrong.common.uniauth.server.data.entity.PermTypeExample;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.RoleCodeExample;
import com.dianrong.common.uniauth.server.data.mapper.PermTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = {"commonService"})
public class CommonCache {

  @Autowired
  private PermTypeMapper permTypeMapper;
  @Autowired
  private RoleCodeMapper roleCodeMapper;

  /**
   * 从缓存中获取permType信息.
   */
  @Cacheable(key = "'permType'")
  public Map<String, PermType> getPermTypeMap() {
    Map<String, PermType> permTypeMap = new HashMap<>();
    PermTypeExample example = new PermTypeExample();
    List<PermType> permTypeList = permTypeMapper.selectByExample(example);
    if (permTypeList != null && !permTypeList.isEmpty()) {
      for (PermType permType : permTypeList) {
        permTypeMap.put(String.valueOf(permType.getId()), permType);
      }
    }
    return permTypeMap;
  }

  /**
   * 从缓存中获取roleCode信息.
   */
  @Cacheable(key = "'roleCode'")
  public Map<String, RoleCode> getRoleCodeMap() {
    Map<String, RoleCode> roleCodeMap = new HashMap<>();
    RoleCodeExample example = new RoleCodeExample();
    List<RoleCode> roleCodeList = roleCodeMapper.selectByExample(example);
    if (roleCodeList != null && !roleCodeList.isEmpty()) {
      for (RoleCode rc : roleCodeList) {
        roleCodeMap.put(String.valueOf(rc.getId()), rc);
      }
    }
    return roleCodeMap;
  }

  /**
   * 获取所有的角色编码信息.
   */
  @Cacheable(key = "'allRoleCode'")
  public List<RoleCodeDto> getAllRoleCodes() {
    RoleCodeExample example = new RoleCodeExample();
    List<RoleCode> roleCodeList = roleCodeMapper.selectByExample(example);
    List<RoleCodeDto> roleCodeDtoList = new ArrayList<RoleCodeDto>();
    if (roleCodeList != null) {
      for (RoleCode roleCode : roleCodeList) {
        roleCodeDtoList.add(BeanConverter.convert(roleCode));
      }
    }
    return roleCodeDtoList;
  }
}
