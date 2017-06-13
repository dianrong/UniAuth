package com.dianrong.uniauth.ssclient.bean;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;

public class SSClientUserExtInfo extends UserExtInfo {

  private static final long serialVersionUID = -4910632638160710758L;

  private Set<Integer> domainIdSet;

  public SSClientUserExtInfo(String username, String password, boolean enabled,
      boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities, Long id, UserDto userDto,
      DomainDto domainDto, Map<String, Set<String>> permMap,
      Map<String, Set<PermissionDto>> permDtoMap) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities, id, userDto, domainDto, permMap, permDtoMap);
  }

  public Set<Integer> getDomainIdSet() {
    return domainIdSet;
  }

  public void setDomainIdSet(Set<Integer> domainIdSet) {
    this.domainIdSet = domainIdSet;
  }
}
