package com.dianrong.common.uniauth.cas.action;

import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.InvalidTicketException;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

@Slf4j
@SuppressWarnings("deprecation")
public final class GenerateServiceTicketAction extends AbstractAction {

  /**
   * Instance of CentralAuthenticationService.
   */
  @NotNull
  private CentralAuthenticationService centralAuthenticationService;

  @Override
  protected Event doExecute(final RequestContext context) {
    final Service service = WebUtils.getService(context);
    final String ticketGrantingTicket = WebUtils.getTicketGrantingTicketId(context);

    try {
      ServiceTicket serviceTicketId;
      // 如果存在ticketGrantingTicket,则代表用户已经登录成功了.直接根据TGT生成ST即可.
      if (StringUtils.hasText(ticketGrantingTicket)) {
        serviceTicketId =
            this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicket, service);
      } else {
        Credential credential = WebUtils.getCredential(context);
        serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicket,
            service, credential);
      }
      WebUtils.putServiceTicketInRequestScope(context, serviceTicketId);
      return success();
    } catch (final AuthenticationException e) {
      log.error("Could not verify credentials to grant service ticket", e);
    } catch (final TicketException e) {
      if (e instanceof InvalidTicketException) {
        this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicket);
      }
      if (isGatewayPresent(context)) {
        return result("gateway");
      }
    }

    return error();
  }

  public void setCentralAuthenticationService(
      final CentralAuthenticationService centralAuthenticationService) {
    this.centralAuthenticationService = centralAuthenticationService;
  }

  /**
   * Checks if <code>gateway</code> is present in the request params.
   *
   * @param context the context
   * @return true, if gateway present
   */
  protected boolean isGatewayPresent(final RequestContext context) {
    return StringUtils
        .hasText(context.getExternalContext().getRequestParameterMap().get("gateway"));
  }
}

