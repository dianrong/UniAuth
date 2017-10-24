package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.client.custom.auth.AuthenticationTagHolder;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.repo.DynamicSecurityContext;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public abstract class UniauthAbstractAuthenticationFilter extends
    AbstractAuthenticationProcessingFilter implements Ordered {

  protected UniauthAbstractAuthenticationFilter(String defaultFilterProcessesUrl) {
    super(defaultFilterProcessesUrl);
  }

  protected UniauthAbstractAuthenticationFilter(
      RequestMatcher requiresAuthenticationRequestMatcher) {
    super(requiresAuthenticationRequestMatcher);
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    try {
      // 或者已经认证过,则不再认证.
      if (!AuthenticationTagHolder.isAuthenticated()) {
        // 切换SecurityContext
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext instanceof DynamicSecurityContext) {
          ((DynamicSecurityContext) securityContext).setSecurityContext(authenticationType());
        }
        super.doFilter(req, res, chain);
      } else {
        chain.doFilter(req, res);
      }
    } finally {
      if (AuthenticationTagHolder.isAuthenticated(authenticationType())) {
        AuthenticationTagHolder.remove();
      }
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain, Authentication authResult)
      throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    // 认证成功, 添加认证的标识
    AuthenticationTagHolder.authenticated(authenticationType());
  }

  @Override
  public int getOrder() {
    return authenticationType().getOrder();
  }

  /**
   * 返回当前Filter的AuthenticationType, 不能为空.
   */
  protected abstract AuthenticationType authenticationType();
}
