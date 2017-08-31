package com.dianrong.common.uniauth.cas.action;

import com.dianrong.common.uniauth.cas.service.jwt.JWTCookieGenerator;
import com.dianrong.common.uniauth.common.util.Assert;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.web.flow.TerminateSessionAction;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class UniauthTerminateSessionAction extends TerminateSessionAction {

  private JWTCookieGenerator jwtCookieGenerator;

  public UniauthTerminateSessionAction(CentralAuthenticationService cas,
      CookieRetrievingCookieGenerator tgtCookieGenerator,
      CookieRetrievingCookieGenerator warnCookieGenerator, 
      JWTCookieGenerator jwtCookieGenerator) {
    super(cas, tgtCookieGenerator, warnCookieGenerator);
    Assert.notNull(jwtCookieGenerator);
    this.jwtCookieGenerator = jwtCookieGenerator;
  }
  
  /**
   * 清除JWT.
   */
  public Event terminate(final RequestContext context) {
    Event event = super.terminate(context);
    // remove jwt
    jwtCookieGenerator.removeCookie(WebUtils.getHttpServletResponse(context));
    return event;
  }
}
