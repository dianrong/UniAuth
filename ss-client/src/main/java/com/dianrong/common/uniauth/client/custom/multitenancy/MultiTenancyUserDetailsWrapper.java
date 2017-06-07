package com.dianrong.common.uniauth.client.custom.multitenancy;

import java.security.InvalidParameterException;
import java.util.Map;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.common.enm.CasProtocal;

public class MultiTenancyUserDetailsWrapper implements
    AuthenticationUserDetailsService<CasAssertionAuthenticationToken>, InitializingBean {

  private MultiTenancyUserDetailsService userDetailsService;

  public MultiTenancyUserDetailsWrapper() {
  }

  public MultiTenancyUserDetailsWrapper(final MultiTenancyUserDetailsService userDetailsService) {
    Assert.notNull(userDetailsService, "userDetailsService cannot be null.");
    this.userDetailsService = userDetailsService;
  }

  public void afterPropertiesSet() throws Exception {
    Assert.notNull(this.userDetailsService, "UserDetailsService must be set");
  }

  public UserDetails loadUserDetails(CasAssertionAuthenticationToken token)
      throws UsernameNotFoundException {
    Assertion assertion = token.getAssertion();
    if (assertion != null && assertion.getPrincipal() != null) {
      Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
      long tenancyId = Long
          .parseLong(attributes.get(CasProtocal.DianRongCas.getTenancyIdName()).toString());
      return this.userDetailsService
          .loadUserByUsername(assertion.getPrincipal().getName(), tenancyId);
    }
    throw new InvalidParameterException("loadUserDetails new userName and tenancy id");
  }

  public void setUserDetailsService(MultiTenancyUserDetailsService aUserDetailsService) {
    this.userDetailsService = aUserDetailsService;
  }
}
