package com.dianrong.common.uniauth.client.custom.sharedomain;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.client.custom.model.SingleDomainUserExtInfo;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
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
public final class ShareDomainCasAuthenticationManager implements AuthenticationProvider {

  private List<AuthenticationProvider> providers = Collections.emptyList();

  public ShareDomainCasAuthenticationManager(List<AuthenticationProvider> providers) {
    Assert.notNull(providers);
    setProviders(providers);
  }

  public List<AuthenticationProvider> getProviders() {
    return Collections.unmodifiableList(providers);
  }

  public void setProviders(List<AuthenticationProvider> providers) {
    Assert.notNull(providers);
    this.providers = providers;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    for (AuthenticationProvider provider : getProviders()) {
      if (!provider.supports(authentication.getClass())) {
        continue;
      }
      log.debug("Authentication attempt using " + provider.getClass().getName());
      Authentication authenticate = provider.authenticate(authentication);
      // 针对CasAuthenticationProvider做一下特殊处理.
      if (provider instanceof CasAuthenticationProvider) {
        grantAuthoritiesMapperProcess(provider, authenticate.getPrincipal());
      }
      return new ShareDomainAuthentication(authenticate, authenticate.getPrincipal());
    }
    throw new ProviderNotFoundException(authentication.getClass().getName() + " not supported!");
  }

  /**
   * GrantAuthoritiesMapperProcess.
   */
  private void grantAuthoritiesMapperProcess(AuthenticationProvider provider, Object principal) {
    if (!(principal instanceof UserExtInfo)) {
      return;
    }
    GrantedAuthoritiesMapper authoritiesMapper =
        (GrantedAuthoritiesMapper) ReflectionUtils.getField(provider, "authoritiesMapper");
    if (authoritiesMapper == null) {
      log.warn(
          "Please check AuthenticationProvider implementation,whether there is a field type of  "
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

  @Override
  public boolean supports(Class<?> authentication) {
    for (AuthenticationProvider provider : this.providers) {
      if (provider.supports(authentication)) {
        return true;
      }
    }
    return false;
  }
}
