package com.dianrong.common.uniauth.cas.action.jwt;

import com.dianrong.common.uniauth.cas.service.jwt.JWTCookieGenerator;
import com.dianrong.common.uniauth.common.jwt.UniauthJWTSecurity;
import com.dianrong.common.uniauth.common.jwt.exp.InvalidJWTExpiredException;
import com.dianrong.common.uniauth.common.jwt.exp.LoginJWTExpiredException;
import com.dianrong.common.uniauth.common.util.Assert;

import lombok.extern.slf4j.Slf4j;

import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Check JWT的Action.
 * 
 * @author wanglin
 *
 */
@Slf4j
public class JWTCheckAction extends AbstractAction {

  public static final String NOT_EXISTS = "notExists";

  public static final String INVALID = "invalid";

  public static final String VALID = "valid";

  private UniauthJWTSecurity uniauthJWTSecurity;
  
  private JWTCookieGenerator jwtCookieGenerator;

  public JWTCheckAction(UniauthJWTSecurity uniauthJWTSecurity, JWTCookieGenerator jwtCookieGenerator) {
    Assert.notNull(uniauthJWTSecurity);
    Assert.notNull(jwtCookieGenerator);
    this.uniauthJWTSecurity = uniauthJWTSecurity;
    this.jwtCookieGenerator = jwtCookieGenerator;
  }

  /**
   * 查看JWT的存在,是否异常的Action.
   * 
   * @return {@link #NOT_EXISTS}, {@link #INVALID}, or {@link #VALID}.
   */
  @Override
  protected Event doExecute(final RequestContext requestContext) throws Exception {
    // 获取JWT
    String jwt = jwtCookieGenerator.retrieveCookieValue(WebUtils.getHttpServletRequest(requestContext));
    if (!StringUtils.hasText(jwt)) {
      return new Event(this, NOT_EXISTS);
    }

    String eventId = INVALID;
    try {
      this.uniauthJWTSecurity.getInfoFromJwt(jwt);
      eventId = VALID;
    } catch (final LoginJWTExpiredException | InvalidJWTExpiredException e) {
      log.error("Get a invalid JWT!", e);
    }
    return new Event(this, eventId);
  }
}
