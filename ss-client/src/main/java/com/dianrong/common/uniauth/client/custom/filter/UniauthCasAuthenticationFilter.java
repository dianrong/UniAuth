package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.cas.web.CasAuthenticationFilter;

/**
 * 指定支持的AuthenticationType.
 * 
 * @author wanglin
 */
public class UniauthCasAuthenticationFilter extends CasAuthenticationFilter
    implements UniauthAuthenticationFilter {

  @Override
  public AuthenticationType authenticationType() {
    return AuthenticationType.CAS;
  }
  
  @Override
  public boolean requiresAuthentication(HttpServletRequest request,
      HttpServletResponse response) {
  return super.requiresAuthentication(request, response);
}

  @Override
  public int getOrder() {
    return 100;
  }
}
