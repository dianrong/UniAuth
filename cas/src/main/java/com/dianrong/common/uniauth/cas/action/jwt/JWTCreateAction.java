package com.dianrong.common.uniauth.cas.action.jwt;

import com.dianrong.common.uniauth.cas.service.jwt.JWTCookieGenerator;
import com.dianrong.common.uniauth.common.enm.CasProtocal;
import com.dianrong.common.uniauth.common.jwt.UniauthJWTSecurity;
import com.dianrong.common.uniauth.common.jwt.UniauthUserJWTInfo;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.execution.RequestContext;

/**
 * 身份认证成功,生成JWT.
 */
@Slf4j
public class JWTCreateAction {

  private final UniauthJWTSecurity uniauthJWTSecurity;

  private final TicketRegistry ticketRegistry;

  private JWTCookieGenerator jwtCookieGenerator;

  /**
   * JWT的过期秒数.
   */
  private int jwtExpireSeconds = 7200;

  public JWTCreateAction(UniauthJWTSecurity uniauthJWTSecurity, TicketRegistry ticketRegistry,
      JWTCookieGenerator jwtCookieGenerator) {
    Assert.notNull(uniauthJWTSecurity);
    Assert.notNull(ticketRegistry);
    Assert.notNull(jwtCookieGenerator);
    this.uniauthJWTSecurity = uniauthJWTSecurity;
    this.ticketRegistry = ticketRegistry;
    this.jwtCookieGenerator = jwtCookieGenerator;
  }

  /**
   * 生成JWT.
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
    Ticket ticket = ticketRegistry.getTicket(tgtId);
    if (ticket instanceof TicketGrantingTicket) {
      TicketGrantingTicket tgticket = (TicketGrantingTicket) ticket;
      Authentication authentication = tgticket.getAuthentication();
      Principal principal = authentication.getPrincipal();
      if (principal != null) {
        try {
          String identity = principal.getId();
          Long tenancyId = StringUtil.translateObjectToLong(
              principal.getAttributes().get(CasProtocal.DianRongCas.getTenancyIdName()));
          String jwt = uniauthJWTSecurity
              .createJwt(new UniauthUserJWTInfo(identity, tenancyId, jwtExpireSeconds * 1000L));
          // 设置cookie
          jwtCookieGenerator.addCookie(WebUtils.getHttpServletResponse(context), jwt);
          return true;
        } catch (Exception ex) {
          log.error("Failed get identity and tenancyId from principle!", ex);
        }
      }
    }
    return false;
  }

  public void setJwtExpireSeconds(int jwtExpireSeconds) {
    if (jwtExpireSeconds <= 0) {
      log.warn("Can not invalid jwtExpireSeconds {}", jwtExpireSeconds);
      return;
    }
    this.jwtExpireSeconds = jwtExpireSeconds;
  }
}
