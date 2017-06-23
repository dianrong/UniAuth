package com.dianrong.common.uniauth.common.customer.basicauth.handler;

import com.dianrong.common.uniauth.common.bean.dto.AllDomainPermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.customer.basicauth.util.AuthorityStringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Created by denghb on 6/20/17.
 */
@Slf4j
public class PermissionTypeHandler implements ModeHandler {

  @Override
  public ArrayList<SimpleGrantedAuthority> handle(UserDetailDto userDetailDto, String domainDefine,
      String permissionType) {
    ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorityArrayList = new ArrayList<>();

    List<DomainDto> domainList = userDetailDto.getDomainList();
    if (domainList != null) {
      for (int i = 0; i < domainList.size(); i++) {
        DomainDto domainDto = domainList.get(i);
        String code = domainDto.getCode();
        // 域名CODE存在
        if (code != null && code.length() != 0 && code.equals(domainDefine)) {
          List<RoleDto> roleList = domainDto.getRoleList();
          // 通过PERMISSION_TYPE方式获取权限
          getByPermissionType(roleList, simpleGrantedAuthorityArrayList, permissionType);
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
      // 通过PERMISSION_TYPE方式获取权限
      getByPermissionType(roleList, simpleGrantedAuthorityArrayList, permissionType);
    } else {
      log.info("公有域不存在");
    }
    return simpleGrantedAuthorityArrayList;
  }

  private void getByPermissionType(List<RoleDto> roleList,
      ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorityArrayList, String permissionType) {
    ArrayList<Set<String>> arrayList = new ArrayList<>();
    for (int i = 0; i < roleList.size(); i++) {
      Set<String> permTypeSet = roleList.get(i).getPermMap().get(permissionType);
      if (permTypeSet == null || permTypeSet.isEmpty()) {
        log.info("没有找到PERMISSION_TYPE是" + permissionType + "对应的数据");
      } else {
        for (String permType : permTypeSet) {
          simpleGrantedAuthorityArrayList
              .add(new SimpleGrantedAuthority(AuthorityStringUtil.roleAuthrorityFormat(permType)));
        }
      }
    }
  }
}
