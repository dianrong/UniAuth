package com.dianrong.common.uniauth.server.service.common;

import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.PermType;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.service.cache.CommonCache;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提供通用服务的一个Service.
 */
@Service
@Slf4j
public class CommonService {

  @Autowired
  private CommonCache commonCache;

  /**
   * 获取PermTypeMap.
   */
  public Map<Integer, PermType> getPermTypeMap() {
    Map<String, PermType> permMap = commonCache.getPermTypeMap();
    Map<Integer, PermType> permTypeMap = Maps.newHashMap();
    for (Entry<String, PermType> entry : permMap.entrySet()) {
      String key = entry.getKey();
      Integer integerKey = StringUtil.tryToTranslateStrToInt(key);
      if (integerKey != null) {
        permTypeMap.put(integerKey, entry.getValue());
      } else {
        log.error("{} is not valid a permission id", key);
      }
    }
    return permTypeMap;
  }

  /**
   * 获取RoleCodeMap.
   */
  public Map<Integer, RoleCode> getRoleCodeMap() {
    Map<String, RoleCode> roleMap = commonCache.getRoleCodeMap();
    Map<Integer, RoleCode> roleCodeMap = Maps.newHashMap();
    for (Entry<String, RoleCode> entry : roleMap.entrySet()) {
      String key = entry.getKey();
      Integer integerKey = StringUtil.tryToTranslateStrToInt(key);
      if (integerKey != null) {
        roleCodeMap.put(integerKey, entry.getValue());
      } else {
        log.error("{} is not valid a role id", key);
      }
    }
    return roleCodeMap;
  }

  /**
   * 根据RoleCode获取RoleCode的Id.
   */
  public Integer getRoleCodeId(String roleCode) {
    Map<String, RoleCode> roleCodeMap = commonCache.getRoleCodeMap();
    for (Entry<String, RoleCode> entry : roleCodeMap.entrySet()) {
      if (entry.getValue().getCode().equalsIgnoreCase(roleCode)) {
        return StringUtil.tryToTranslateStrToInt(entry.getKey());
      }
    }
    return null;
  }

  /**
   * 根据permType获取权限类型的Id.
   */
  public Integer getPermTypeId(String permType) {
    Map<String, PermType> permTypeMap = commonCache.getPermTypeMap();
    for (Entry<String, PermType> entry : permTypeMap.entrySet()) {
      if (entry.getValue().getType().equalsIgnoreCase(permType)) {
        return StringUtil.tryToTranslateStrToInt(entry.getKey());
      }
    }
    return null;
  }
}
