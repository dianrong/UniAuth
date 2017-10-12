package com.dianrong.common.uniauth.client.custom.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.client.custom.jwt.ComposedJWTQuery;
import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.jwt.UniauthJWTSecurity;
import com.dianrong.common.uniauth.common.jwt.exp.InvalidJWTExpiredException;
import com.dianrong.common.uniauth.common.jwt.exp.LoginJWTExpiredException;
import com.dianrong.common.uniauth.common.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * JWT拦截实现登出操作.
 * 
 * @author wanglin
 */
@Slf4j
public class UniauthJWTLogoutFilter extends LogoutFilter {
  /**
   * JWT验证工具.
   */
  private final UniauthJWTSecurity uniauthJWTSecurity;

  /**
   * 获取JWT工具类.
   */
  private JWTQuery jwtQuery = new ComposedJWTQuery();

  /**
   * 验证的方式,默认采用CAS的方式验证.
   */
  private AuthenticationType authenticationType = AuthenticationType.ALL;

  public UniauthJWTLogoutFilter(UniauthJWTSecurity uniauthJWTSecurity,
      LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
    super(logoutSuccessHandler, handlers);
    Assert.notNull(uniauthJWTSecurity);
    this.uniauthJWTSecurity = uniauthJWTSecurity;
  }

  public UniauthJWTLogoutFilter(UniauthJWTSecurity uniauthJWTSecurity, String logoutSuccessUrl,
      LogoutHandler... handlers) {
    super(logoutSuccessUrl, handlers);
    Assert.notNull(uniauthJWTSecurity);
    this.uniauthJWTSecurity = uniauthJWTSecurity;
  }

  /**
   * 重写需要登出处理的逻辑.
   */
  protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
    if (!this.authenticationType.isSupported(AuthenticationType.JWT)) {
      return false;
    }
    // 获取JWT信息.
    String jwt = jwtQuery.getJWT(request);
    if (StringUtils.hasText(jwt)) {
      try {
        uniauthJWTSecurity.getInfoFromJwt(jwt);
      } catch (LoginJWTExpiredException e) {
        log.error("JWT is expired!", e);
        return true;
      } catch (InvalidJWTExpiredException e) {
        log.error("JWT:" + jwt + "is a invalid JWT string!", e);
        return true;
      }
    }
    return false;
  }

  protected JWTQuery getJwtQuery() {
    return jwtQuery;
  }

  /**
   * 设置JWTQuery实现.
   */
  public void setJwtQuery(JWTQuery jwtQuery) {
    Assert.notNull(jwtQuery);
    this.jwtQuery = jwtQuery;
  }

  public void setAuthenticationType(AuthenticationType authenticationType) {
    Assert.notNull(authenticationType);
    this.authenticationType = authenticationType;
  }
}
