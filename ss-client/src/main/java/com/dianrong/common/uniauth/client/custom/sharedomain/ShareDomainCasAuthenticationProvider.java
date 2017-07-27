package com.dianrong.common.uniauth.client.custom.sharedomain;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.client.custom.model.SingleDomainUserExtInfo;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.util.Assert;

/**
 * 用于处理多域共享信息的情况.
 *
 * @author wanglin
 */
@Slf4j
public final class ShareDomainCasAuthenticationProvider extends ProviderManager {

  public ShareDomainCasAuthenticationProvider(List<AuthenticationProvider> providers) {
    super(providers);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Authentication authenticate = super.authenticate(authentication);
    grantAuthoritesMapperProcess(authenticate.getPrincipal());
    return new ShareDomainAuthentication(authenticate,
        authenticate.getPrincipal());
  }

  /**
   * GrantAuthoritesMapperProcess.
   */
  private void grantAuthoritesMapperProcess(Object principal) {
    if (!(principal instanceof UserExtInfo)) {
      return;
    }
    GrantedAuthoritiesMapper authoritiesMapper =
        (GrantedAuthoritiesMapper) ReflectionUtils.getField(this, "authoritiesMapper");
    if (authoritiesMapper == null) {
      log.warn(
          "please check AuthenticationProvider implementation, whether there is a fied type of  "
              + "GrantedAuthoritiesMapper not name of authoritiesMapper. "
              + "GrantedAuthoritiesMapper is not effective");
      return;
    }

    UserExtInfo userExtInfo = (UserExtInfo) principal;
    SingleDomainUserExtInfo loginDomainUserExtInfo = userExtInfo.getLoginDomainUserExtInfo();
    if (loginDomainUserExtInfo != null) {
      userExtInfo.setLoginDomainUserExtInfo(replaceAuthorities(loginDomainUserExtInfo,
          authoritiesMapper.mapAuthorities(loginDomainUserExtInfo.getAuthorities())));
    }
    AllDomainUserExtInfo allDomainUserExtInfo = userExtInfo.getAllDomainUserExtInfo();
    if (allDomainUserExtInfo != null) {
      Set<String> allDomainCodes = allDomainUserExtInfo.getAllDomainCode();
      for (String key : allDomainCodes) {
        SingleDomainUserExtInfo domainUserExtInfo = allDomainUserExtInfo.getUserDetail(key);
        allDomainUserExtInfo.replaceUserExtInfo(key, replaceAuthorities(domainUserExtInfo,
            authoritiesMapper.mapAuthorities(domainUserExtInfo.getAuthorities())));
      }
    }
  }

  private SingleDomainUserExtInfo replaceAuthorities(final SingleDomainUserExtInfo orginalOne,
      final Collection<? extends GrantedAuthority> newAuthorities) {
    Assert.notNull(orginalOne);
    Collection<? extends GrantedAuthority> tempNewAuthorities =
        newAuthorities == null ? new ArrayList<GrantedAuthority>() : newAuthorities;
    return new SingleDomainUserExtInfo(orginalOne.getUsername(), orginalOne.getPassword(),
        orginalOne.isEnabled(), orginalOne.isAccountNonExpired(),
        orginalOne.isCredentialsNonExpired(), orginalOne.isAccountNonLocked(), tempNewAuthorities,
        orginalOne.getId(), orginalOne.getUserDto(), orginalOne.getDomainDto(),
        orginalOne.getPermMap(), orginalOne.getPermDtoMap());
  }
}
