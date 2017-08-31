package com.dianrong.common.uniauth.cas.action.jwt;

import com.dianrong.common.uniauth.cas.controller.support.CasLoginSupport;
import com.dianrong.common.uniauth.cas.service.jwt.JWTCookieGenerator;
import com.dianrong.common.uniauth.common.jwt.exp.LoginJWTCreateFailedException;
import com.dianrong.common.uniauth.common.util.Assert;

import lombok.extern.slf4j.Slf4j;

import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.execution.RequestContext;

/**
 * 身份认证成功,生成JWT.
 */
@Slf4j
public class JWTCreateAction {

  private final JWTCookieGenerator jwtCookieGenerator;

  private final CasLoginSupport casLoginSupport;

  public JWTCreateAction(CasLoginSupport casLoginSupport, JWTCookieGenerator jwtCookieGenerator) {
    Assert.notNull(casLoginSupport);
    Assert.notNull(jwtCookieGenerator);
    this.casLoginSupport = casLoginSupport;
    this.jwtCookieGenerator = jwtCookieGenerator;
  }

  /**
   * 生成JWT.
   * 
   * @return 是否正常生成JWT.
   */
  public boolean doExecute(final RequestContext context) {
    final String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);
    final String ticketGrantingTicketValueFromCookie =
        (String) context.getFlowScope().get("ticketGrantingTicketId");
    String tgtId = ticketGrantingTicketId == null ? ticketGrantingTicketValueFromCookie
        : ticketGrantingTicketId;
    if (tgtId == null) {
      return false;
    }
    // set JWT cookie
    try {
      String jwt = casLoginSupport.createJWTByTgt(ticketGrantingTicketId);
      // 设置cookie
      jwtCookieGenerator.addCookie(WebUtils.getHttpServletResponse(context), jwt);
      return true;
    } catch (LoginJWTCreateFailedException ex) {
      log.error("Failed to create JWT!", ex);
    }
    return false;
  }
}
