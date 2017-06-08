package com.dianrong.common.uniauth.cas.action;

import com.dianrong.common.uniauth.cas.service.DomainService;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.repository.NoSuchFlowExecutionException;

@Slf4j
public final class InitialFlowSetupAction extends AbstractAction {

  /**
   * The services manager with access to the registry.
   **/
  @NotNull
  private ServicesManager servicesManager;

  /**
   * CookieGenerator for the Warnings.
   */
  @NotNull
  private CookieRetrievingCookieGenerator warnCookieGenerator;

  /**
   * CookieGenerator for the TicketGrantingTickets.
   */
  @NotNull
  private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

  /**
   * Extractors for finding the service.
   */
  @NotNull
  @Size(min = 1)
  private List<ArgumentExtractor> argumentExtractors;

  /**
   * Boolean to note whether we've set the values on the generators or not.
   */
  private boolean pathPopulated;

  /**
   * If no authentication request from a service is present, halt and warn the user.
   */
  private boolean enableFlowOnAbsentServiceRequest = true;

  private DomainService domainService;

  @Override
  protected Event doExecute(final RequestContext context) throws Exception {
    final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
    // 添加分之处理用户编辑管理
    String requestType = request.getParameter(AppConstants.CAS_USERINFO_MANAGE_EDIT_KEY);
    if (!StringUtil.strIsNullOrEmpty(requestType)) {
      // 往flowscope中放入一个标识符用于区分不同的流程
      context.getFlowScope().put(AppConstants.CAS_USERINFO_MANAGE_EDIT_KEY, "go");
    }
    if (!this.pathPopulated) {
      final String contextPath = context.getExternalContext().getContextPath();
      final String cookiePath = StringUtils.hasText(contextPath) ? contextPath + '/' : "/";
      log.info("Setting path for cookies to: {} ", cookiePath);
      this.warnCookieGenerator.setCookiePath(cookiePath);
      this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);
      this.pathPopulated = true;
    }

    WebUtils.putTicketGrantingTicketInScopes(context,
        this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request));

    WebUtils.putWarningCookie(context,
        Boolean.valueOf(this.warnCookieGenerator.retrieveCookieValue(request)));

    final Service service = WebUtils.getService(this.argumentExtractors, context);

    if (service != null) {
      log.debug("Placing service in context scope: [{}]", service.getId());

      final RegisteredService registeredService = this.servicesManager.findServiceBy(service);
      if (registeredService != null
          && registeredService.getAccessStrategy().isServiceAccessAllowed()) {
        log.debug("Placing registered service [{}] with id [{}] in context scope",
            registeredService.getServiceId(), registeredService.getId());
        WebUtils.putRegisteredService(context, registeredService);
      }
    } else if (!this.enableFlowOnAbsentServiceRequest) {
      log.warn(
          "No service authentication request is available at [{}]. "
          + "CAS is configured to disable the flow.",
          WebUtils.getHttpServletRequest(context).getRequestURL());
      throw new NoSuchFlowExecutionException(context.getFlowExecutionContext().getKey(),
          new UnauthorizedServiceException("screen.service.required.message",
              "Service is required"));
    }
    WebUtils.putService(context, service);
    return result("success");
  }

  public void setTicketGrantingTicketCookieGenerator(
      final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
    this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
  }

  public void setWarnCookieGenerator(final CookieRetrievingCookieGenerator warnCookieGenerator) {
    this.warnCookieGenerator = warnCookieGenerator;
  }

  public void setArgumentExtractors(final List<ArgumentExtractor> argumentExtractors) {
    this.argumentExtractors = argumentExtractors;
  }

  /**
   * Set the service manager to allow access to the registry to retrieve the registered service
   * details associated with an incoming service. Since 4.1
   *
   * @param servicesManager the services manager
   */
  public void setServicesManager(final ServicesManager servicesManager) {
    this.servicesManager = servicesManager;
  }

  /**
   * Decide whether CAS should allow authentication requests when no service is present in the
   * request. Default is enabled.
   *
   * @param enableFlowOnAbsentServiceRequest the enable flow on absent service request
   */
  public void setEnableFlowOnAbsentServiceRequest(final boolean enableFlowOnAbsentServiceRequest) {
    this.enableFlowOnAbsentServiceRequest = enableFlowOnAbsentServiceRequest;
  }

  public DomainService getDomainService() {
    return domainService;
  }

  public void setDomainService(DomainService domainService) {
    this.domainService = domainService;
  }
}
