package com.dianrong.common.uniauth.client.support;

import com.dianrong.common.uniauth.common.bean.dto.AllDomainPermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 在Uniauth集成系统的客户端中为权限的处理提供一些工具方法.
 * 
 * @author wanglin
 *
 */

@Slf4j
public final class PermissionUtil {
  /**
   * 将IPA权限合并到权限集合中.
   */
  public static final void mergeIPAPermission(AllDomainPermissionDto ipaPermissionDto,
      Collection<GrantedAuthority> authorities, Map<String, Set<String>> permMap,
      Map<String, Set<PermissionDto>> permDtoMap) {
    if (authorities == null || permDtoMap == null || permMap == null) {
      log.warn("mergeIPAPermission need parameter authorities, permMap, subPermMap not null");
      return;
    }
    if (ipaPermissionDto != null && ipaPermissionDto.getRoleList() != null
        && !ipaPermissionDto.getRoleList().isEmpty()) {
      mergeDomainPermission(ipaPermissionDto.getRoleList(), authorities, permMap, permDtoMap);
    }
  }

  /**
   * 合并权限.
   */
  public static final void mergeDomainPermission(List<RoleDto> roleDtoList,
      Collection<GrantedAuthority> authorities, Map<String, Set<String>> permMap,
      Map<String, Set<PermissionDto>> permDtoMap) {
    if (authorities == null || permDtoMap == null || permMap == null) {
      log.warn("mergeDomainPermission need parameter authorities, permMap, subPermMap not null");
      return;
    }
    if (roleDtoList != null && !roleDtoList.isEmpty()) {
      for (RoleDto roleDto : roleDtoList) {
        String roleCode = roleDto.getRoleCode();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleCode);
        authorities.add(authority);
        mergePermMap(permMap, roleDto.getPermMap());
        mergePermMap(permDtoMap, roleDto.getPermDtoMap());
      }
    }
  }

  /**
   * 合并权限的辅助方法.
   */
  private static <T> void mergePermMap(Map<String, Set<T>> permMap,
      Map<String, Set<T>> subPermMap) {
    Set<Entry<String, Set<T>>> subEntrySet = subPermMap.entrySet();
    Iterator<Entry<String, Set<T>>> subEntryIterator = subEntrySet.iterator();
    while (subEntryIterator.hasNext()) {
      Entry<String, Set<T>> subEntry = subEntryIterator.next();
      String permTypeName = subEntry.getKey();
      Set<T> permValueSet = subEntry.getValue();

      if (permMap.containsKey(permTypeName)) {
        permMap.get(permTypeName).addAll(permValueSet);
      } else {
        permMap.put(permTypeName, permValueSet);
      }
    }
  }
}
