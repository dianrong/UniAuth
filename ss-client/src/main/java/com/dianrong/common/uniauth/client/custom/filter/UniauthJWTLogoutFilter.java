package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.client.custom.jwt.JWTWebScopeUtil;
import com.dianrong.common.uniauth.client.custom.jwt.JWTWebScopeUtil.JWTUserTagInfo;
import com.dianrong.common.uniauth.client.custom.jwt.SimpleJWTQuery;
import com.dianrong.common.uniauth.common.jwt.UniauthJWTSecurity;
import com.dianrong.common.uniauth.common.jwt.UniauthUserJWTInfo;
import com.dianrong.common.uniauth.common.jwt.exp.InvalidJWTExpiredException;
import com.dianrong.common.uniauth.common.jwt.exp.LoginJWTExpiredException;
import com.dianrong.common.uniauth.common.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

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
  private JWTQuery jwtQuery = new SimpleJWTQuery();

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
    // 获取JWT信息.
    String jwt = jwtQuery.getJWT(request);
    // 获取当前线程中存储的用户信息.
    JWTUserTagInfo tagInfo = JWTWebScopeUtil.getJWTUserTagInfo(request);
    if (!StringUtils.hasText(jwt)) {
      // 没有了JWT,但是还处于登陆状态,需要登出.
      if (tagInfo != null) {
        return true;
      }
    } else {
      // 验证JWT信息.
      String identity = null;
      Long tenancyId = null;
      try {
        UniauthUserJWTInfo info = uniauthJWTSecurity.getInfoFromJwt(jwt);
        identity = info.getIdentity();
        tenancyId = info.getTenancyId();
      } catch (LoginJWTExpiredException e) {
        log.error("JWT is expired!", e);
        return true;
      } catch (InvalidJWTExpiredException e) {
        log.error("JWT:" + jwt + "is a invalid JWT string!", e);
        return true;
      }
      // 如果当前Session中的标识信息与JWT中指定的信息不一致,则登出.
      if (tagInfo != null) {
        return !tagInfo.isEquals(identity, tenancyId);
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
}
