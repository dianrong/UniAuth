package com.dianrong.common.uniauth.common.customer.basicauth.handler;

import com.dianrong.common.uniauth.common.bean.dto.AllDomainPermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.customer.basicauth.mode.PermissionType;
import com.dianrong.common.uniauth.common.customer.basicauth.util.AuthorityStringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Created by denghb on 6/20/17.
 */
@Slf4j
public class PermissionHandler implements ModeHandler {

  @Override
  public ArrayList<SimpleGrantedAuthority> handle(UserDetailDto userDetailDto, String domainCode,
      PermissionType permissionType) {
    ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorityArrayList = new ArrayList<>();

    List<DomainDto> domainList = userDetailDto.getDomainList();
    if (domainList != null) {
      for (DomainDto domainDto : domainList) {
        String code = domainDto.getCode();
        // 域名CODE存在
        if (!StringUtils.isBlank(code) && code.equals(domainCode)) {
          List<RoleDto> roleList = domainDto.getRoleList();
          // 通过PERMISSION方式获取权限
          getByPermission(roleList, simpleGrantedAuthorityArrayList);
          break;
        } else {
          log.warn("域名CODE不存在");
        }
      }
    } else {
      log.warn("域名不存在");
    }

    AllDomainPermissionDto dto = userDetailDto.getAllDomainPermissionDto();
    if (dto != null) {
      List<RoleDto> roleList = dto.getRoleList();
      // 通过PERMISSION方式获取权限
      getByPermission(roleList, simpleGrantedAuthorityArrayList);
    } else {
      log.info("公有域不存在");
    }
    return simpleGrantedAuthorityArrayList;
  }

  private void getByPermission(List<RoleDto> roleList,
      ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorityArrayList) {

    ArrayList<Set<String>> arrayList = new ArrayList<>();
    for (RoleDto roleDto : roleList) {
      arrayList.addAll(roleDto.getPermMap().values());
    }

    for (Set<String> set : arrayList) {
      for (String permType : set) {
        simpleGrantedAuthorityArrayList
            .add(new SimpleGrantedAuthority(AuthorityStringUtil.roleAuthrorityFormat(permType)));
      }
    }
  }
}
